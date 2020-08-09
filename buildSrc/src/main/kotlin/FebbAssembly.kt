import abstractor.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import metautils.testing.getResource
import metautils.testing.verifyClassFiles
import metautils.util.*
import net.fabricmc.mapping.tree.TinyMappingFactory
import net.fabricmc.mapping.tree.TinyTree
import net.fabricmc.stitch.merge.JarMerger
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap

class FebbAssembly : Plugin<Project> {
    companion object {
        lateinit var artifacts: ProjectContext
    }

    override fun apply(project: Project) {
        ProjectContext(project).apply {
            artifacts = this
            apply()
        }
    }
}


class ProjectContext(val project: Project) {
    private val sourceSets = project.convention.getPlugin(JavaPluginConvention::class.java).sourceSets
    private val resourcesOutputDir = sourceSets.getByName("main").output.resourcesDir!!
    private val classesOutputDir = sourceSets.getByName("main").output.classesDirs.first().toPath()
//    final SourceSetContainer sourceSets = javaPlugin.getSourceSets();

    private val mcVersion = project.property("minecraft_version").toString()
    private val mappingsBuild = project.property("mappings_build").toString().toInt()
    private val apiBuild = project.property("api_build").toString().toInt()
    private val febbDir = project.buildDir.resolve("febbAssembly").toPath()
    private val versionDir = febbDir.resolve(mcVersion)
    private val minecraftVersionManifest = versionDir.resolve("version_manifest.json")
    private val minecraftDir = versionDir.resolve("minecraft")
    private val clientPath = minecraftDir.resolve("client.jar")
    private val serverPath = minecraftDir.resolve("server.jar")
    private val mergedPath = minecraftDir.resolve("merged.jar")
    private val remappedMcPath = minecraftDir.resolve("named.jar")
    private val libsDir = versionDir.resolve("libraries")
    private val mappingsDir = versionDir.resolve("mappings")
    private val mappingsJar = mappingsDir.resolve("yarn-build.$mappingsBuild.jar")
    private val mappingsPath = mappingsDir.resolve("yarn-build.$mappingsBuild.tinyv2")
    private val abstractedDir = versionDir.resolve("abstracted")
    private val abstractedDirectOutputDir = abstractedDir.resolve("directOutput")
    private val apiBinariesDir = abstractedDirectOutputDir.resolve("api")
    private val apiSourcesDir = abstractedDirectOutputDir.resolve("api-sources")

    private val abstractionManifestJson = abstractedDirectOutputDir.resolve("abstractionManifest.json")

    private val implNamedDir = abstractedDirectOutputDir.resolve("impl-named")

    private val runtimeManifestProperties = resourcesOutputDir.resolve("runtimeManifest.properties").toPath()
    private val implNamedDest = classesOutputDir
    private val currentVersionAbstractedDirInClasses = implNamedDest.resolve("v" + mcVersion.replace(".", "_"))

    private val apiForDevTesting = project.file("dev/test-api.jar").toPath()
    private val apiBinariesJar = abstractedDir.resolve("api.jar")
    private val apiSourcesJar = abstractedDir.resolve("api-sources.jar")

    private val abstractionManifestJar = abstractedDir.resolve("abstractionManifest.jar")

    private val classpath by lazy { libsDir.recursiveChildren().filter { it.hasExtension(".jar") }.toList() }
    private val mappings by lazy { Files.newBufferedReader(mappingsPath).use { TinyMappingFactory.load(it) } }
    private val configDir = project.projectDir.toPath().resolve("config")
    private val interfaceAbstractions = configDir.resolve("interfaces.conf").toAbsolutePath()
    private val baseclassAbstractions = configDir.resolve("baseclasses.conf")
    private val addedInterfaces = configDir.resolve("iinterfaces.properties")
    private val addedBaseclasses = configDir.resolve("baseinterfaces.properties")

