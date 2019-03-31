package mianshi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class NIOClient implements Runnable {
	// 创建连接的地址
	InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);
    private int index;
    public NIOClient(int index) {
    	this.index = index;
    }
	// 需要一个Selector
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new NIOClient(i)).start();
		}
	}

	@Override
	public void run() {

		// 声明连接通道
		SocketChannel sc = null;
		// 建立缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		try {
			// 打开通道
			sc = SocketChannel.open();
			// 进行连接
			sc.connect(address);

			// 定义一个字节数组，然后使用系统录入功能：
			byte[] bytes = ("Thread" +index+ ":" + new Random().nextInt(100000)).getBytes();

			// System.in.read(bytes);
			// 把数据放到缓冲区中
			buf.put(bytes);
			// 对缓冲区进行复位
			buf.flip();
			// 写出数据
			sc.write(buf);
			// 清空缓冲区数据
			buf.clear();
			sc.read(buf2);
			buf2.flip();
			byte[] bytes2 = new byte[buf2.remaining()];
			// 7 接收缓冲区数据
			buf2.get(bytes2);
			System.out.println("Thread" +index + "返回数据：" + new String(bytes2).trim());

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
