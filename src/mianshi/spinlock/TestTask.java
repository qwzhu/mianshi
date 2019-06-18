package mianshi.spinlock;

import java.util.Random;

public class TestTask implements Runnable{
	static int sum;
	private BadSpinLock badLock;
	private SpinLock lock;
	
	
	public TestTask(BadSpinLock badLock) {
		this.badLock = badLock;
	}
	
	
	
	public TestTask(SpinLock lock) {
		this.lock = lock;
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
	//	BadSpinLock lock = new BadSpinLock();
		SpinLock lock = new SpinLock();
		for (int i = 0; i < 2; i++) {
			TestTask test = new TestTask(lock);
			Thread t = new Thread(test);
			t.start();
		}
		
	}
	
	@Override
	public void run() {
		try {
		this.lock.lock(1);
		
			Thread.sleep(1000+new Random().nextInt(1000));
			this.lock.lock(2);
			Thread.sleep(1000+new Random().nextInt(1000));

			this.lock.unLock(1);
			Thread.sleep(1000+new Random().nextInt(1000));

			this.lock.unLock(2);
		} catch (InterruptedException e) {
			
		}
		
	}
 
}
