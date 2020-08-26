package tests;

import static io.github.iridis.api.data.NBTag.create;
import static io.github.iridis.api.data.NBTag.createListTag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import classloader.golf.MixinHackLoaderRunner;
import io.github.iridis.api.data.NBTList;
import io.github.iridis.api.data.NBTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

import net.minecraft.Bootstrap;

@RunWith (MixinHackLoaderRunner.class)
public class NBTagTest {
	@Before
	public void before() {
		Bootstrap.initialize();
	}

	@Test
	@Ignore
	public void testTag() {
		ICompoundTag tag = create();
		tag.put(NBTag.Type.STRING, "key", "value");
		Assert.assertEquals("value", tag.get(NBTag.Type.STRING, "key"));
	}

	@Test
	@Ignore
	public void testList() {
		NBTList<String> strings = createListTag(NBTag.Type.STRING);
		strings.add("test1");
		strings.add("test2");
		strings.add("test3");
		Assert.assertEquals(strings.get(0), "test1");
		Assert.assertEquals(strings.get(1), "test2");
		Assert.assertEquals(strings.get(2), "test3");
	}

	@Test
	@Ignore
	public void roundTrip() throws IOException {
		ICompoundTag tag = create();
		tag.put(NBTag.Type.BOOLEAN, "bool", false);
		tag.put(NBTag.Type.BYTE, "byte", (byte) 0);
		tag.put(NBTag.Type.SHORT, "short", (short) 0);
		tag.put(NBTag.Type.CHAR, "char", 'a');
		tag.put(NBTag.Type.INT, "int", 0);
		tag.put(NBTag.Type.FLOAT, "float", 0f);
		tag.put(NBTag.Type.LONG, "long", 0L);
		tag.put(NBTag.Type.DOUBLE, "double", 0d);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		tag.writeData(new DataOutputStream(baos));
		ICompoundTag ctag = NBTag.read(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
		Assert.assertEquals(tag, ctag);
	}
}
