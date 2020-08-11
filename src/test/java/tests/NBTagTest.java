package tests;

import classloader.golf.MixinHackLoaderRunner;
import io.github.iridis.api.data.NBTList;
import io.github.iridis.api.data.NBTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import net.minecraft.Bootstrap;

@RunWith(MixinHackLoaderRunner.class)
public class NBTagTest {
	@Before
	public void before() {
		Bootstrap.initialize();
	}

	@Test
	public void testTag() {
		NBTag tag = NBTag.create();
		tag.put(NBTag.Type.STRING, "key", "value");
		Assert.assertEquals("value", tag.get(NBTag.Type.STRING, "key"));
	}

	@Test
	public void testList() {
		NBTList<String> strings = NBTag.createListTag(NBTag.Type.STRING);
		strings.add("test1");
		strings.add("test2");
		strings.add("test3");
		Assert.assertEquals(strings.get(0), "test1");
		Assert.assertEquals(strings.get(1), "test2");
		Assert.assertEquals(strings.get(2), "test3");
	}
}
