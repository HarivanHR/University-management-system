package bgu.spl.a2.sim.actions;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
 * 
 * This class represents an action that opens more places to register in a course.
 *
 */

public class OpenNewPlaceInACourse extends Action<Object>
{
	private int additionalSpaces;
	
	
	/**
	 * 
	 * @param additionalSpaces
	 * 			the amount of spaces we want to add.
	 * @param initialActorID
	 * 			the course we want to add spaces to.
	 */
	
	public OpenNewPlaceInACourse(int additionalSpaces, String initialActorID)
	{
		super();
		this.setActionName("Add Spaces");
		this.additionalSpaces = additionalSpaces;
		this.myActorID = initialActorID;
	}
	
	/**
	 * This function goes to the course PrivateState and increase the amount of available spots it has.
	 */
	
	public void start()
	{
		CoursePrivateState courseState = (CoursePrivateState)pool.getPrivateState(myActorID);
		if (courseState.getAvailableSpots() != -1) {
			courseState.setAvailableSpots(courseState.getAvailableSpots() + additionalSpaces);
			complete(true);
		}
		else
			complete(false);
	}
}
