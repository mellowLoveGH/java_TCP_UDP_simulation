package test06;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

	public static void receiveData(DatagramSocket ds, DatagramPacket DpReceive, byte[] receive) {
		try {
			DpReceive = new DatagramPacket(receive, receive.length); 
			ds.receive(DpReceive); 
		} catch (Exception e) {
			
		}
	}

	public static StringBuilder data(byte[] a) {
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			ret.append((char) a[i]);
			i++;
		}
		return ret;
	}

	public static void main(String[] args) {
		
		try {
			int port = 1234;
			DatagramSocket ds = new DatagramSocket(port); 
			DatagramPacket DpReceive = null; 
			byte[] receive = null; 
			int len = 1024;
			
			receive = new byte[len];
			receiveData(ds, DpReceive, receive);
			String str = data(receive).toString();
			System.out.println("Client:-" + str);
			
			//
			len = Integer.parseInt((str.split(",")[1].trim())) + 1;
			receive = new byte[len];
			receiveData(ds, DpReceive, receive);
			String content = data(receive).toString();
			System.out.println("Client:-" + content);
			System.out.println("Client:-" + content.getBytes().length);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}
		
	}

}
