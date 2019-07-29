package shiftman.server;

/**
 * Checked Exception checking if start time / end time are valid.
 * @author Martin Tiangco
 *
 */
public class TimeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeException(String msg) {
		super(msg);
	}
}
