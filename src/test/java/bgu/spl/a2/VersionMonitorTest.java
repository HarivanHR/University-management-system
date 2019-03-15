package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class VersionMonitorTest extends TestCase {

	VersionMonitor vm1;
	VersionMonitor vm2;
	VersionMonitor vm3;
	
	protected void setUp() throws Exception {
		vm1 = new VersionMonitor();
		vm2 = new VersionMonitor();
		vm3 = new VersionMonitor();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetVersion() {
		assertEquals(0, vm1.getVersion());
	}

	public void testInc() {
		vm2.inc();
		assertEquals(1, vm2.getVersion());
	}

	public void testAwait() {
		final boolean[] test = {false};
		Runnable r = new Runnable() {
			public void run() {
				int oldVersion = vm3.getVersion();
				try {
					vm3.await(3);
				}
				catch(Exception e) {
					Assert.fail();
				}
				try {
					Thread.sleep(3000);
				}
				catch(Exception e) {}
				try {
					vm3.await(vm3.getVersion());
				}
				catch(Exception e) {
					Assert.fail();
				}
				assertEquals(oldVersion+1, vm3.getVersion());
				test[0] = true;
			}
		};
		Thread t = new Thread(r);
		t.start();
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {}
		if (t.getState() == Thread.State.WAITING) {
			Assert.fail();
		}
		try {
			Thread.sleep(4000);
		}
		catch(Exception e) {}
		assertEquals(Thread.State.WAITING, t.getState());
		vm3.inc();
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {}
		if(test[0] == false)
		{
			Assert.fail();
		}
	}
}
