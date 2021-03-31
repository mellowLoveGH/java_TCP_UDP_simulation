package test06;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPtoTCPServer implements Runnable{
	
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
	
	public static void sendPCK(DatagramSocket ds, int port, byte buf[]) {
		try {
			//
			InetAddress ip = InetAddress.getLocalHost();
			DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, port);
			//
			ds.send(DpSend);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		
		try {
			//receive data port
			int receive_port = 1234;
			DatagramSocket ds_receive = new DatagramSocket(receive_port); 
			DatagramPacket DpReceive = null; 
			byte[] receive = null; 
			int len = 1024;
			//send acknowledgement port
			DatagramSocket ds_send = new DatagramSocket();
			int send_port = 1235;
			byte send[] = null;
			String ack = "";
			
			//receive header
			receive = new byte[len];
			receiveData(ds_receive, DpReceive, receive);
			String header = data(receive).toString();
			System.out.println("header:\n" + header);
			//
			ack = "ack header";
			send = ack.getBytes();
			//send acknowledgement if received
			//if not, try to receive again
			if(header == null || "".equals(header)) {
				while(header == null || "".equals(header)) {
					receive = new byte[len];
					receiveData(ds_receive, DpReceive, receive);
					header = data(receive).toString();
					System.out.println("header:\n" + header);
					//
					ack = "ack header";
					send = ack.getBytes();
					sendPCK(ds_send, send_port, send);
					System.out.println("send header ack again");
				}
			}else {
				sendPCK(ds_send, send_port, send);
				System.out.println("send header ack");
			}
			
			
			//
			len = Integer.parseInt((header.split(",")[1].trim())) + 1;
			receive = new byte[len];
			receiveData(ds_receive, DpReceive, receive);
			String content = data(receive).toString();
			System.out.println("content:\n" + content);
			//
			ack = "ack content";
			send = ack.getBytes();
			//send acknowledgement if received
			//if not, try to receive again
			if(content == null || "".equals(content)) {
				while(content == null || "".equals(content)) {
					receive = new byte[len];
					receiveData(ds_receive, DpReceive, receive);
					content = data(receive).toString();
					System.out.println("content:\n" + content);
					//
					ack = "ack content";
					send = ack.getBytes();
					sendPCK(ds_send, send_port, send);	
					System.out.println("send header ack again");
				}
			}else {
				sendPCK(ds_send, send_port, send);
				System.out.println("send header ack");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1 * 1000);
			main(null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
