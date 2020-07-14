import net.fabricmc.mapping.tree.TinyTree
import net.fabricmc.tinyremapper.IMappingProvider

object TinyRemapperMappingsHelper {
    private fun memberOf(
        className: String,
        memberName: String,
        descriptor: String
    ): IMappingProvider.Member {
        return IMappingProvider.Member(className, memberName, descriptor)
    }

    fun create(
        mappings: TinyTree,
        from: String?,
        to: String?,
        remapLocalVariables: Boolean
    ): IMappingProvider {
        return IMappingProvider { acceptor ->
            for (classDef in mappings.classes) {
                val className: String = classDef.getName(from)
                acceptor.acceptClass(className, classDef.getName(to))
                for (field in classDef.fields) {
                    acceptor.acceptField(
                        memberOf(
                            className,
                            field.getName(from),
                            field.getDescriptor(from)
                        ), field.getName(to)
                    )
                }
                for (method in classDef.methods) {
                    val methodIdentifier: IMappingProvider.Member =
                        memberOf(className, method.getName(from), method.getDescriptor(from))
                    acceptor.acceptMethod(methodIdentifier, method.getName(to))
                    if (remapLocalVariables) {
                        for (parameter in method.parameters) {
                            acceptor.acceptMethodArg(
                                methodIdentifier,
                                parameter.localVariableIndex,
                                parameter.getName(to)
                            )
                        }
                        for (localVariable in method.localVariables) {
                            acceptor.acceptMethodVar(
                                methodIdentifier, localVariable.localVariableIndex,
                                localVariable.localVariableStartOffset, localVariable.localVariableTableIndex,
                                localVariable.getName(to)
                            )
                        }
                    }
                }
            }
        }
    }
}