package shiftman.server;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Roster is passed all of the methods from ShiftManServer and is handled here. The bulk of the functionality to do
 * with roster handling is stored in this class.
 * This class has the following functionality:
 * 		- can create a new roster for the working week consisting of 7 days (providing the name is valid)
 * 				- stores all 7 days in a list
 * 				- sets working hours for each day
 * 				- adds shifts to each day
 * 		- has a List<StaffWorker> which is a list of employee names that have been registered
 *   		 	- assigns staff to previously made shifts
 * 		- throws checked exceptions in several methods for ShiftManServer class to catch
 *		- retrieves the following
 *				- registered staff
 * 					- and those unassigned to shifts
 * 				- understaffed or overstaffed shifts
 * 				- shifts without manager
 * 				- rosters 
 * 					- for a specific day
 * 					- for worker (assumed that worker is NOT the manager)
 * 					- for manager
 * @author Martin Tiangco
 *
 */
public class Roster {
	//fields
	private String _shopName;
	private List<Day> _day = new ArrayList<>();
	private List<StaffWorker> _staffList = new ArrayList<>();

	/**
	 * Creates a roster if a valid name is given, then creates a list of Day objects of the 7 days of the week
	 * @param name (must be valid)
	 * @throws RosterException (if invalid name has been given)
	 */
	public Roster(String name) throws RosterException {
		if (name == null) {
			throw new RosterException("RosterException: Please provide a non-empty shop name.");
		} else if (name.isEmpty()) {
			throw new RosterException("RosterException: Please provide a non-empty shop name.");
		} else {
			_shopName = name;
			_day.add(new Day("Monday"));
			_day.add(new Day("Tuesday"));
			_day.add(new Day("Wednesday"));
			_day.add(new Day("Thursday"));
			_day.add(new Day("Friday"));
			_day.add(new Day("Saturday"));
			_day.add(new Day("Sunday"));
		}
	}
	
	/**
	 * Sets the working hours of the specified day 
	 * @param dayOfWeek in string format matching one of the 7 days (case sensitive)
	 * @param startTime in string format hh:mm
	 * @param endTime in string format hh:mm
	 * @throws RosterException if name of day is invalid
	 * @throws TimeException if time is not valid
	 * 			- time must be in the range 00:00-23:59
	 * 			- startTime != endTime
	 * 			- startTime is before endTime
	 */
	public void setWorkingHours(String dayOfWeek, String startTime, String endTime) throws RosterException, TimeException {
		DateTime datetime = new DateTime(startTime, endTime);	//check if time is valid
		if (datetime.checkIfValidWorkingHours()) {
			for (Day d : _day) {								//iterate through ArrayList<Day> and find the dayOfWeek
				if (d.toString().equals(dayOfWeek)) {
					d.setStartAndEndTimes(startTime, endTime);
					return;
				}
			}
			//day is either misspelled or invalid
			throw new RosterException("RosterException: Please write a valid name of day");
		} else {
			throw new TimeException("TimeException: Please provide a valid start and/or end time.");
		}
	}
	
