package org.unique.commons.io.filenode;


public class FileNodeNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public FileNodeNotFoundException() {
        super();
    }

    public FileNodeNotFoundException(String message) {
        super(message);
    }

    public FileNodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public FileNodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
