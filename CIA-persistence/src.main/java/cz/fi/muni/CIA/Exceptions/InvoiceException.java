package cz.fi.muni.CIA.Exceptions;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class InvoiceException extends RuntimeException {

	public InvoiceException() {
	}

	public InvoiceException(String message) {
		super(message);
	}

	public InvoiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvoiceException(Throwable cause) {
		super(cause);
	}

	public InvoiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
