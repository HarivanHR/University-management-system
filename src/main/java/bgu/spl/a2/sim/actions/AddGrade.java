package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * 
 * this class represents an action that add a course and grade to a student's grades sheet.
 *
 */

public class AddGrade extends Action<Object> 
{
	private Integer grade;
	private String courseName;
	
	/**
	 * 
	 * @param grade
	 * 			grade is the grade to be added for the recieved course.
	 * @param courseName
	 * 			courseName is the name of the course which we want to add to a student's grades sheet
	 * 
	 * @param initialActorID
	 * 			the Student's actor's ID
	 */
	
	public AddGrade(Integer grade, String courseName, String initialActorID)
	{
		super();
		this.setActionName("Add Grade");
		this.grade = grade;
		this.courseName = courseName;
		this.myActorID = initialActorID;

	}
	
	/**
	 * start puts the course and the grade in the grades sheet of a student.
	 */
	public void start()
	{	
		((StudentPrivateState)pool.getPrivateState(myActorID)).getGrades().put(courseName, grade);
		this.complete(true);		
	}
}
