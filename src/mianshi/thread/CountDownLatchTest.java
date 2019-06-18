package mianshi.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

	public Runnable newThread(CountDownLatch countDownLatch) {
		return new Runnable() {
			public void run() {

				System.out.println(Thread.currentThread().getName() + " add start");
				try {
					Thread.sleep(1 + new Random().nextInt(1000));
				} catch (InterruptedException e) {

				}
				countDownLatch.countDown(); // 数量减一，当前线程仍然执行
				System.out.println(Thread.currentThread().getName() + " add end");

			}
		};
	}

	public static void main(String args[]) throws InterruptedException {
		CountDownLatchTest four = new CountDownLatchTest();
		final CountDownLatch countDownLatch = new CountDownLatch(3);
		System.out.println("主线程开始执行啦~");
		for (int i = 0; i < 4; i++) {
			new Thread(four.newThread(countDownLatch)).start();
		}
		countDownLatch.await(); // 主线程阻塞到countDownLatch = 0
		System.out.println("主线程结束啦~");
	}
}