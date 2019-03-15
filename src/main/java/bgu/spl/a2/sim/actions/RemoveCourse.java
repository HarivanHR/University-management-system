package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;


public class RemoveCourse extends Action<Object>
{	

	/**
	 * 
	 * @param initialActorID
	 * 			the course we want to remove
	 */
	
	public RemoveCourse(String initialActorID)
	{
		super();
		this.setActionName("Remove Course");
		this.myActorID = initialActorID;

	}
	
	/**
	 * This action sets the properties of the course that receives it to the properties of a closed course 
	 * (i.e no available spots, removes all the students, etc..)
	 */
	
	public void start() 
	{
		CoursePrivateState courseState = (CoursePrivateState)pool.getPrivateState(myActorID);
		courseState.setAvailableSpots(-1);
		courseState.setRegistered(0);
		LinkedList<String> regStudents = (LinkedList<String>)courseState.getRegStudents();
		if (regStudents.size()>0) {
			while (courseState.getRegStudents().size()>0)
			{
				String studentName = courseState.getRegStudents().get(0);
				RemoveGrade removeGrade = new RemoveGrade(myActorID, studentName);
				this.sendMessage(removeGrade, studentName, pool.getPrivateState(studentName));;
				regStudents.removeFirst();
			}
			this.restOfAction = () -> {
				if (!this.getResult().isResolved())
					complete(true);
			};
			then(IDependOn, backToQueue);
		}
		else {
			if (!this.getResult().isResolved())
				this.complete(true);
		}
	}
}
