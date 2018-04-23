package cz.fi.muni.CIA.Exceptions;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class PropertyException extends Exception {
	public PropertyException() {
	}

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyException(Throwable cause) {
		super(cause);
	}

	public PropertyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
