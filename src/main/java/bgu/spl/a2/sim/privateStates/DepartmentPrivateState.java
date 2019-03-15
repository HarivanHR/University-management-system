package bgu.spl.a2.sim.privateStates;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState{
	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState() {
		super();
		courseList = new LinkedList<String>();
		studentList = new LinkedList<String>();
	}

	/**
	 * 
	 * @return the courses list
	 */
	public List<String> getCourseList() {
		return courseList;
	}

	/**
	 * 
	 * @return the students list
	 */
	public List<String> getStudentList() {
		return studentList;
	}
	
	/**
	 * 
	 * @param courseName
	 * 			add this course to the department's courses list
	 */
	public void addCourse(String courseName)
	{
		courseList.add(courseName);
	}
	
	/**
	 * 
	 * @param studentName
	 * 			add this student to the department's students list 
	 */
	public void addStudent(String studentName)
	{
		studentList.add(studentName);
	}
}
