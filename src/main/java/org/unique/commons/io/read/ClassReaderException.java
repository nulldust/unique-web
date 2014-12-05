package org.unique.commons.io.read;


public class ClassReaderException extends RuntimeException {

	private static final long serialVersionUID = -1L;
	
	public ClassReaderException() {
		super();
	}
	
	public ClassReaderException(Exception e) {
		super(e);
	}
	
	public ClassReaderException(String msg) {
		super(msg);
	}
	
	public ClassReaderException(String msg, Exception e) {
		super(msg, e);
	}
}
