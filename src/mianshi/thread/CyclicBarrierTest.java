package mianshi.thread;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CyclicBarrierTest {
	
	public Runnable newThread(CyclicBarrier barrier) {
		return new Runnable() {
			public void run() {
				try {
					System.out.println(Thread.currentThread().getName() + " add start");
					Thread.sleep(1 + new Random().nextInt(1000));
					try {
						barrier.await(2000, TimeUnit.MILLISECONDS); // ÿ3���߳�await,����3���߳��е�һ������threadGroupEnd
						// ��ǰ���õ��߳�������Ϊ3,�߳�����Ϊ10�������һ��ֻ�ܽ���ȵ���ʱ
					} catch (TimeoutException e) {
						System.out.println(Thread.currentThread().getName() + " time out .Ϊ�˽�����������������ʱ�Ȳ�׽");
					}
					System.out.println(Thread.currentThread().getName() + " add end");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	private static Runnable threadGroupEnd() {
		return new Runnable() {
			@Override
			public void run() {
				System.out.println("=====" + Thread.currentThread().getName() + " ����β���� ");
			}
		};
	}

	public static void main(String args[]) throws InterruptedException {
		CyclicBarrierTest four = new CyclicBarrierTest();
		final CyclicBarrier barrier = new CyclicBarrier(2, threadGroupEnd());
		for (int i = 0; i < 4; i++) {
			new Thread(four.newThread(barrier)).start();
		}
		Thread.sleep(1000);
		for (int i = 0; i < 4; i++) {
			new Thread(four.newThread(barrier)).start();
		}
	}
}