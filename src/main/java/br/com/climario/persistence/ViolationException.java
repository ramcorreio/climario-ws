package br.com.climario.persistence;

import javax.persistence.PersistenceException;

public class ViolationException extends PersistenceException {

    private static final long serialVersionUID = -4865622151657410428L;

	public ViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViolationException(String message) {
        super(message);
    }

    public ViolationException(Throwable cause) {
        super(cause);
    }

}
