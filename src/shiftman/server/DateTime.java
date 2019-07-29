package shiftman.server;

/**
 * The DateTime class has the following functionality:
 * 		- checks if start and end times are valid (in the range 00:00-23:59, startTime != endTime, startTime is before endTime)
 * 		- compares shifts in chronological order
 * All measurements are in minutes
 * A day consists of 0 mins (00:00) to 1439 mins (23:59)
 * @author Martin Tiangco
 *
 */

public class DateTime {
	//fields
	private int _start;
	private int _end;
	
	/**
	 * Creates DateTime object with only startTime parameter (which slightly cleans up code in Shift class)
	 * and converts String to integer (in minutes)
	 * @param startTime
	 */
	public DateTime(String startTime) { 
		_start = Integer.parseInt(startTime.substring(0, 2)) * 60 + Integer.parseInt(startTime.substring(3, 5));
	}
	
	/**
	 * Creates DateTime object with startTime and endTime parameters, and converts String to integer (in minutes)
	 * @param startTime
	 * @param endTime
	 */
	public DateTime(String startTime, String endTime) { 
		_start = Integer.parseInt(startTime.substring(0, 2)) * 60 + Integer.parseInt(startTime.substring(3, 5));
		_end = 	Integer.parseInt(endTime.substring(0, 2)) * 60 + Integer.parseInt(endTime.substring(3, 5));
	}
	
	/**
	 * Checks if the working hours are valid
	 * Valid means:
	 * 		- time is between 00:00 (0 mins) and 23:59 (1439 mins) 
	 * 		- startTime != endTime
	 * 		- startTime is BEFORE endTime
	 * @return true if valid, false otherwise
	 */
	public boolean checkIfValidWorkingHours() {
		if ((_start >= 0 && _start < 1440  
				&& _end >= 0 && _end < 1440)
				&& _start < _end) {				//check if startTime is before endTime and startTime != endTime
			return true;
		}
		return false;
	}
	
	/**
	 * Used when ordering Shifts in compareTo method in Shift class
	 * @param other - other DateTime object being compared to
	 * @return 1 if "this" starts after other shift, -1 if "this" starts before
	 */
	public int compareStart(DateTime other) {
		if (this._start > other._start) {
			return 1;
		} else {
			return -1;
		}
	}
}
