package shiftman.server;

/**
 * Checked exception class checking if staff worker's name is empty or null.
 * @author Martin Tiangco
 *
 */
public class StaffException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StaffException(String msg) {
		super(msg);
	}
}
