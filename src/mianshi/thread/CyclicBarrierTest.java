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
						barrier.await(2000, TimeUnit.MILLISECONDS); // 每3个线程await,则这3个线程中的一个调用threadGroupEnd
						// 当前设置的线程组容量为3,线程数量为10，多余的一个只能进入等到超时
					} catch (TimeoutException e) {
						System.out.println(Thread.currentThread().getName() + " time out .为了进程能正常结束，暂时先捕捉");
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
				System.out.println("=====" + Thread.currentThread().getName() + " 做结尾工作 ");
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