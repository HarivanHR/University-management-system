package bgu.spl.a2.sim;


/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse 
{
	Computer[] computers;
	
	/**
	 * 
	 * @param amount 
	 * 				initialize the number of the computers
	 */
	
	public Warehouse(int amount)
	{
		computers = new Computer[amount];
	}
	
	/**
	 * 
	 * @return the computers in the warehouse
	 */
	public Computer[] getComputers() {
		return computers;
	}
	
	
}