    private fun downloadIfChanged(url: String, path: Path) {
        DownloadUtil.downloadIfChanged(URL(url), path.toFile(), project.logger)
    }

//    open class AbstractTask @Inject constructor(private val context: ProjectContext) : DefaultTask() {
////        private var noVerify: Boolean = false
//
//        init {
//            group = "FebbAssembly"
//            with(context) {
//                inputs.properties(project.properties
//                        .filterKeys { it in setOf("minecraft_version", "mappings_build", "api_build") })
//                outputs.dir(abstractedDir.toFile())
//                outputs.dir(currentVersionAbstractedDirInClasses.toFile())
//                outputs.file(runtimeManifestProperties.toFile())
//                outputs.file(apiForDevTesting.toFile())
//
//
//            }
//        }
//
//
//        @TaskAction
//        fun abstract() = with(context) {
////            val versionManifest = Minecraft.downloadVersionManifest(
////                    Minecraft.downloadVersionManifestList(), mcVersion
////            )
////            downloadMinecraft(versionManifest)
////            downloadMcLibraries(versionManifest)
////            mergeMinecraftJars()
////            downloadMappings()
////            remapMinecraftJar(classpath, mappings)
//
////            abstractMinecraft(classpath + classesOutputDir.toPath(), mappings)
////            copyImplToClassesDir()
////            copyApiForTestingInDev()
//        }
//
//    }


    fun apply() {
//        val abstractTask = project.tasks.register<AbstractTask>("abstract", AbstractTask::class.java, this)
        val downloadVersionManifest by task {
            setPropertyInputs("mcVersion" to mcVersion)
            setFileOutputs(minecraftVersionManifest)
            doLast {
                val versionManifest = Minecraft.downloadVersionManifest(
                        Minecraft.downloadVersionManifestList(), mcVersion
                )
                minecraftVersionManifest.writeString(versionManifest.toString())
            }
        }
        val downloadMinecraft by task(downloadVersionManifest) {
            setFileInputs(minecraftVersionManifest)
            setFileOutputs(clientPath, serverPath)
            doLast {
                downloadMinecraft(readVersionManifest())
            }
        }
        val downloadMcLibraries by task(downloadVersionManifest) {
            setFileInputs(minecraftVersionManifest)
            setDirOutputs(libsDir)
            doLast {
                downloadMcLibraries(readVersionManifest())
            }
        }
        val mergeMcJars by task(downloadMinecraft) {
            setFileInputs(clientPath, serverPath)
            setFileOutputs(mergedPath)
            doLast {
                mergeMinecraftJars()
            }
        }
        val downloadMappings by task {
            setPropertyInputs("mcVersion" to mcVersion, "mappings" to mappingsBuild)
            setDirOutputs(mappingsDir)
            doLast {
                downloadMappings()
            }
        }
        val remapMcJar by task(mergeMcJars, downloadMappings, downloadMcLibraries) {
            setFileInputs(mergedPath, mappingsPath)
            setDirInputs(libsDir)
            setFileOutputs(remappedMcPath)
            doLast {
                remapMinecraftJar(classpath, mappings)
            }
        }
        val abstract by task(remapMcJar, project.tasks.getByName("processResources")) {
            setPropertyInputs("apiBuild" to apiBuild)
            setFileInputs(mergedPath, mappingsPath)
            setDirInputs(libsDir,configDir)
            setDirOutputs(abstractedDir)

            doLast {
                abstractMinecraft(classpath, mappings)
            }
        }
        val setupDev by task(abstract) {
            setDirInputs(abstractedDir)
            setDirOutputs(currentVersionAbstractedDirInClasses)
            setFileOutputs(apiForDevTesting)
            doLast {
                copyImplToClassesDir()
                copyApiForTestingInDev()
            }
        }

        val verifyAbstractedJar by task(setupDev, project.tasks.getByName("classes")) {
            setDirInputs(implNamedDir, libsDir, classesOutputDir)
            setFileInputs(remappedMcPath)
            doLast {
                verifyAbstractedJar()
            }
        }


        project.tasks.getByName("build").dependsOn(verifyAbstractedJar)

        // We need the classfiles of FebbAssembly to verify the abstracted impl jar, since we use interfaces
        // from there.
        // Make sure Abstract runs before we publish
        project.tasks.getByName("prepareKotlinBuildScriptModel").dependsOn(setupDev)
        project.tasks.getByName("classes").dependsOn(setupDev)
    }

