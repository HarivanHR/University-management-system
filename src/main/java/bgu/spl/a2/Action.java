package bgu.spl.a2;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> 
{

	protected Promise<R> promise = new Promise<>();
	protected AtomicInteger howManyReady = new AtomicInteger(0);
	protected List<Action<?>> IDependOn = new LinkedList<>(); //holds the results of all those who i need answer from
	protected String actionName;
	protected String myActorID;
	protected callback restOfAction = null; // will be initialized in the start function
	protected callback backToQueue = null; //this is the callback that will put the action back to the queue
	protected ActorThreadPool pool;
	
	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
//    the handle() will check if the restOfAction is null.
//    if it is, it means we handle the action for the first time.
//    we will run the Action, starting by updating the field "restOfAction" to be
//    what needs to be done after we get results from the rest of the actions we need.
//	  NOTE: in restOfAction we call to the complete() function, in order to reslove ourself.
//    After that we use the sendMessage() to enque all the Actions we need to complete this
//    action. NOTE: sendmessage() will add the new actions to the IDependOn list on its own,
//    and will also subscribe a check if we need to return to enque ourself back to the actor.
    
    
    
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) 
   {
	   this.pool = pool;
	   if (restOfAction == null)
	   {
		   pool.stateMap.get(myActorID).addRecord(this.getActionName());
		   this.start();
	   }
	   else
	   {
		   restOfAction.call();
	   }	   
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) 
    {
    	this.backToQueue = () -> {
			this.howManyReady.decrementAndGet();
	    	if (this.howManyReady.get()==0)
	    	{
	    		pool.submit(this, myActorID, pool.getPrivateState(myActorID));
	    	}
		};
    	for ( Action<?> action : actions)
    		action.promise.subscribe(backToQueue);
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result)
    {
    	promise.resolve(result);
    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult()
    {
    	return this.promise;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState)
	{
		howManyReady.incrementAndGet();
		IDependOn.add(action);
		pool.submit(action, actorId, actorState);
		return action.promise;
	}
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName)
	{
        this.actionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName()
	{
		return actionName;
	}
	
	/**
	 * 
	 * @return myActorID
	 */
	
	public String getMyActorID() {
		return this.myActorID;
	}
	
	
}