package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PromiseTest extends TestCase {
	
	Promise<Boolean> promiseGet;
	Promise<Boolean> promiseIsResolved;
	Promise<Boolean> promiseResolve;
	Promise<Boolean> promiseSubscribe;
	
	protected void setUp() throws Exception {
		promiseGet = new Promise<Boolean>();
		promiseIsResolved = new Promise<Boolean>();
		promiseResolve = new Promise<Boolean>();
		promiseSubscribe = new Promise<Boolean>();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGet() {
		final Boolean[] result = {false};
		try {
			result[0] = promiseGet.get();
			Assert.fail();
		}
		catch(IllegalStateException e) {}
			promiseGet.resolve(true);
			result[0] = promiseGet.get();
			Boolean answer = true;
			assertEquals(answer, result[0]);
	}

	public void testIsResolved() {
		assertEquals(false, promiseIsResolved.isResolved());
		promiseIsResolved.resolve(true);
		assertEquals(true, promiseIsResolved.isResolved());
	}

	public void testResolve() {
		final Integer[] callbackTest = {1,1};
		callback call1 = () ->{
			callbackTest[0] = 4;
		};
		callback call2 = () ->{
			callbackTest[1] = 5;
		};
		promiseResolve.subscribe(call1);
		promiseResolve.subscribe(call2);
		try {
			promiseResolve.resolve(true);
			assertEquals(new Integer(4), callbackTest[0]);
			assertEquals(new Integer(5), callbackTest[1]);
		}
		catch(Exception e) {
			Assert.fail();
		}
		Boolean resultChangeTest = true;
		assertEquals(resultChangeTest, promiseResolve.get());
		try {
			promiseResolve.resolve(false);
			Assert.fail();
		}
		catch (IllegalStateException e) {
			assertEquals(resultChangeTest, promiseResolve.get());
		}
	}

	public void testSubscribe() {
		final Integer[] callbackTest = {1,1,1};
		callback call1 = () ->{
			callbackTest[0] = callbackTest[0]+3;
		};
		callback call2 = () ->{
			callbackTest[1] = 5;
		};
		callback call3 = () ->{
			callbackTest[2] = 6;
		};
		try {
			promiseSubscribe.subscribe(call1);
			assertEquals(new Integer (1), callbackTest[0]);
		}
		catch(Exception e) {
			Assert.fail();
		}
		try {
			promiseSubscribe.subscribe(call2);
		}
		catch(Exception e) {
			Assert.fail();
		}
		promiseSubscribe.resolve(true);
		assertEquals(new Integer(4), callbackTest[0]);
		assertEquals(new Integer(5), callbackTest[1]);
		try {
			promiseSubscribe.subscribe(call3);
		}
		catch (Exception e) {
			Assert.fail();
		}
		assertEquals(new Integer(6), callbackTest[2]);
	}
}