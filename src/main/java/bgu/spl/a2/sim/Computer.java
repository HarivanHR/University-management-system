package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;



public class Computer
{

	String computerType;
	long failSig;
	long successSig;
	protected SuspendingMutex mySuspendingMutex;
	//private String currentDepartment=null;
	
	public Computer(String computerType) 
	{
		this.computerType = computerType;
		this.mySuspendingMutex = new SuspendingMutex(this);
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades)
	{
		for(int i = 0 ; i<courses.size() ; i++) {
			String currentCourse = courses.get(i);
			if (!coursesGrades.containsKey(currentCourse)) {
				return failSig;
			}
			else {
				if(coursesGrades.get(currentCourse)<56) {
					return failSig;
				}
			}
		}
		return successSig;
	}
	
	/**
	 * 
	 * @param sig set the success signature
	 */
	
	public void setSuccessSig(long sig) {
		this.successSig = sig;
	}
	
	/**
	 * 
	 * @param sig set the fail signature
	 */
	
	public void setFailSig(long sig) {
		this.failSig = sig;
	}
	
	/**
	 * 
	 * @return the computer type
	 */
	
	public String getType() {
		return this.computerType;
	}
	
	/**
	 * 
	 * @return the suspendingMutex
	 */
	public SuspendingMutex getSuspendingMutex() 
	{
		return this.mySuspendingMutex;
	}
}




