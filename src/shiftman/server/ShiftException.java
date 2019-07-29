package shiftman.server;

/**
 * Checked exception checking if shift has been added before assigning staff to that shift.
 * @author Martin Tiangco
 *
 */
public class ShiftException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShiftException(String msg) {
		super(msg);
	}
}
