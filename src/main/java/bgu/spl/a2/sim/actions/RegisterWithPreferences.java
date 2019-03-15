package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class RegisterWithPreferences extends Action<Object>
{
	private List<String> preferences;
	private List<Integer> grades;
	private boolean registered;
	final int[] priority = {0};
	
	/**
	 * 
	 * @param preferences
	 * 		The list of courses the student want to register to.
	 * @param grades
	 * 		The grades of the courses the student want to register to. Each index of the grade is the same as the index of it's corresponding course.
	 * @param initialActorID
	 * 		The student's actor's ID.
	 */
	
	public RegisterWithPreferences(List<String> preferences, List<Integer> grades, String initialActorID)
	{
		super();
		this.setActionName("Register With Preferences");
		this.preferences = preferences;
		this.grades = grades;
		this.registered = false;
		this.myActorID = initialActorID;
	}
	
	/**
	 * The student try to register to a course according to his preference.
	 * The student will register to 1 course only, or none at all (if he cant register to any of the courses in the list)
	 */
	
	public void start()
	{
		
		this.restOfAction = () ->{
			this.registered = ((Boolean)IDependOn.get(priority[0]).getResult().get()).booleanValue();	
			if(registered)
			{
				this.complete(true);
			}
			else 
			{
				priority[0] ++;
				if(priority[0]<preferences.size()) 
				{
					CoursePrivateState courseStatePriority = (CoursePrivateState)pool.getPrivateState(preferences.get(priority[0]));
					ParticipatingInCourse registerToPriority = new ParticipatingInCourse(myActorID, grades.get(priority[0]), preferences.get(priority[0]));
					sendMessage(registerToPriority, preferences.get(priority[0]), courseStatePriority);	
					registerToPriority.getResult().subscribe(backToQueue);
				}
				else
				{
					this.complete(false);	
				}
			}
		};
		CoursePrivateState courseState = (CoursePrivateState)pool.getPrivateState(preferences.get(priority[0]));
		ParticipatingInCourse registerTo = new ParticipatingInCourse(myActorID, grades.get(priority[0]), preferences.get(priority[0]));
		sendMessage(registerTo, preferences.get(priority[0]), courseState);
		then(IDependOn, backToQueue);
	}

}
