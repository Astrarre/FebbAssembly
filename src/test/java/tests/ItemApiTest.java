package tests;

import classloader.golf.MixinHackLoaderRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import v1_16_1.net.minecraft.item.IItem;
import v1_16_1.net.minecraft.util.IIdentifier;
import v1_16_1.net.minecraft.util.registry.IRegistry;

import net.minecraft.Bootstrap;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;

@RunWith (MixinHackLoaderRunner.class)
public class ItemApiTest {
	private static IItem item;

	@Before
	public void before() {
		Bootstrap.initialize();
		item = IRegistry.register(IRegistry.getITEM(),
		                          IIdentifier.create("mymod", "myitem"),
		                          IItem.create(IItem.Settings.create()
		                                                     .burnTime(10)));
		System.out.println("help");
	}

	@Test
	public void burnTime() {
		Assert.assertEquals((int)AbstractFurnaceBlockEntity.createFuelTimeMap().get(item), 10);
	}
}
