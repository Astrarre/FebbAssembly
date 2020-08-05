package io.github.iridis.internal.events.eams;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.iridis.internal.Constants;
import io.github.iridis.internal.asm.IridisAsm;
import net.devtech.nanoevents.api.event.EventApplyManager;
import net.devtech.nanoevents.api.event.filters.MixinFilters;
import net.devtech.nanoevents.api.listeners.InvokerTargets;
import net.devtech.nanoevents.util.Id;
import org.ini4j.Profile;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityDamageEventApplyManager implements EventApplyManager, Opcodes {
	private Id id;

	@Override
	public void init(Id id, Profile.Section eventProperties) {
		this.id = id;
	}

	@Override
	public void handle(Profile.Section event, MixinFilters filters, InvokerTargets targets) {
		String listener = event.getOrDefault("listeners", "")
		                       .replace('.', '/');
		String entityClasses = event.getOrDefault("entities", "")
		                            .replace('.', '/');
		String desc = event.get("desc");
		for (String apiCls : entityClasses.split(",")) {
			String entityClass = IridisAsm.getMinecraftFromApi(apiCls);
			ClassTinkerers.addTransformation(entityClass, c -> {
				MethodNode node = null;
				label:
				for (String s : listener.split(",")) {
					if (node != null) {
						write(node, s, desc == null ? apiCls : desc);
						continue;
					}

					for (MethodNode method : c.methods) {
						if (Constants.DAMAGE_METHOD_NAME.equals(method.name) && Constants.DAMAGE_METHOD_DESC.equals(method.desc)) {
							// detected
							write(method, s, desc == null ? apiCls : desc);
							node = method;
							continue label;
						}
					}

					MethodNode method = new MethodNode(ACC_PUBLIC, Constants.DAMAGE_METHOD_NAME, Constants.DAMAGE_METHOD_DESC, null, null);
					node = method;
					write(method, s, desc == null ? apiCls : desc);
					// write super call
					method.visitVarInsn(ALOAD, 0);
					method.visitVarInsn(ALOAD, 1);
					method.visitVarInsn(FLOAD, 2);
					method.visitMethodInsn(INVOKESPECIAL, Constants.ENTITY_CLASS_NAME, Constants.DAMAGE_METHOD_NAME, Constants.DAMAGE_METHOD_DESC, false);
					method.visitInsn(IRETURN);
				}

				// override time
			});
		}
	}

	private static void write(MethodNode node, String listener, String descOrApiClass) {
		InsnList insns = new InsnList();
		insns.add(new VarInsnNode(ALOAD, 0));
		insns.add(new VarInsnNode(ALOAD, 1));
		insns.add(new VarInsnNode(FLOAD, 2));
		int index = listener.indexOf("::");
		insns.add(new MethodInsnNode(INVOKESTATIC,
		                             listener.substring(0, index),
		                             listener.substring(index + 2),
		                             descOrApiClass.startsWith("(") ? descOrApiClass : String.format(Constants.ENTITY_DAMAGE_LISTENER_DESC,
		                                                                                             descOrApiClass))); // todo replace with signature accepting "s"
		LabelNode ret = new LabelNode();
		insns.add(new JumpInsnNode(IFEQ, ret));
		insns.add(new InsnNode(ICONST_0));
		insns.add(new InsnNode(IRETURN));
		insns.add(ret);
		insns.add(node.instructions);
		node.instructions = insns;
	}

	@Override
	public void noListeners(MixinFilters filters, InvokerTargets targets) {}
}
