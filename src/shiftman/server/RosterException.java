package shiftman.server;

/**
 * A checked exception that checks:
 * 		- if shop name is valid (not empty or null)
 * 		- if dayOfWeek is valid (one of the 7 days)
 * @author Martin Tiangco
 *
 */
public class RosterException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RosterException(String msg) {
		super(msg);
	}
}
