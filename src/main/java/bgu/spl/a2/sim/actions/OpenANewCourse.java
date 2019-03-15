package bgu.spl.a2.sim.actions;



import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * 
 * This class represents an action to open a new course.
 *
 */

public class OpenANewCourse extends Action<Object> {
	
	private String courseName;
	private List<String> prequisites;
	private Integer availableSpots;
	
	/**
	 * 
	 * @param courseName
	 * 			the course we want to open.
	 * @param prequisites
	 * 			list of courses that are required in order to register to this course.
	 * @param availableSpots
	 * 			the amount of available spots for this course
	 * @param initialActorID
	 * 			the department we want to open the course in.
	 */
	
	public OpenANewCourse(String courseName, List<String> prequisites, Integer availableSpots, String initialActorID)
	{
		super();
		this.setActionName("Open Course");
		this.courseName = courseName;
		this.prequisites = prequisites;
		this.availableSpots = availableSpots;
		this.myActorID = initialActorID;
	}
	
	/**
	 * this function opens the new course.
	 * It starts by check if the course already exists, so we won't open the same course twice.
	 * First, we create a PrivateState for the course, and set it's parameters (Available spots, prequisites, etc..).
	 * Afterwards, we create an actor for the new course.
	 * Lastly, we add the course to the department's courses list.
	 * 
	 */
	
	public void start()
	{
		if(!pool.getActors().containsKey(courseName))
		{
			CoursePrivateState courseState = new CoursePrivateState();
			courseState.setAvailableSpots(availableSpots);
			courseState.setPrequisites(prequisites);
			pool.getQueuesMap().put(courseName, new ConcurrentLinkedQueue<Action<?>>());
			pool.getActors().put(courseName, courseState);
			pool.getLockedList().put(courseName, new AtomicBoolean(false));
			((DepartmentPrivateState)pool.getPrivateState(myActorID)).addCourse(courseName);			
			this.complete(true);
		}
		else
		{
			this.complete(false);
		}
	}
}