	/**
	 * Adds a shift to a specified day with startTime and endTime
	 * @param dayOfWeek	must match one of the 7 days exactly
	 * @param startTime in format hh:mm
	 * @param endTime in format hh:mm
	 * @param minimumWorkers
	 * @throws RosterException if provided day is misspelled or invalid
	 * @throws TimeException if provided time is not valid (see setWorkingHours documentation for more information)
	 */
	public void addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) throws RosterException, TimeException {
		DateTime datetime = new DateTime(startTime, endTime);					//check if time is valid
		if (datetime.checkIfValidWorkingHours()) {
			for (Day d : _day) {												//iterate through ArrayList to find the dayOfWeek
				if (d.toString().equals(dayOfWeek)) {
					d.addShift(dayOfWeek, startTime, endTime, minimumWorkers);	//adds shift to specific day
					return;
				}
			}
			throw new RosterException("RosterException: Provided dayOfWeek is not valid"); //only reachable if dayOfWeek is not in _day		
		} else {
			throw new TimeException("TimeException: Please provide a valid start and/or end time.");
		}
	}
	
	/**
	 * Registers a staff with provided name
	 * @param givenName 
	 * @param familyName
	 * @throws StaffException if Given/Family names are empty
	 * @throws DuplicateStaffException if there is a duplicate staff already in the list (case insensitive)
	 */
	public void registerStaff(String givenName, String familyName) throws StaffException, DuplicateStaffException {
		boolean foundStaffWorker = false;
		if (!(givenName.isEmpty() || familyName.isEmpty())) {
			for (StaffWorker staff : _staffList) {
				if (staff.getFullName().toLowerCase().equals((givenName + " " + familyName).toLowerCase())) {
					foundStaffWorker = true;	//true if names are equal ignoring case
				}
			}
			if (foundStaffWorker == false) {
				StaffWorker _staff = new StaffWorker(givenName, familyName);
				_staffList.add(_staff); 										//add staff to ArrayList<StaffWorker>
				Collections.sort(_staffList); 									//sorts in alphabetical order
			} else {
				throw new DuplicateStaffException("DuplicateStaffException: Staff already registered.");
			}
		} else {
			throw new StaffException("StaffException: Given/Family names are empty");
		}
	}
	
	/**
	 * Assigns a staff member to specified time 
	 * @param dayOfWeek must match one of the 7 days
	 * @param startTime in format hh:mm
	 * @param endTime in format hh:mm
	 * @param givenName 
	 * @param familyName
	 * @param isManager true if manager, false otherwise
	 * @return empty string if successful
	 * @throws ShiftException if specified shift is not found in the list (hasn't been previously created)
	 */
	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, 
			String familyName, boolean isManager) throws ShiftException {
		for (Day d : _day) {									//iterate through day ArrayList and find dayOfWeek
			if (d.toString().equals(dayOfWeek)) {
				//match startTime and endTime with existing shift
				//returns null if no shift is found
				Shift existingShift = d.getExistingShift(startTime, endTime);
				if (existingShift != null) {
					for (StaffWorker staff : _staffList) {		//iterate through staff members to find wanted staff member
						if (staff.getFullName().toLowerCase().equals((givenName + " " + familyName).toLowerCase())) {
							staff.assignShift(existingShift, isManager);		//assign shift to either manager or worker
							if (isManager == false) {
								existingShift.addStaffMemberToList(staff);		//add to list of workers for that shift
							}
						}
					}
				} else {
					throw new ShiftException("ShiftException: Specified shift is not found in list of shifts.");
				}
			}
		}
		return "";
	}
	
	/**
	 * Retrieves a list of registered staff in String format
	 * @return list of registered staff
	 */
	public List<String> getRegisteredStaff() {	//loop through list<StaffWorker> and convert StaffWorkers to list<String>
		List<String> _staffNamesString = new ArrayList<>();
		for (StaffWorker s : _staffList) {
			_staffNamesString.add(s.getFullName()); 
		}
		return _staffNamesString;
	}
	
	/**
	 * Retrieves a list of unassigned staff in String format
	 * @return list of unassigned staff
	 */
	public List<String> getUnassignedStaff() {
		List<String> unassignedStaff = new ArrayList<String>();
		for (StaffWorker s : _staffList) {			    //iterate through all StaffWorkers and find those without shifts
			if (s.getShift().isEmpty()) {
				unassignedStaff.add(s.getFullName());	//add those to the unassignedStaff ArrayList
			}
		}
		return unassignedStaff;
	}
	
	/**
	 * Retrieves the roster for a specified worker (NOT manager)
	 * @param workerName in format "Givenname FamilyName"
	 * @return list of shifts of a worker in String format
	 */
	public List<String> getRosterForWorker(String workerName) {
		List<String> shiftListString = new ArrayList<>();			//create new List<String>
		for (StaffWorker s : _staffList) {							//iterate through StaffWorkers ArrayList
			if (s.getFullName().toLowerCase().equals(workerName.toLowerCase())) {
				List<Shift> shiftList = s.getShift(false);	//false refers to boolean isManager
				if (!(shiftList.isEmpty())) {
					Collections.sort(shiftList);					//sorts List<Shift> in chronological order
					shiftListString.add(s.formatName());	//1st entry is the worker name in format "FamilyName, GivenName"
					for (Shift shift : shiftList) { 				//convert List<Shift> to List<String>
						shiftListString.add(shift.toString());
					}
				}
			}
		}
		return shiftListString;		//will return an empty list if worker is not found or if there are no shifts
	}
	
	/**
	 * Retrieves the roster for a specified day
	 * @param dayOfWeek must match one of the 7 days
	 * @return formatted string with the following contents
	 * 			- first entry is the shop's name
	 * 			- second entry is the working hours of the day
	 * 			- next entries are the shifts with manager name, list of workers and the time of the shifts
	 * 				- with the format dayOfWeek[startTime-endTime] Manager: [Worker List]
	 * If there is no shifts on that day, return an empty list 
	 */
	public List<String> getRosterForDay(String dayOfWeek) {
		List<String> listShiftString = new ArrayList<>();
		List<Shift> listShift = new ArrayList<>();
		for (Day d : _day) {
			if (d.toString().equals(dayOfWeek)) {						//find dayOfWeek in the list
				listShift = d.getListOfShifts();						//retrieves List<Shift> of specified day	
				if (!(listShift.isEmpty())) {							//convert List<Shift> to List<String>
					listShiftString.add(_shopName);						//1st entry into list - shop name
					listShiftString.add(d + " " + d.getStartWorkingHours() + "-" + d.getEndWorkingHours()); //2nd entry - working hours
					for (Shift s : listShift) {
						if (s.getManager() == null) {						//no manager
							if (s.getStaffWorkerList().isEmpty()) {			//no manager and no staff workers
								listShiftString.add(s.toString() + 
										" [No manager assigned] " + "[No workers assigned]");
							} else {										//no manager and has staff workers
								
								listShiftString.add(s.toString() + " [No manager assigned] " + 
										s.getStaffWorkerListString()); 
							}
						} else {											//manager
							if (s.getStaffWorkerList().isEmpty()) {			//manager and no staff workers
								String managerFormat = s.getManagerFormatted();
								listShiftString.add(s.toString() + " Manager:" + managerFormat + " " + 
										"[No workers assigned]"); 	
							} else {										//manager and staff workers
								String managerFormat = s.getManagerFormatted();
								listShiftString.add(s.toString() + " Manager:" + managerFormat + " " + 
										s.getStaffWorkerListString());
							}
						}
					}
				} 
			}
		}
		return listShiftString;		//if shift list is empty, returns an empty list.
	}
	
	/**
	 * Retrieves the list of shifts that are managed by the staff employee given by parameter
	 * @param managerName 
	 * @return list of shifts managed by employee in String format
	 */
	public List<String> getShiftsManagedBy(String managerName) {
		List<String> managerShiftsString = new ArrayList<>();		//new List<String> to return later
		List<Shift> managerShifts = new ArrayList<>();
		for (StaffWorker staff : _staffList) {
			if (staff.getFullName().toLowerCase().equals(managerName.toLowerCase())) {
				managerShifts = staff.getShift(true);	//true refers to boolean IsManager
				if (!(managerShifts.isEmpty())) {
					managerShiftsString.add(staff.formatName());	//1st entry is name of manager with format "FamilyName, GivenName"
				}
			}
		}
		for (Shift s : managerShifts) {
			managerShiftsString.add(s.toString());	//loop through all shifts where staff is manager, then add to String
		}
		return managerShiftsString;
	}
	
	/**
	 * Retrieves the shifts without managers
	 * @return shifts no managers in String format
	 */
	public List<String> shiftsWithoutManagers() {
		List<String> noManagerString = new ArrayList<>();
		for (Day d : _day) {						//iterate through days and collect shifts
			List<Shift> listOfShift = d.getListOfShifts();
			for (Shift shift : listOfShift) {		//iterate through List<Shift> and find those without managers
				if (shift.getManager() == null) {
					noManagerString.add(shift.toString());	//add shift to List<String>
				}
			}
		}
		return noManagerString;
	}
	
	/**
	 * Retrieves the under or overstaffed shifts based on which of the shifts you wish to retrieve
	 * @param underOrOver - either "understaffed" or "overstaffed"
	 * @return list of shifts that are understaffed or overstaffed 
	 */
	public List<String> getUnderOrOverStaffedShifts(String underOrOver) {
		List<String> shiftListString = new ArrayList<>();
		for (Day d: _day) {
			List<Shift> listOfShift = d.getListOfShifts();
			for (Shift shift : listOfShift) {
				switch (underOrOver) {
				case "understaffed" :
					if (shift.getStaffWorkerList().size() < shift.getMinimumWorkers()) {
						shiftListString.add(shift.toString());		//add understaffed shift to List<String>
					}
					break;
				case "overstaffed" :
					if (shift.getStaffWorkerList().size() > shift.getMinimumWorkers()) {
						shiftListString.add(shift.toString());		//add overstaffed shift to List<String>
					}
					break;
				}
			}
		}
		return shiftListString;
	}
	
	public String toString() {
		return _shopName;
	}
	
}
