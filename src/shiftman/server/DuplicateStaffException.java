package shiftman.server;

/**
 * A checked exception that checks for duplicate staff being registered.
 * @author Martin Tiangco
 *
 */
public class DuplicateStaffException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateStaffException(String msg) {
		super(msg);
	}
}
