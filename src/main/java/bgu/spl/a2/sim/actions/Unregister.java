package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class Unregister extends Action<Object>
{
	private String studentName;
	
	/**
	 * 
	 * @param studentName
	 * 		the student we want to unregister from the course.
	 * @param initialActorID
	 * 		the course we want to unregister from.
	 */
	
	public Unregister(String studentName, String initialActorID)
	{
		super();
		this.setActionName("Unregister");
		this.studentName = studentName;
		this.myActorID = initialActorID;
	}
	
	/**
	 * Unregisters a student from a course.
	 */
	
	public void start()
	{
		CoursePrivateState courseState = (CoursePrivateState)pool.getPrivateState(myActorID);
		if (courseState.getRegStudents().contains(studentName))
		{
			courseState.getRegStudents().remove(studentName);			
			courseState.setAvailableSpots(courseState.getAvailableSpots() + 1);
			courseState.setRegistered(courseState.getRegistered() - 1);
			RemoveGrade removeGrade = new RemoveGrade(myActorID, studentName);
			this.sendMessage(removeGrade, studentName, pool.getPrivateState(studentName));
			this.restOfAction = () ->{
				this.complete(true);
			};
			then(IDependOn, backToQueue);
		}
		else
		{
			this.complete(true);
		}			
	}
}
