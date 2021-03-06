package cz.fi.muni.CIA.Exceptions;

/**
 * Exception describing problems with Owner
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class OwnerException extends RuntimeException {

    public OwnerException() {
        super();
    }

    public OwnerException(String s) {
        super(s);
    }

    public OwnerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OwnerException(Throwable throwable) {
        super(throwable);
    }

    protected OwnerException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
