package mianshi.spinlock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * TODO 可重入的自旋锁
 * 
 * @author <a href="mailto:zhuqingwei@zhexinit.com" >朱晴蔚</a>
 * @version 1.0.0
 */
public class SpinLock {
	AtomicReference<Thread> owner = new AtomicReference<Thread>();// 持有自旋锁的线程对象
	private int count;// 用一个计数器 来做 重入锁获取次数的计数

	public void lock(int num) {
		Thread cur = Thread.currentThread();
		System.out.println(cur.getName()+" ,num=" + num+"  into ,owner = " + (owner == null || owner.get() == null ? " null "
				: owner.get().getName()));
		if (cur == owner.get()) {
			count++;
			System.out.println(cur.getName() + " count++ = " + count);
			return;
		}
		System.out.println(cur.getName() + "  while");
		while (!owner.compareAndSet(null, cur)) {// 当线程越来越多 由于while循环 会浪费CPU时间片，CompareAndSet 需要多次对同一内存进行访问
			// 会造成内存的竞争，然而对于X86，会采取竞争内存总线的方式来访问内存，所以会造成内存访问速度下降(其他线程老访问缓存)，因而会影响整个系统的性能
			//System.out.println(cur.getName() + "  while");
		}
	}

	public void unLock(int num) {
		Thread cur = Thread.currentThread();
		System.out.println(cur.getName()+" ,num=" + num + "  out ,owner = " + (owner == null || owner.get() == null ? " null "
				: owner.get().getName()));
		if (cur == owner.get()) {
			if (count > 0) {
				count--;
				System.out.println(cur.getName() + " count-- = " + count);
			} else {
				owner.compareAndSet(cur, null);
				System.out.println(cur.getName() + " count unlock");

			}
		}
	}

}
