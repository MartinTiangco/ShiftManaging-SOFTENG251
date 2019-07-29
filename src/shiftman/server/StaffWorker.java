package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StaffWorker refers to an individual staff employee.
 * This class has the following functionality:
 * 		-formats the name like in the requirements
 * 		-add staff to List<Shift> that they are assigned to
 * There are three List<Shift>:
 * 		- every shift the staff is assigned to
 * 		- shifts the staff is assigned to as either worker or manager
 * @author Martin Tiangco
 *
 */
public class StaffWorker implements Comparable<StaffWorker> {
	//fields
	private final String _givenname;
	private final String _familyName;
	private List<Shift> _assignedShift = new ArrayList<Shift>();
	private List<Shift> _assignedShiftWorker = new ArrayList<Shift>();
	private List<Shift> _assignedShiftManager = new ArrayList<Shift>();
	
	/**
	 * Creates a StaffWorker based on the parameters given
	 * @param givenname
	 * @param familyName
	 */
	public StaffWorker(String givenname, String familyName) {
		_givenname = givenname;
		_familyName = familyName;
	}
	
	/**
	 * Assigns an existing shift to either a worker or a manager, adds to list of staff for that shift
	 * then adds to either list of workers, or list of managers
	 * @param existingShift
	 * @param IsManager	- true if manager, false if worker
	 */
	public void assignShift(Shift existingShift, boolean IsManager) {
		_assignedShift.add(existingShift);
		if (IsManager == true) {
			existingShift.assignManager(getFullName());
			_assignedShiftManager.add(existingShift);      //adds to manager shift list
			Collections.sort(_assignedShiftManager);	   
		} else {
			_assignedShiftWorker.add(existingShift);       //adds to worker shift list
			Collections.sort(_assignedShiftWorker);		   
		}
	}
	
	public String getFullName() {
		return _givenname + " " + _familyName;
	}

	public String getGivenName() {
		return _givenname;
	}
	
	public String getFamilyName() {
		return _familyName;
	}
	
	/**
	 * Formats the staff member's string to "FamilyName, Givenname" as per requirements
	 * @return formatted string
	 */
	public String formatName() {	
		return _familyName + ", " + _givenname;
	}
	
	/**
	 * Retrieves all assigned shifts in the list
	 * @return list of shifts of the staff
	 */
	public List<Shift> getShift() {
		return _assignedShift;
	}
	
	/**
	 * Retrieves either shifts of worker or shifts of manager depending on boolean isManager parameter
	 * @param isManager	- true if manager/false if worker
	 * @return shift of Manager OR shift of Worker
	 */
	public List<Shift> getShift(boolean isManager) {
		if (isManager == true) {
			return _assignedShiftManager;
		} else {
			return _assignedShiftWorker;
		}
	}

	/**
	 * Sorts in alphabetical order of FamilyName, and if FamilyName are equal, sorts by Givenname
	 */
	@Override
	public int compareTo(StaffWorker other) {
		int compare = this.getFamilyName().compareTo(other.getFamilyName());
		if (compare == 0) {			//familyName are the same
			return this.getGivenName().compareTo(other.getGivenName());
		}
		return compare;
	}
}
