import metautils.api.AbstractionType
import metautils.api.ClassAbstractionType
import metautils.api.ClassApi
import metautils.api.TargetSelector
import metautils.util.applyIf
import metautils.util.toDollarString
import metautils.util.toSlashString


data class AbstractionSelections(val interfaces: AbstractionSelection, val baseclasses: AbstractionSelection) {
    fun toTargetSelector() : TargetSelector {
        val compiledInterfaces = interfaces.compile()
        val compiledBaseclasses = baseclasses.compile()

        fun String.selectedAbstractionType(
                classApi: ClassApi, memberSelectionsProperty: (CompiledAbstractedClass) -> List<Regex>?
        ): AbstractionType {
            val selectedForBaseclass = isSelected(classApi, compiledBaseclasses, memberSelectionsProperty)
            val selectedForInterface = isSelected(classApi, compiledInterfaces, memberSelectionsProperty)

            return when {
                selectedForBaseclass && selectedForInterface -> AbstractionType.BaseclassAndInterface
                selectedForBaseclass -> AbstractionType.Baseclass
                selectedForInterface -> AbstractionType.Interface
                else -> AbstractionType.None
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
            fields = {  field ->
                field.name.selectedAbstractionType(field.classIn) { it.fields }
            },
            methods = {  method ->
                method.locallyUniqueIdentifier.selectedAbstractionType(method.classIn) { it.methods }
            }
        )
    }

    private fun ClassApi.getSelection(selections: CompiledAbstractionSelection): CompiledAbstractedClass? {
        val packageRegexes = selections[name.packageName.toSlashString()] ?: return null
        return packageRegexes.find { (regex, _) -> regex.matches(name.shortName.toDollarString()) }?.second
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

private fun AbstractedPackage.compile(): CompiledAbstractedPackage = map { (k, v) -> k.toRegex( v.regex) to v.compile() }

private class CompiledAbstractedClass(
    val fields: List<Regex>? = null,
    val methods: List<Regex>? = null
)

private fun AbstractedClass.compile() = CompiledAbstractedClass(fields.compile(regex), methods.compile(regex))
private fun List<String>?.compile(regex: Boolean) = this?.map { it.toRegex(regex) }

private fun String.toRegex(isRegex: Boolean) = applyIf(!isRegex) { "^${Regex.escape(this)}\$" }.toRegex()

