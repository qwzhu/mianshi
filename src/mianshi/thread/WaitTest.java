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
                System.out.println("�߳�" + Thread.currentThread().getName()
                        + "��ȡ������...");
                try {
                    System.out.println("�߳�" + Thread.currentThread().getName()
                            + "�������ͷ���...");
                    object.wait();
                } catch (InterruptedException e) {
                }
                System.out.println("�߳�" + Thread.currentThread().getName()
                        + "ִ�����...");
            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println("�߳�" + Thread.currentThread().getName()
                        + "��ȡ������...");
                object.notify();
                System.out.println("�߳�" + Thread.currentThread().getName()
                        + "����������wait���߳�...");
            }
            System.out
                    .println("�߳�" + Thread.currentThread().getName() + "ִ�����...");
        }
    }
}/* Output: 
        �߳�Thread-0��ȡ������...
        �߳�Thread-0�������ͷ���...
        �߳�Thread-1��ȡ������...
        �߳�Thread-1����������wait���߳�...
        �߳�Thread-1ִ�����...
        �߳�Thread-0ִ�����...
 *///:~