    private fun verifyAbstractedJar() {
        println("Verifying abstracted jar...")
        verifyClassFiles(implNamedDir, classpath + listOf(remappedMcPath, classesOutputDir))
    }

    private fun readVersionManifest(): JsonObject =
            Json(JsonConfiguration.Stable).parseJson(minecraftVersionManifest.readToString()).jsonObject

    private fun copyApiForTestingInDev() {
        apiForDevTesting.createParentDirectories()
        apiBinariesJar.copyTo(apiForDevTesting)
    }

    // This is so the published artifact will have the abstracted stuff, in addition to the real java code
    // we have in FebbAssembly
    private fun copyImplToClassesDir() {
        FileUtils.copyDirectory(implNamedDir.toFile(), implNamedDest.toFile())
    }


    private fun downloadMinecraft(versionManifest: JsonObject) {
        val downloads = versionManifest.getObject("downloads")
        val client = downloads.getObject("client").getPrimitive("url").content
        val server = downloads.getObject("server").getPrimitive("url").content
        minecraftDir.createDirectories()
        downloadIfChanged(client, clientPath)
        downloadIfChanged(server, serverPath)
    }

    private fun downloadMcLibraries(versionManifest: JsonObject) {
        for (library in Minecraft.getLibraryUrls(versionManifest)) {
            val path = libsDir.resolve(library.filePath)
            path.createParentDirectories()
            downloadIfChanged(library.url, path)
        }
    }

    private fun mergeMinecraftJars() {
        JarMerger(clientPath.toFile(), serverPath.toFile(), mergedPath.toFile()).use { jarMerger ->
            jarMerger.enableSyntheticParamsOffset()
            jarMerger.merge()
        }
    }

    private fun downloadMappings() {
        mappingsDir.createDirectories()
        downloadIfChanged(Fabric.getMergedMappingsUrl(mcVersion, mappingsBuild), mappingsJar)
        mappingsJar.openJar {
            it.getPath("mappings/mappings.tiny").copyTo(mappingsPath)
        }
    }

    private fun remapMinecraftJar(classpath: List<Path>, mappings: TinyTree) {
        remap(
                mappings,
                classpath,
                fromNamespace = "official",
                toNamespace = "named",
                fromPath = mergedPath,
                toPath = remappedMcPath
        )
    }

    private fun remap(
            mappings: TinyTree,
            classpath: List<Path>,
            fromNamespace: String,
            toNamespace: String,
            fromPath: Path,
            toPath: Path
    ) {
        val remapper = TinyRemapper.newRemapper()
                .withMappings(TinyRemapperMappingsHelper.create(mappings, fromNamespace, toNamespace, true))
                .renameInvalidLocals(true)
                .rebuildSourceFilenames(true)
                .build()

        OutputConsumerPath.Builder(toPath).build().use { outputConsumer ->
            outputConsumer.addNonClassFiles(fromPath)
            remapper.readClassPath(*classpath.toTypedArray())
            remapper.readInputs(fromPath)
            remapper.apply(outputConsumer)
        }
    }

    private fun abstractMinecraft(
            classpath: List<Path>,
            mappings: TinyTree
    ) {
        assert(remappedMcPath.exists())
        val metadata = createAbstractionMetadata(classpath)
        val manifest = runAbstractor(metadata)
        saveManifest(manifest, mappings)
    }

