package mianshi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class NIOClient implements Runnable {
	// �������ӵĵ�ַ
	InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);
    private int index;
    public NIOClient(int index) {
    	this.index = index;
    }
	// ��Ҫһ��Selector
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new NIOClient(i)).start();
		}
	}

	@Override
	public void run() {

		// ��������ͨ��
		SocketChannel sc = null;
		// ����������
		ByteBuffer buf = ByteBuffer.allocate(1024);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		try {
			// ��ͨ��
			sc = SocketChannel.open();
			// ��������
			sc.connect(address);

			// ����һ���ֽ����飬Ȼ��ʹ��ϵͳ¼�빦�ܣ�
			byte[] bytes = ("Thread" +index+ ":" + new Random().nextInt(100000)).getBytes();

			// System.in.read(bytes);
			// �����ݷŵ���������
			buf.put(bytes);
			// �Ի��������и�λ
			buf.flip();
			// д������
			sc.write(buf);
			// ��ջ���������
			buf.clear();
			sc.read(buf2);
			buf2.flip();
			byte[] bytes2 = new byte[buf2.remaining()];
			// 7 ���ջ���������
			buf2.get(bytes2);
			System.out.println("Thread" +index + "�������ݣ�" + new String(bytes2).trim());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				try {
					sc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
