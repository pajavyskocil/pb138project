package cz.fi.muni.CIA.Exceptions;

/**
 * Exception describing problems with generating PDF
 *
 * @author Andrej Dravecký
 */
public class GeneratorException extends RuntimeException {

	public GeneratorException() {
	}

	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratorException(Throwable cause) {
		super(cause);
	}

	public GeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
