package bgu.spl.a2.sim.actions;


import bgu.spl.a2.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * 
 * This class represent a registration to a course by a student.
 *
 */

public class ParticipatingInCourse extends Action<Object> 
{
	
	
	private String studentName;
	private Integer grade;
	
	/**
	 * 
	 * @param studentName
	 * 			the student that wants to register
	 * @param grade
	 * 			the grade of the course for said student.
	 * @param initialActorID
	 * 			the course we want to register the student to.
	 * 
	 */
	
	public ParticipatingInCourse(String studentName, Integer grade, String initialActorID)
	{
		super();
		this.setActionName("Participate In Course");
		this.studentName = studentName;
		this.grade = grade;
		this.myActorID = initialActorID;
	}
	
	/**
	 * The function that registers the student.
	 * First, it checks that there are available spots to register in the course.
	 * Afterwards, it checks if the student has the required courses to register for this course.
	 * After the actions receives an answer(the student has the required courses or not) it continues the action
	 * by sending an action to the student's actor to add the course and the grade to his grades sheet, and updates
	 * the course PrivateState accordingly.
	 */
	
	public void start() 
	{
		CoursePrivateState courseState =(CoursePrivateState)pool.getPrivateState(myActorID);

		if(courseState.getAvailableSpots() > 0)
		{
			boolean canRegister = this.checkPrequisites(courseState);
			if (canRegister) {
				if (!((StudentPrivateState)pool.getPrivateState(studentName)).getGrades().containsKey(myActorID)) {
					courseState.setAvailableSpots(courseState.getAvailableSpots() -1);
					courseState.setRegistered(courseState.getRegistered() +1);
					courseState.getRegStudents().add(studentName);
				}
				AddGrade addGrade = new AddGrade(grade, myActorID, studentName);
				this.sendMessage(addGrade, studentName, pool.getPrivateState(studentName));
				this.restOfAction = () ->{
					this.complete(true);
				};
				then(IDependOn, backToQueue);
			}
			else
			{
				this.complete(false);
			}
		}
		else {
			this.complete(false);
		}
	}	
	
	private boolean checkPrequisites(CoursePrivateState courseState) {
		for(int i = 0; i<courseState.getPrequisites().size() ; i++)
		{
			StudentPrivateState currentStudentState = (StudentPrivateState)pool.getPrivateState(studentName);
			String currentCourse = courseState.getPrequisites().get(i);
			if(!currentStudentState.getGrades().containsKey(currentCourse))
			{
				return false;
			}
		}
		return true;
	}
}
