package bgu.spl.a2.sim.actions;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;

import bgu.spl.a2.sim.Warehouse;

import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import bgu.spl.a2.*;


public class CheckAdministrativeObligations extends Action<Object>
{
	private String[] studentsList;
	private String computerType;
	private List<String> coursesList;
	private Warehouse warehouse;
	
	/**
	 * 
	 * @param studentsList
	 * 					initialize the student list
	 * @param computerType
	 * 					initialize the computer type
	 * @param coursesList
	 * 					initialize the course list
	 * @param warehouse
	 * 					initialize the warehouse
	 * @param initialActorID
	 * 					initialize the actor id
	 */
	public CheckAdministrativeObligations (String[] studentsList, String computerType, List<String> coursesList,Warehouse warehouse, String initialActorID) {
		this.setActionName("Administrative Check");
		this.studentsList = studentsList;
		this.computerType = computerType;
		this.coursesList = coursesList;
		this.warehouse = warehouse;
		this.myActorID = initialActorID;
	}
	
	/**
	 * checks if each student in the list passed each course in the courseList
	 */
	
	 @SuppressWarnings("unchecked")
	public void start()
	 {
		Computer myComputer = getComputer(warehouse.getComputers(), computerType);
		Promise<Computer> myPromise = myComputer.getSuspendingMutex().down();
		restOfAction = () ->{
			for(int i = 0; i<studentsList.length ; i++)
			{
				StudentPrivateState currentStudentState = (StudentPrivateState)pool.getPrivateState(studentsList[i]);
				boolean created = false;
				HashMap<String,Integer> cloneGrades = null;
				while (!created) {
					try {
						cloneGrades =(HashMap<String, Integer>) currentStudentState.getGrades().clone();
						created = true;
					}
					catch (ConcurrentModificationException e) {}
				}
				long sign = myComputer.checkAndSign(coursesList,cloneGrades);
				setSignature updateSig = new setSignature(sign, studentsList[i]);
				sendMessage(updateSig, studentsList[i] , pool.getPrivateState(studentsList[i]));
			}
			myComputer.getSuspendingMutex().up();
			complete(true);
		};
		myPromise.subscribe(restOfAction);		
	 }
	
	 private Computer getComputer(Computer[] computers, String computerType)
	 {
		 for (int i = 0; i < computers.length; i++) 
		 {
			 if (computers[i].getType().equals(computerType))
			 {
				 return computers[i];
			 }
		 }
		 return null;
	 }
}
