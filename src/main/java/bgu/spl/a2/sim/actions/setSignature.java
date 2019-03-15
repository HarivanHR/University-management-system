package bgu.spl.a2.sim.actions;
import bgu.spl.a2.*;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class setSignature extends Action<Object> {

	private long sign;
	
	/**
	 * 
	 * @param sign
	 * 		the signature we want to set to the student.
	 * @param initialActorID
	 * 		the student we want to set the signature to.
	 */
	
	public setSignature(long sign, String initialActorID) {
		super();
		this.setActionName("Set Signature");
		this.sign = sign;
		this.myActorID = initialActorID;
	}
	
	/**
	 * Updates the student's signature
	 */
	
	public void start() {
		((StudentPrivateState)pool.getPrivateState(myActorID)).setSignature(sign);
		complete(true);
	}
}
