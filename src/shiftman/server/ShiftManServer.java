package shiftman.server;

import java.util.ArrayList;
import java.util.List;

/**
 * ShiftManServer implements all of the methods of the interface ShiftMan
 * This Class is mainly used for checking for checked exceptions.
 * ShiftManServer's methods pass the relevant parameters to the Roster class to be handled.
 * @author Martin Tiangco
 *
 */

public class ShiftManServer implements shiftman.server.ShiftMan {
	//fields
	private Roster _roster; 
	private static boolean _rosterCreated = false; 	//When newRoster is called and a valid shop name is given, this will be true.
	private static List<String> _errorCheck = new ArrayList<>(); //used in List<String> methods to return an error if new roster hasn't been called
	
	static {
		_errorCheck.add("ERROR: no roster has been created");
	}
		
	public String newRoster(String shopName) {
		try {
		_roster = new Roster(shopName);
		_rosterCreated = true;
		return "";
		} catch (RosterException e) {			//if user inputs an empty or null shop name
			_rosterCreated = false;
			return "%ERROR% --- Please provide a non-empty shop name.";
		}
	}
	
	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {
		if (_rosterCreated == false) {
			return "%ERROR% --- Please create a new roster first.";
		} else {
			try {
				_roster.setWorkingHours(dayOfWeek, startTime, endTime);
				return "";
			} catch (RosterException e) {
				return "%ERROR% --- Please provide a valid name of day";
			} catch (TimeException e) {	//valid means between 00:00-23:59, if startTime != endTime, if startTime is before endTime
				return "%ERROR% --- Please provide a valid start and/or end time.";
			}
		}	
	}
	
	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		if (_rosterCreated == true) {
			try {
				_roster.addShift(dayOfWeek, startTime, endTime, minimumWorkers);
				return "";
			} catch (RosterException e) {
				return "%ERROR% --- Please provide a valid name of day";
			} catch (TimeException e) { //valid means between 00:00-23:59, if startTime != endTime, if startTime is before endTime
				return "%ERROR% --- Please provide a valid start and/or end time.";
			}
		} else {
			return "%ERROR% --- Please create a new roster first.";
		}	
	}
	
	public String registerStaff(String givenname, String familyName) {
		if (_rosterCreated == true) {
			try {
				_roster.registerStaff(givenname, familyName);
				return "";
			} catch (StaffException e) { //if "null" or empty names were entered as parameters
				return "%ERROR% --- Please provide a non-empty GivenName and/or FamilyName.";
			} catch (DuplicateStaffException e) { //if there is a matching name found in the list (case insensitive)
				return "%ERROR% --- Staff already registered.";
			}
		} else {
			return "%ERROR% --- Please create a new roster first.";
		}
	}
	
	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, 
			String familyName, boolean isManager) {
		if (_rosterCreated == true) {
			try {
				_roster.assignStaff(dayOfWeek, startTime, endTime, givenName, familyName, isManager);
				return "";
			} catch (ShiftException e){ //must addShift first before assignShift
				return "%ERROR% --- Specified shift has not been previously set.";
			}
		} else {
			return "%ERROR% --- Please create a new roster first.";
		}
	}
	
	public List<String> getRegisteredStaff() {
		if (_rosterCreated == true) {
			return _roster.getRegisteredStaff();
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> getUnassignedStaff() {
		if (_rosterCreated == true) {
			return _roster.getUnassignedStaff();
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> shiftsWithoutManagers() {
		if (_rosterCreated == true) {
			return _roster.shiftsWithoutManagers();
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> understaffedShifts() {
		if (_rosterCreated == true) {
			return _roster.getUnderOrOverStaffedShifts("understaffed");
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> overstaffedShifts() {
		if (_rosterCreated == true) {
			return _roster.getUnderOrOverStaffedShifts("overstaffed");
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> getRosterForDay(String dayOfWeek) {
		if (_rosterCreated == true) {
			return _roster.getRosterForDay(dayOfWeek);
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> getRosterForWorker(String workerName) {
		if (_rosterCreated == true) {
			return _roster.getRosterForWorker(workerName);
		} else {
			return _errorCheck;
		}
	}
	
	public List<String> getShiftsManagedBy(String managerName) {
		if (_rosterCreated == true) {
			return _roster.getShiftsManagedBy(managerName);
		} else {
			return _errorCheck;
		}
	}
	
	public String reportRosterIssues() {
		return "";
	}
	
	public String displayRoster() {
		return "";
	}
}

