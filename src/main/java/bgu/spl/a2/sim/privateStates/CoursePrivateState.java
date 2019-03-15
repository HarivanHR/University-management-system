package bgu.spl.a2.sim.privateStates;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() 
	{	
		super();
		regStudents = new LinkedList<String>();
		prequisites = new LinkedList<String>();
		this.registered = 0;
	}

	/**
	 * 
	 * @return the number of available spots in the course
	 */
	
	public Integer getAvailableSpots() {
		return availableSpots;
	}

	/**
	 * 
	 * @return return the number of students registered to the course
	 */
	
	public Integer getRegistered() {
		return registered;
	}

	/**
	 * 
	 * @return list of students that are registered to the course
	 */
	public List<String> getRegStudents() {
		return regStudents;
	}

	/**
	 * 
	 * @return list of the courses a student need to be registered to in order to register this course
	 */
	public List<String> getPrequisites() {
		return prequisites;
	}
	
	/**
	 * 
	 * @param availableSpots
	 * 			update the available spots
	 */
	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots = availableSpots;
	}
	
	/**
	 * 
	 * @param prequisites
	 * 		update the prerequisites
	 */
	public void setPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
	}
	
	/**
	 * 
	 * @param num
	 * 		update the number of students that are registered to this course
	 */
	public void setRegistered(Integer num)
	{
		this.registered = num;
	}
}
