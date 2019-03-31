package mianshi.aio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	final static String ADDRESS = "127.0.0.1";
	final static int PORT = 8765;
    private int index ;
    public Client(int index) {
    	this.index = index;
    }
	public static void main(String[] args) {
		for(int i = 0 ;i<100 ;i++) {
			new Thread(new Client(i)).start();
		}
	}

	@Override
	public void run() {
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket(ADDRESS, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			// 向服务器端发送数据
			out.println("客户端["+this.index+"]数据："+ System.currentTimeMillis());
			String response = in.readLine();
			System.out.println("Client: " + response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			socket = null;
		}
		
	}
}
