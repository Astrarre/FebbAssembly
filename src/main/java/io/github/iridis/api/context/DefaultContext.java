package io.github.iridis.api.context;

public enum DefaultContext {
	/**
	 * for blaming players / entities for breaking blocks n stuff
	 */
	BLAME,
	/**
	 * this tells you the current server/world/other things in that category being currently ticked
	 */
	SERVER;

	private final ThreadLocal<ContextManager> context = ThreadLocal.withInitial(ContextManager::new);

	public ContextManager get() {
		return this.context.get();
	}
}
