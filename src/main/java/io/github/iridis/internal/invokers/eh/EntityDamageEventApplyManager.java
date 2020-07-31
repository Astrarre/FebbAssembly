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
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityDamageEventApplyManager implements EventApplyManager, Opcodes {
	private Id id;
	private String entityClass;

	@Override
	public void init(Id id, Profile.Section eventProperties) {
		this.id = id;
		this.entityClass = IridisAsm.getMinecraftFromApi(eventProperties.get("entity"));
	}

	@Override
	public void handle(Profile.Section event, MixinFilters filters, InvokerTargets targets) {
		String listener = event.get("listeners");
		for (String s : listener.split(",")) {
			ClassTinkerers.addTransformation(this.entityClass, c -> {
				for (MethodNode method : c.methods) {
					if(Constants.DAMAGE_METHOD_NAME.equals(method.name) && Constants.DAMAGE_METHOD_DESC.equals(method.desc)) {
						// detected

						return;
					}
				}

				// override time
			});
		}
	}

	private static void write(MethodNode node, String s) {
		InsnList insns = new InsnList();
		insns.insert(new VarInsnNode(ALOAD, 0));
		insns.insert(new VarInsnNode(ALOAD, 1));
		insns.insert(new VarInsnNode(FLOAD, 2));
		int index = s.indexOf("::");
		insns.insert(new MethodInsnNode(INVOKESTATIC, s.substring(0, index), s.substring(index + 2), Constants.ENTITY_DAMAGE_LISTENER_DESC));
		// todo if true, return false
	}

	@Override
	public void noListeners(MixinFilters filters, InvokerTargets targets) {
		targets.link(this.id, Constants.ENTITY_DAMAGE_EVENTS_INVOKER);
	}
}
