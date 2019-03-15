package bgu.spl.a2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 * @param <Queue>
 */
public class ActorThreadPool 
{
		
	protected LinkedList<Worker> workersList;
	protected LinkedList<Thread> threadsList;
	protected ConcurrentHashMap<String, ConcurrentLinkedQueue<Action<?>>> queueMap;
	protected ConcurrentHashMap<String, PrivateState> stateMap;
	protected ConcurrentHashMap<String, AtomicBoolean> lockedList;
	protected VersionMonitor vm;
	
	
	
	
	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) 
	{
		this.workersList = new LinkedList<>();
		for(int i = 0; i < nthreads; i++)
		{
			workersList.add(new Worker(this));
		}
		this.threadsList = new LinkedList<>();
		this.queueMap = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Action<?>>>();
		this.stateMap = new ConcurrentHashMap<String, PrivateState>();
		this.lockedList = new ConcurrentHashMap<String, AtomicBoolean>();
		this.vm = new VersionMonitor();
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState)
	{
		if (!queueMap.containsKey(actorId))
		{
			queueMap.put(actorId, new ConcurrentLinkedQueue<Action<?>>());
			stateMap.put(actorId, actorState);
			lockedList.put(actorId, new AtomicBoolean(false));
		}
			queueMap.get(actorId).add(action);
			this.vm.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		for(int i = 0; i<threadsList.size() ; i++)
		{
			threadsList.get(i).interrupt();
		}
		for(int i = 0 ; i<threadsList.size() ; i++)
			while (threadsList.get(i).isAlive()) {}
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(int i=0 ; i<workersList.size() ; i++)
		{
			Thread t = new Thread(workersList.get(i));
			threadsList.add(t);
			t.start();
		}
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId)
	{
		return this.stateMap.get(actorId);
	}
	
	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors()
	{
		return this.stateMap;
	}
	
	/**
	 * 
	 * @return the queueMap
	 */
	public Map<String,ConcurrentLinkedQueue<Action<?>>> getQueuesMap()
	{
		return this.queueMap;
	}
	/**
	 * 
	 * @return the lockMap
	 */
	public ConcurrentHashMap<String, AtomicBoolean> getLockedList() {
		return lockedList;
	}
	

	
	
}
