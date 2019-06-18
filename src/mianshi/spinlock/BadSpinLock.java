package mianshi.spinlock;



import java.util.concurrent.atomic.AtomicReference;

public class BadSpinLock {
 
	AtomicReference<Thread> owner = new AtomicReference<Thread>();//持有自旋锁的线程对象
	public void lock() {
		Thread cur = Thread.currentThread();
		while (!owner.compareAndSet(null, cur)) {
			System.out.println(cur.getName()+ " wait lock release");
		}
	}
	
	public void unLock() {
		Thread cur = Thread.currentThread();
		if (cur == owner.get()) {
			owner.compareAndSet(cur, null);
			System.out.println(cur.getName()+ " release lock");
		}
	}
 
 
}
