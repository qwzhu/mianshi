package mianshi.thread;

import java.util.Arrays;

public class WaitTest {
    public static Object object = new Object();
    
//    public static int[] s1 = new int[25];
//    public static char[] s2= new char[25];

    public static void main(String[] args) throws InterruptedException {
    	
    	//Arrays.setAll(s1, i -> i+1);
    	
        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();

        thread1.start();

        Thread.sleep(2000);

        thread2.start();
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println("线程" + Thread.currentThread().getName()
                        + "获取到了锁...");
                try {
                    System.out.println("线程" + Thread.currentThread().getName()
                            + "阻塞并释放锁...");
                    object.wait();
                } catch (InterruptedException e) {
                }
                System.out.println("线程" + Thread.currentThread().getName()
                        + "执行完成...");
            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println("线程" + Thread.currentThread().getName()
                        + "获取到了锁...");
                object.notify();
                System.out.println("线程" + Thread.currentThread().getName()
                        + "唤醒了正在wait的线程...");
            }
            System.out
                    .println("线程" + Thread.currentThread().getName() + "执行完成...");
        }
    }
}/* Output: 
        线程Thread-0获取到了锁...
        线程Thread-0阻塞并释放锁...
        线程Thread-1获取到了锁...
        线程Thread-1唤醒了正在wait的线程...
        线程Thread-1执行完成...
        线程Thread-0执行完成...
 *///:~
