import net.fabricmc.mapping.tree.TinyTree

fun TinyTree.mapNamedClassesToIntermediary(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    for (classEntry in classes) {
        map[classEntry.getName("named")] = classEntry.getName("intermediary")
    }
    return map
}