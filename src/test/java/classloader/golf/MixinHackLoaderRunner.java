package classloader.golf;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.launch.knot.Knot2;

public class MixinHackLoaderRunner extends BlockJUnit4ClassRunner {
	private static final ClassLoader KNOT_CLASS_LOADER = new Knot2(EnvType.SERVER).init(new String[] {"nogui"});

	/**
	 * Creates a BlockJUnit4ClassRunner to run {@code klass}
	 *
	 * @throws InitializationError if the test class is malformed.
	 */
	public MixinHackLoaderRunner(Class<?> klass) throws InitializationError, ClassNotFoundException {
		super(Class.forName(klass.getName(), true, KNOT_CLASS_LOADER));
	}

	@Override
	public void run(RunNotifier notifier) {
		try {
			Thread thread = new Thread(() -> super.run(notifier));
			thread.setContextClassLoader(KNOT_CLASS_LOADER);
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
