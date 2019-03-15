package bgu.spl.a2.sim.actions;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * 
 * This class represents an action that registers a student to a department.
 *
 */

public class AddStudent extends Action<Object> {

	private String studentName;
	
	/**
	 * @param initialActorID
	 * 			The Department we register the student to.
	 * @param studentName
	 * 			The student we want to register to the department.
	 */
	
	public AddStudent(String studentName, String initialActorID)
	{
		super();
		this.setActionName("Add Student");
		this.studentName = studentName;
		this.myActorID = initialActorID;
	}
	
	/**
	 * add the student to the department registered student list, and also creates an actor for the student (queue for actions and PrivateState).
	 */
	
	public void start() {
		((DepartmentPrivateState)pool.getPrivateState(myActorID)).addStudent(studentName);
		pool.getQueuesMap().put(studentName, new ConcurrentLinkedQueue<Action<?>>());
		pool.getActors().put(studentName, new StudentPrivateState());
		pool.getLockedList().put(studentName, new AtomicBoolean(false));
		this.complete(true);
	}
}
