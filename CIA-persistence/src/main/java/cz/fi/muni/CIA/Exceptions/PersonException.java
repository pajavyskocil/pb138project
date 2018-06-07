package cz.fi.muni.CIA.Exceptions;

/**
 * Exception describing problems with Person
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class PersonException extends RuntimeException {

	public PersonException() {
	}

	public PersonException(String message) {
		super(message);
	}

	public PersonException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersonException(Throwable cause) {
		super(cause);
	}

	public PersonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
