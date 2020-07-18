import abstractor.ClassAbstractionType
import abstractor.MemberAbstractionType
import abstractor.TargetSelector
import metautils.api.ClassApi
import metautils.api.uniqueIdentifier
import metautils.util.applyIf


data class AbstractionSelections(val interfaces: AbstractionSelection, val baseclasses: AbstractionSelection) {
    fun toTargetSelector() : TargetSelector {
        val compiledInterfaces = interfaces.compile()
        val compiledBaseclasses = baseclasses.compile()

        fun String.selectedAbstractionType(
            classApi: ClassApi, memberSelectionsProperty: (CompiledAbstractedClass) -> List<Regex>?
        ): MemberAbstractionType {
            val selectedForBaseclass = isSelected(classApi, compiledInterfaces, memberSelectionsProperty)
            val selectedForInterface = isSelected(classApi, compiledBaseclasses, memberSelectionsProperty)

            return when {
                selectedForBaseclass && selectedForBaseclass -> MemberAbstractionType.BaseclassAndInterface
                selectedForBaseclass -> MemberAbstractionType.Baseclass
                selectedForInterface -> MemberAbstractionType.Interface
                else -> MemberAbstractionType.None
            }
        }

        return TargetSelector(
            classes = {
                when {
                    it.isSelected(compiledBaseclasses) -> ClassAbstractionType.BaseclassAndInterface
                    it.isSelected(compiledInterfaces) -> ClassAbstractionType.Interface
                    else -> ClassAbstractionType.None
                }
            },
            fields = { classApi, field ->
                field.name.selectedAbstractionType(classApi) { it.fields }
            },
            methods = { classApi, method ->
                method.uniqueIdentifier().selectedAbstractionType(classApi) { it.methods }
            }
        )
    }

    private fun ClassApi.getSelection(selections: CompiledAbstractionSelection): CompiledAbstractedClass? {
        val packageRegexes = selections[name.packageName!!.toSlashQualified()] ?: return null
        return packageRegexes.find { (regex, _) -> regex.matches(name.shortName.toDollarQualifiedString()) }?.second
    }

    private fun ClassApi.isSelected(selections: CompiledAbstractionSelection) = getSelection(selections) != null


    private inline fun String.isSelected(
        classApi: ClassApi, selections: CompiledAbstractionSelection,
        memberSelectionsProperty: (CompiledAbstractedClass) -> List<Regex>?
    ): Boolean {
        val classSelection = classApi.getSelection(selections)
        // If the class is not selected then abstractor will take care of not abstracting it and its members itself,
        // but abstractor does some optimizations so it's better to leave it up to it
                ?: return true
        val memberSelections = memberSelectionsProperty(classSelection) ?: return true
        return memberSelections.any { it.matches(this) }
    }

}


private typealias CompiledAbstractionSelection = Map<String, CompiledAbstractedPackage>

private fun AbstractionSelection.compile(): CompiledAbstractionSelection = value.mapValues { (_, v) -> v.compile() }

private typealias CompiledAbstractedPackage = List<Pair<Regex, CompiledAbstractedClass>>

private fun AbstractedPackage.compile(): CompiledAbstractedPackage = map { (k, v) -> k.toRegex(v.regex) to v.compile() }

private class CompiledAbstractedClass(
    val fields: List<Regex>? = null,
    val methods: List<Regex>? = null
)

private fun AbstractedClass.compile() = CompiledAbstractedClass(fields.compile(regex), methods.compile(regex))
private fun List<String>?.compile(regex: Boolean) = this?.map { it.toRegex(regex) }

private fun String.toRegex(isRegex: Boolean) = applyIf(!isRegex) { "^$this\$" }.toRegex()

