//    "gameVersion": "1.16.1",
//    "separator": "+build.",
//    "build": 21,
//    "maven": "net.fabricmc:yarn:1.16.1+build.21",
//    "version": "1.16.1+build.21",
//    "stable": false

//@Serializable
//data class YarnVersion(
//    val gameVersion: String,
//    val seperator: String,
//    val build: Int,
//    val maven: String,
//    val version: String,
//    val stable: Boolean
//)

object Fabric {
//    private val json = Json(JsonConfiguration.Stable)

    fun getYarnMappingsUrl(mcVersion: String, build: Int) =
        "https://maven.fabricmc.net/net/fabricmc/yarn/$mcVersion%2Bbuild.$build/yarn-$mcVersion+build.$build-v2.jar"
    fun getMergedMappingsUrl(mcVersion: String, build: Int) =
        "https://maven.fabricmc.net/net/fabricmc/yarn/$mcVersion%2Bbuild.$build/yarn-$mcVersion+build.$build-mergedv2.jar"

    fun getIntermediariesUrl(mcVersion: String) =
        "https://maven.fabricmc.net/net/fabricmc/intermediary/$mcVersion/intermediary-$mcVersion-v2.jar"


//    fun downloadYarnVersions(mcVersion: String): List<YarnVersion> = json.parse(
//        YarnVersion.serializer().list,
//        downloadUtfStringFromUrl("https://meta.fabricmc.net/v2/versions/yarn/$mcVersion")
//    )
}

//fun List<YarnVersion>.getMavenUrlOfBuild(build: Int){
//    val version = first { it.build == build }
//    val url = "https://maven.fabricmc.net/net/fabricmc/yarn/1.15.1%2Bbuild.9/"
//}