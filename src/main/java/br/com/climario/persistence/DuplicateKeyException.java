package br.com.climario.persistence;

public class DuplicateKeyException extends ViolationException {

	private static final long serialVersionUID = -8493506385081937813L;

	public DuplicateKeyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateKeyException(String arg0) {
		super(arg0);
	}

	public DuplicateKeyException(Throwable arg0) {
		super(arg0);
	}

}
