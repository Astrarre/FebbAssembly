package io.github.iridis.internal.invokers.eh;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.iridis.internal.Constants;
import io.github.iridis.internal.IridisAsm;
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
		String listener = event.get("listeners");
		String entityClass = IridisAsm.getMinecraftFromApi(event.get("entity").replace('.', '/'));
		ClassTinkerers.addTransformation(entityClass, c -> {
			MethodNode node = null;
			label:
			for (String s : listener.split(",")) {
				if(node != null) {
					write(node, s, entityClass);
					continue;
				}

				for (MethodNode method : c.methods) {
					if (Constants.DAMAGE_METHOD_NAME.equals(method.name) && Constants.DAMAGE_METHOD_DESC.equals(method.desc)) {
						// detected
						write(method, s, entityClass);
						node = method;
						continue label;
					}
				}

				MethodNode method = new MethodNode(ACC_PUBLIC, Constants.DAMAGE_METHOD_NAME, Constants.DAMAGE_METHOD_DESC, null, null);
				node = method;
				write(method, s, entityClass);
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

	private static void write(MethodNode node, String listener, String entityClass) {
		InsnList insns = new InsnList();
		insns.insert(new VarInsnNode(ALOAD, 0));
		insns.insert(new VarInsnNode(ALOAD, 1));
		insns.insert(new VarInsnNode(FLOAD, 2));
		int index = listener.indexOf("::");
		insns.insert(new MethodInsnNode(INVOKESTATIC, listener.substring(0, index), listener.substring(index + 2), String.format(Constants.ENTITY_DAMAGE_LISTENER_DESC, entityClass))); // todo replace with signature accepting "s"
		LabelNode ret = new LabelNode();
		insns.insert(new JumpInsnNode(IFEQ, ret));
		insns.insert(new InsnNode(ICONST_0));
		insns.insert(new InsnNode(IRETURN));
		insns.insert(ret);
		insns.add(node.instructions);
		node.instructions = insns;
	}

	@Override
	public void noListeners(MixinFilters filters, InvokerTargets targets) {
		targets.link(this.id, Constants.ENTITY_DAMAGE_EVENTS_INVOKER);
	}
}
