package bgu.spl.a2;

import java.util.Iterator;

public class Worker implements Runnable {
	
	private ActorThreadPool pool;
	protected boolean toShutDown;
	
	/**
	 * 
	 * @param pool
	 * 			the simulator's ActorThreadPool
	 */
	
	public Worker(ActorThreadPool pool)
	{
		this.pool = pool;
		this.toShutDown = false;
	}
	
	/**
	 * The event loop of the worker to take actions when there are available actions to take.
	 * Otherwise, the worker waits.
	 */
	
	public void run()
	{
		while(!toShutDown)
		{
			int oldPoolVersion = pool.vm.getVersion();
			Action<?> actionToStart = null;
			Iterator<String> iter = pool.lockedList.keySet().iterator();
			while (iter.hasNext())
			{
				toShutDown = Thread.currentThread().isInterrupted();
				String actorID = iter.next();
				if (!pool.lockedList.get(actorID).getAndSet(true) && !toShutDown) {
					if (!pool.queueMap.get(actorID).isEmpty())
					{
						actionToStart = pool.queueMap.get(actorID).poll();
						if(actionToStart !=null) {
							actionToStart.handle(pool, actorID, pool.stateMap.get(actorID));
							pool.lockedList.get(actorID).set(false);
							pool.vm.inc();
						}
					}
					else {
						pool.lockedList.get(actorID).set(false);
					}
				}	
			}
			while (pool.vm.getVersion() == oldPoolVersion && !toShutDown)
			{	
				synchronized (this) 
				{
					try 
					{
						pool.vm.await(pool.vm.getVersion());
					} 
					catch (InterruptedException e) {
						this.toShutDown = true;
					}
				}
			}
		}
	}	
}
