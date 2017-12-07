package org.citydb.plugins.ade_manager.script;

public class CcgException extends Exception {

	private static final long serialVersionUID = -1115965491412100047L;

	public CcgException() {
		super();
	}
	
	public CcgException(String message) {
		super(message);
	}
	
	public CcgException(Throwable cause) {
		super(cause);
	}
	
	public CcgException(String message, Throwable cause) {
		super(message, cause);
	}
}
