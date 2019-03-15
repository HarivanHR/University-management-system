package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveGrade extends Action<Object>
{
	private String courseName;
	
	/**
	 * 
	 * @param courseName
	 * 			the course we want to remove from the grades sheet.
	 * @param initialActorID
	 * 			the student we want to remove the course from his grades sheet.
	 */
	public RemoveGrade(String courseName, String initialActorID)
	{
		super();
		this.setActionName("Remove Grade");
		this.courseName = courseName;
		this.myActorID = initialActorID;

	}
	
	/**
	 * This action removes the course and it's grade from the student's grades sheet 	
	 */
	
	public void start()
	{
		if(((StudentPrivateState)pool.getPrivateState(myActorID)).getGrades().containsKey(courseName)){
			((StudentPrivateState)pool.getPrivateState(myActorID)).getGrades().remove(courseName);
		}
		this.complete(true);
	}
}