    private fun createAbstractionMetadata(classpath: List<Path>): AbstractionMetadata {
//          interfaces, baseclasses, iinterfacesPath, baseinterfacesPath ->
        val interfaceSelection = AbstractionSelection.fromHocon(interfaceAbstractions.readToString())
        val baseclassSelection = AbstractionSelection.fromHocon(baseclassAbstractions.readToString())
        val imap = HashMap<String, Collection<String>>()
        val iinterfaceProperties = Properties()
        iinterfaceProperties.load(addedInterfaces.inputStream())
        iinterfaceProperties.forEach { a, b ->
            imap[a.toString()] = b.toString().split(",")
        }

        val baseMap = HashMap<String, Collection<String>>()
        val interfaceBaseProperties = Properties()
        interfaceBaseProperties.load(addedBaseclasses.inputStream())
        interfaceBaseProperties.forEach { a, b ->
            baseMap[a.toString()] = b.toString().split(",")
        }

        return AbstractionMetadata(
                versionPackage = VersionPackage.fromMcVersion(mcVersion),
                writeRawAsm = true,
                fitToPublicApi = false,
                classPath = classpath,
                javadocs = JavaDocs.readTiny(mappingsPath),
                selector = AbstractionSelections(interfaceSelection, baseclassSelection).toTargetSelector(),
                iinterfaces = imap,
                interfacesbase = baseMap
        )
    }

    private fun runAbstractor(
            metadata: AbstractionMetadata
    ): AbstractionManifest {
        abstractedDirectOutputDir.createDirectories()
        val manifest = Abstractor.parse(mcJar = remappedMcPath, metadata = metadata) {
            it.abstract(implNamedDir, metadata)
            val apiMetadata = metadata.copy(fitToPublicApi = true)
            it.abstract(apiBinariesDir, apiMetadata)
            it.abstract(apiSourcesDir, apiMetadata.copy(writeRawAsm = false))
        }

        deleteOldAbstractedClassesInClassesDir()
//        implNamedDir.convertDirToJar(implNamedJar)
        apiBinariesDir.convertDirToJar(apiBinariesJar)
        apiSourcesDir.convertDirToJar(apiSourcesJar)
        return manifest
    }

    private fun deleteOldAbstractedClassesInClassesDir() {
        if (classesOutputDir.exists()) {
            classesOutputDir.directChildren().forEach {
                // we assume only api classes start with "v". If we accidently name something with a root package
                // of "vaccum" it will be a very sad day.
                if (it.toString().startsWith("v")) {
                    it.deleteRecursively()
                }
            }
        }
    }


    private fun saveManifest(manifest: AbstractionManifest, mappings: TinyTree) {
        abstractionManifestJson.writeString(
                Json(JsonConfiguration.Stable).stringify(AbstractionManifestSerializer, manifest)
        )

        abstractionManifestJson.storeInJar(abstractionManifestJar)

        val namedToInt = mappings.mapNamedClassesToIntermediary()

        val intDotQualifiedToApi = manifest.map { (mcClassName, apiClassInfo) ->
            namedToInt.getValue(mcClassName).replace("/", ".") to apiClassInfo.apiClassName
        }

        Properties().apply {
            putAll(intDotQualifiedToApi)
            Files.newOutputStream(runtimeManifestProperties).use { store(it, null) }
        }

//        runtimeManifestProperties.storeInJar(runtimeManifestJar)

    }

    private fun Path.storeInJar(jarPath: Path) {
        jarPath.deleteIfExists()
        jarPath.createJar()
        jarPath.openJar {
            copyTo(it.getPath("/$fileName"))
        }
    }

}

fun <T> getResources(path1: String, path2: String, path3: String, path4: String, usage: (Path, Path, Path, Path) -> T): T {
    return getResource(path1) { r1 ->
        getResource(path2) { r2 ->
            getResource(path3) { r3 ->
                getResource(path4) { r4 -> usage(r1, r2, r3, r4) }
            }
        }
    }
}