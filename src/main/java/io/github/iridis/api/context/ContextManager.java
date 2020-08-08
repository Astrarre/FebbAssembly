package io.github.iridis.api.context;

import java.util.Objects;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@SuppressWarnings ("unchecked")
public class ContextManager {
	public static final ThreadLocal<ContextManager> INSTANCE = ThreadLocal.withInitial(ContextManager::new);

	private enum StackMarker {INSTANCE}
	// todo type sorting list for speed
	private final ObjectArrayList<Object> context = new ObjectArrayList<>();

	public static ContextManager getInstance() {
		return INSTANCE.get();
	}

	/**
	 * pushes all of the objects onto the stack, then calls the runnable, then pops all of the objects again, this is a little slow so use it sparingly
	 */
	public <T> T act(Supplier<T> action, Object... objects) {
		for (Object object : objects) {
			this.push(object);
		}

		T val = action.get();

		for (int i = objects.length - 1; i >= 0; i--) {
			this.pop(objects[i]);
		}
		return val;
	}

	/**
	 * push a new value onto the stack
	 */
	public void push(Object object) {
		this.context.push(object);
	}

	public void push(String key, Object object) {
		this.context.push(ContextKey.of(key, object));
	}

	/**
	 * pop the top value off the stack without verification
	 */
	public void pop() {
		this.context.pop();
	}

	/**
	 * pop the top of the stack and ensure the key is identical
	 */
	public void pop(Object object) {
		Object popped = this.context.pop();
		if (!Objects.equals(popped, object)) {
			this.printStack();
			throw new IllegalStateException("Expected to pop " + object + " but popped " + popped);
		}
	}

	public void pop(String key, Object object) {
		Object popped = this.context.pop();
		if(!(popped instanceof ContextKey) || !Objects.equals(((ContextKey) popped).object, object) || !Objects.equals(((ContextKey) popped).key, key)) {
			this.printStack();
			throw new IllegalStateException("Expected to pop (" + key + ',' + object + ") but popped " + popped);
		}
	}

	/**
	 * put a marker object on the stack, subsequent {@link #copyStack()} will stop before this
	 */
	public void pushStackMarker() {
		this.push(StackMarker.INSTANCE);
	}

	/**
	 * pop a marker object
	 */
	public void popStackMarker() {
		this.pop(StackMarker.INSTANCE);
	}

	/**
	 * @see #peekType(Class, int) but count = 0
	 */
	public <T> T peekFirstOfType(Class<T> type) {
		return this.peekType(type, 0);
	}

	/**
	 * @throws IllegalContextException if no instance of the class is found
	 * @see #peekFirstOfType(Class)
	 */
	public <T> T peekFirstOfTypeOrThrow(Class<T> type) {
		T obj = this.peekFirstOfType(type);
		if (obj == null) {
			throw new IllegalContextException("Expected to find context of type " + type + " but found none!");
		}
		return obj;
	}

	/**
	 * find the nth object of type T on the stack if n is greater than the number of objects of type T on the stack, the last object of type T is returned
	 * instead if there are no objects of type T on the stack, the function returns null
	 */
	public <T> T peekType(Class<T> type, int count) {
		Object last = null;
		for (int i = 0; i < this.context.size(); i++) {
			Object value = this.context.peek(i);
			if (type.isAssignableFrom(value.getClass()) || (value instanceof ContextKey && type.isAssignableFrom(((ContextKey<?>) value).getType()))) {
				count--;
				T val = (T) getObj(value);
				if (count < 0) {
					return val;
				}
				last = val;
			}
		}
		return (T) last;
	}

	public <T> T peekFirstKey(String key, Class<T> type) {
		return this.peekKey(key, type, 0);
	}

	public <T> T peekKey(String key, Class<T> type, int count) {
		Object last = null;
		for (int i = 0; i < this.context.size(); i++) {
			Object val = this.context.peek(i);
			if (val instanceof ContextKey && ((ContextKey) val).key.equals(key) && type.isAssignableFrom(((ContextKey) val).getClass())) {
				count--;
				if (count < 0) {
					return (T) ((ContextKey) val).object;
				}
				last = ((ContextKey) val).object;
			}
		}
		return (T) last;
	}

	// todo pop and push whole stack Objects
	public ObjectArrayList<Object> copyStack() {
		ObjectArrayList<Object> stack = new ObjectArrayList<>();
		for (int i = 0; i < this.context.size(); i++) {
			Object value = this.context.peek(i);
			stack.add(0, value);
			if (value == StackMarker.INSTANCE) {
				break;
			}
		}
		return stack;
	}

	public <T> T actStack(ObjectArrayList<Object> stack, Supplier<T> func) {
		if (stack != null) {
			for (Object o : stack) {
				this.push(o);
			}
		}

		T val = func.get();

		if (stack != null) {
			for (int i = stack.size() - 1; i >= 0; i--) {
				Object object = stack.get(i);
				this.pop(object);
			}
		}

		return val;
	}

	private static Object getObj(Object object) {
		return object instanceof ContextKey ? ((ContextKey) object).object : object;
	}

	public void printStack() {
		for (Object o : this.context) {
			System.out.println(o);
		}
	}
}
