package mianshi.spinlock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * TODO �������������
 * 
 * @author <a href="mailto:zhuqingwei@zhexinit.com" >����ε</a>
 * @version 1.0.0
 */
public class SpinLock {
	AtomicReference<Thread> owner = new AtomicReference<Thread>();// �������������̶߳���
	private int count;// ��һ�������� ���� ��������ȡ�����ļ���

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
		while (!owner.compareAndSet(null, cur)) {// ���߳�Խ��Խ�� ����whileѭ�� ���˷�CPUʱ��Ƭ��CompareAndSet ��Ҫ��ζ�ͬһ�ڴ���з���
			// ������ڴ�ľ�����Ȼ������X86�����ȡ�����ڴ����ߵķ�ʽ�������ڴ棬���Ի�����ڴ�����ٶ��½�(�����߳��Ϸ��ʻ���)�������Ӱ������ϵͳ������
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
