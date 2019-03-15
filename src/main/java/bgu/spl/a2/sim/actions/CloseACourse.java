package bgu.spl.a2.sim.actions;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * 
 * This class represents an action to close a course.
 *
 */

public class CloseACourse extends Action<Object>
{
	String courseName;
	
	/**
	 * 
	 * @param courseName
	 * 		the course to be closed.
	 * @param initialActorID
	 * 		the department which the course is a part of.
	 *
	 */

	public CloseACourse(String courseName, String initialActorID)
	{
		super();
		this.setActionName("Close Course");
		this.courseName = courseName;
		this.myActorID = initialActorID;
	}
	
	/**
	 * this action closes a course
	 */
	
	public void start()
	{
		
		DepartmentPrivateState departmentState = (DepartmentPrivateState)pool.getPrivateState(myActorID);
		departmentState.getCourseList().remove(courseName);
		RemoveCourse removeCourse = new RemoveCourse(courseName);
		sendMessage(removeCourse, courseName, pool.getPrivateState(courseName));
		this.restOfAction = () -> {
			complete(true);
		};
		then(IDependOn, backToQueue);
	}
}
