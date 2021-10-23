package huffman.exception;

public class IllegalInputException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4743308369734909296L;
	public IllegalInputException(String msg) {
		super(msg);
	}
	public IllegalInputException(Throwable t) {
		super(t);
	}
}
