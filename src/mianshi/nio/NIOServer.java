package mianshi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;

public class NIOServer {
	// 1 多路复用器（管理所有的通道）
	private Selector seletor;
	// 2 建立缓冲区
	private ByteBuffer readBuf = ByteBuffer.allocate(1024);
	// 3
	private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

	public NIOServer(int port) {
		try {
			// 1 打开路复用器
			this.seletor = Selector.open();
			// 2 打开服务器通道
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			// 3 设置服务器通道为非阻塞模式
			serverSocketChannel.configureBlocking(false);
			// 4 绑定地址
			serverSocketChannel.bind(new InetSocketAddress(port));
			// 5 把服务器通道注册到多路复用器上，并且监听阻塞事件
			serverSocketChannel.register(this.seletor, SelectionKey.OP_ACCEPT);
			while (true) {
				long timeout = 1000L; //超时时间设置为1s
				// 1 必须要让多路复用器开始监听
				this.seletor.select(timeout);
				// 2 返回多路复用器已经选择的结果集
				Iterator<SelectionKey> keys = this.seletor.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();
					// 6 如果是有效的
					if (key.isValid()) {
						if (key.isAcceptable()) {// 7 如果为阻塞状态
							this.accept(key);
						}
						if (key.isReadable()) { // 8 如果为可读状态
							this.read(key);
						}
						if (key.isWritable()) {// 9 写数据
							this.write(key); // ssc
						}
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void write(SelectionKey key) {
		SocketChannel ssc = (SocketChannel) key.channel();
		System.out.println("write key = " +key.toString());
		try {
			ssc.write(writeBuf);
			writeBuf.clear();
			ssc.close();
		} catch (Exception e) {
			System.out.println("Server write error: ");
		}
	}

	private void transferToWrite(SelectionKey key, String content) {
		SocketChannel ssc = (SocketChannel) key.channel();
		try {
			ssc.register(this.seletor, SelectionKey.OP_WRITE);
			writeBuf.put(content.getBytes());
			writeBuf.flip();
		} catch (Exception e) {
		}
	}

	private void read(SelectionKey key) {
		try {
			System.out.println("read key = " +key.toString());
			// 1 清空缓冲区旧的数据
			this.readBuf.clear();
			// 2 获取之前注册的socket通道对象
			SocketChannel sc = (SocketChannel) key.channel();
			// 3 读取数据
			int count = sc.read(this.readBuf);
			// 4 如果没有数据
			if (count == -1) {
				key.channel().close();
				key.cancel();
				return;
			}
			// 5 有数据则进行读取 读取之前需要进行复位方法(把position 和limit进行复位)
			this.readBuf.flip();
			// 6 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
			byte[] bytes = new byte[this.readBuf.remaining()];
			// 7 接收缓冲区数据
			this.readBuf.get(bytes);
			// 8 打印结果
			String body = new String(bytes).trim();
			System.out.println("Server Read: " + body);
			// 9..可以写回给客户端数据
			Thread.sleep(3000+new Random().nextInt(3000));
			transferToWrite(key, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void accept(SelectionKey key) {
		try {
			// 1 获取服务通道
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			// 2 执行阻塞方法
			SocketChannel sc = ssc.accept();
			// 3 设置阻塞模式
			sc.configureBlocking(false);
			System.out.println("accept key = " +key.toString());
			// 4 注册到多路复用器上，并设置读取标识
			sc.register(this.seletor, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new NIOServer(8765);
	}
}