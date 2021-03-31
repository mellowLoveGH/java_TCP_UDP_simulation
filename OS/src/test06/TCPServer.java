package test06;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPServer extends Thread {

	private ServerSocket serverSocket;

	public TCPServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(10*1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void run() {
		while (true) {
			try {
				System.out.println();
				System.out.println("there is client, port is: " + serverSocket.getLocalPort());
				Socket server = serverSocket.accept();
				DataInputStream in = new DataInputStream(server.getInputStream());
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				//receive header
				System.out.println("\nheader: ");
				System.out.println(in.readUTF());
				out.writeUTF("received header!");
				
				//receive content
				System.out.println("\ncontent: ");
				System.out.println(in.readUTF());
				out.writeUTF("received content!");
				
				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {

		int port = 6060;
		try {
			Thread t = new TCPServer(port);
			t.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
