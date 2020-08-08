package io.github.iridis.api.context;

public class IllegalContextException extends IllegalStateException {
	public IllegalContextException() {
	}

	public IllegalContextException(String s) {
		super(s);
	}

	public IllegalContextException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalContextException(Throwable cause) {
		super(cause);
	}
}
