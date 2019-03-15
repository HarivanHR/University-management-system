package bgu.spl.a2.sim.privateStates;

import java.util.HashMap;

import bgu.spl.a2.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() 
	{
		super();
		grades = new HashMap<String, Integer>();
	}

	/**
	 * 
	 * @return the student's grades sheet
	 */
	public HashMap<String, Integer> getGrades() 
	{
		return grades;
	}

	/**
	 * 
	 * @return the signature
	 */
	public long getSignature()
	{
		return signature;
	}
	
	/**
	 * 
	 * @param sign
	 * 			update the signature
	 */
	public void setSignature(long sign) {
		this.signature = sign;
	}
}
