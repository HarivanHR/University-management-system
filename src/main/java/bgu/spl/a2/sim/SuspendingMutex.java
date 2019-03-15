package bgu.spl.a2.sim;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	
	private AtomicBoolean locked;
	private Computer computer;
	private LinkedBlockingDeque<Promise<Computer>> promiseList;

	
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer)
	{
		this.locked = new AtomicBoolean(false);
		this.computer = computer;
		this.promiseList = new LinkedBlockingDeque<Promise<Computer>>();
	}
	
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down()
	{
		Promise<Computer> promise = new Promise<>();
			if (this.locked.getAndSet(true)) 
			{
				promiseList.offer(promise);
				return promise;
			}
			else {
				promise.resolve(computer);
				return promise;
			}
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up()
	{
		
		if(promiseList.size() != 0)
		{
			promiseList.poll().resolve(computer);
		}
		else
		{
			this.locked.set(false);
		}
	}
	
	/**
	 * 
	 * @return the computer property
	 */
	
	public Computer getComputer() 
	{
		return this.computer;
	}
	
	/**
	 * 
	 * @return if the computer is locked
	 */
	
	public AtomicBoolean getLocked() 
	{
		return this.locked;
	}
	
	/**
	 * 
	 * @return the promise list
	 */
	public LinkedBlockingDeque<Promise<Computer>> getPromiseList()
	{
		return this.promiseList;
	}
}
