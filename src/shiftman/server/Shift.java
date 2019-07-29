package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shift refers to a single shift assigned to a day with the specified hours
 * This class has the following functionality:
 * 		- assigns manager to shift for access used in getRosterForDay
 * 		- formats manager name to "FamilyName, Givenname" as per requirements
 * 		- adds staff member to List<StaffWorker> to allow for access to all StaffWorkers assigned to a shift
 * @author Martin Tiangco
 *
 */

public class Shift implements Comparable<Shift> {
	//fields
	private String _dayOfWeek;
	private String _startTime;
	private String _endTime;
	private int _minimumWorkers;
	private String _manager;
	private List<StaffWorker> _staffWorkerList = new ArrayList<>();
	private List<String> _staffWorkerListString = new ArrayList<>();
	
	/**
	 * Creates a Shift object based on the parameters
	 * @param dayOfWeek
	 * @param startTime
	 * @param endTime
	 * @param minimumWorkers	- this is converted into integers
	 */
	public Shift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		_dayOfWeek = dayOfWeek;
		_startTime = startTime;
		_endTime = endTime;
		_minimumWorkers = Integer.parseInt(minimumWorkers);
	}
	
	public void assignManager(String name) {
		_manager = name;
	}
	
	/**
	 * Adds staff member to list of staff in both List<StaffWorker> and List<String> formats
	 * @param name of StaffWorker
	 */
	public void addStaffMemberToList(StaffWorker name) {
		_staffWorkerList.add(name);
		Collections.sort(_staffWorkerList);
		_staffWorkerListString.clear();          //clears list to remove duplication 
		for (StaffWorker s : _staffWorkerList) { //convert to String
			_staffWorkerListString.add(s.getFullName());
		}
	}
	
	public int getMinimumWorkers() {
		return _minimumWorkers;
	}
	
	public String getManager() {
		return _manager;
	}
	
	/**
	 * Formats the manager's name in "FamilyName, Givenname" as per requirements
	 * @return formatted string of manager name
	 */
	public String getManagerFormatted() {
		String FamilyName = _manager.substring(_manager.indexOf(" ") + 1);
		String GivenName = _manager.substring(0, _manager.indexOf(" "));
		return FamilyName + ", " + GivenName;
	}

	/**
	 * Compares "this" shift and other shift (provided in parameter) in chronological order
	 */
	@Override
	public int compareTo(Shift other) {
		int thisOrder = 0;
		int otherOrder = 0;
		int compare = 0;
		for (DayOfWeek day : DayOfWeek.values()) {									//compare order of days using enum
			if (this._dayOfWeek.equals(day.toString())) {
				thisOrder = day.getOrder(); 										//gets order of "this" day
			}
			if (other._dayOfWeek.equals(day.toString())) {
				otherOrder = day.getOrder(); 										//gets order of "other" day
			}
		}
		if (thisOrder == otherOrder) {												//compare orders
			DateTime datetime1 = new DateTime(this._startTime);
			DateTime datetime2 = new DateTime(other._startTime);
			compare = datetime1.compareStart(datetime2);							//compare start time
		} else if (thisOrder < otherOrder) {
			compare = -1; 
		} 
		else {
			compare = 1; 
		}
		return compare;
	}
	
	/**
	 * Retrieves the list of StaffWorker in StaffWorker format
	 * @return List<StaffWorker> of StaffWorker
	 */
	public List<StaffWorker> getStaffWorkerList() {
		return _staffWorkerList;
	}
	
	/**
	 * Retrieves the lift of StaffWorker in String format
	 * @return List<String> of StaffWorker
	 */
	public List<String> getStaffWorkerListString() {
		return _staffWorkerListString;
	}
	
	public String getStartTime() {
		return _startTime;
	}

	public String getEndTime() {
		return _endTime;
	}
	
	/**
	 * Retrieves string formatted as per requirements dayOfWeek[startTime-endTime] e.g. Monday[00:00-23:59]
	 */
	@Override
	public String toString() {
		return _dayOfWeek + "[" + _startTime + "-" + _endTime + "]";
	}
}
