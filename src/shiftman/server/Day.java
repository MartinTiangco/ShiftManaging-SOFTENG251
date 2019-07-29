package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Day refers to the 7 days of the week and the shifts assigned for each day
 * This class has the following functionality:
 * 		- has a list of Shifts to do with each day, and allows you to add new shifts or retrieve existing shifts.
 * 		- the enum is used to sort List<Shift> in chronological order
 * 		- can set the working hours of each day
 * 		- can assign a shift to each day
 * 		- can retrieve existing shifts in the List
 * 
 * @author Martin Tiangco
 */

enum DayOfWeek {	//used for ordering
	Monday(1), Tuesday(2), Wednesday(3), Thursday(4), Friday(5), Saturday(6), Sunday(7);
	
	private int _order;
	
	DayOfWeek(int order) {
		_order = order;
	}
	
	public int getOrder() {
		return _order;
	}
}

public class Day {
	//fields
	private final String _day;
	private boolean _workingHoursSet = false; //will be true once working hours are set
	private String _startTime;
	private String _endTime;
	private List<Shift> _shifts = new ArrayList<Shift>();
	
	/**
	 * Creates a Day object based on the string given (one of the 7 days)
	 * @param dayOfWeek
	 */
	public Day(String dayOfWeek) {
		_day = dayOfWeek;	
	}
	
	/**
	 * Sets the working hours of a day
	 * @param startTime
	 * @param endTime
	 */
	public void setStartAndEndTimes(String startTime, String endTime) {
		_startTime = startTime;
		_endTime = endTime;
		_workingHoursSet = true;
	}
	
	/**
	 * Adds a shift based on the parameters given
	 * @param dayOfWeek
	 * @param startTime
	 * @param endTime
	 * @param minimumWorkers
	 */
	public void addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		if (_workingHoursSet == true) {
			Shift _shift = new Shift(dayOfWeek, startTime, endTime, minimumWorkers);
			_shifts.add(_shift);														//add Shift object to List
			Collections.sort(_shifts);													//sort by chronological order
		}
	}
	
	/**
	 * Retrieves an existing shift based on the parameters given
	 * @param startTime
	 * @param endTime
	 * @return the existing Shift object
	 */
	public Shift getExistingShift(String startTime, String endTime) {
		//iterate through List<Shift> to find specified Shift
		for (Shift s : _shifts) {
			if (s.getStartTime().equals(startTime) && s.getEndTime().equals(endTime)) {
				return s;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return _day;
	}
	
	/**
	 * Retrieves the start time of the day's working hours
	 * @return start time
	 */
	public String getStartWorkingHours() {
		return _startTime;
	}
	
	/**
	 * Retrieves the end time of the day's working hours
	 * @return end time
	 */
	public String getEndWorkingHours() {
		return _endTime;
	}
	
	/**
	 * Retrieves the list of Shifts for a specified day
	 * @return list of shifts
	 */
	public List<Shift> getListOfShifts() {
		return _shifts;
	}
}
