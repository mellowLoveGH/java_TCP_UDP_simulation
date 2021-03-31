package test06;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class UDPtoTCPClient implements Runnable{
	public static String metaFile(String path) {
		try {
			File file = new File(path);
			Path p = Paths.get(file.getAbsolutePath());
			BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			FileTime fileTime = view.creationTime();
			
			return (path + ", " + view.size() + ", " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((fileTime.toMillis())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String readFile(String path) {
		String str = "";
		try {
			File file = new File(path);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				str = str + st + "\n";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return str.trim();
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
		String path = "file.txt";
		String header = metaFile(path);
		String content = readFile(path);
		
		try {
			//port for send
			DatagramSocket ds_send = new DatagramSocket();
			int send_port = 1234;
			byte send[] = null;
			//port for receive
			int receive_port = 1235;
			DatagramSocket ds_receive = new DatagramSocket(receive_port); 
			DatagramPacket DpReceive = null; 
			byte[] receive = null; 
			int len = 1024;
			
			//send header
			send = header.getBytes();
			sendPCK(ds_send, send_port, send);
			System.out.println("send header");
			//receive acknowledgement
			receive = new byte[len];
			receiveData(ds_receive, DpReceive, receive);
			String header_ack = data(receive).toString().trim();
			//if acknowledgement is not correct, try again
			if(! "ack header".equals(header_ack)) {
				while(! "ack header".equals(header_ack)) {
					send = header.getBytes();
					sendPCK(ds_send, send_port, send);
					System.out.println("send header again");
					
					receive = new byte[len];
					receiveData(ds_receive, DpReceive, receive);
					header_ack = data(receive).toString().trim();
					System.out.println("send header again----------------------------");
				}
				System.out.println("get: " + header_ack);
			}else {
				System.out.println("get: " + header_ack);
			}
			
			
			//send content
			send = content.getBytes();
			sendPCK(ds_send, send_port, send);			
			System.out.println("send content");	
			//receive acknowledgement
			len = Integer.parseInt((header.split(",")[1].trim())) + 1;
			receive = new byte[len];
			receiveData(ds_receive, DpReceive, receive);
			String content_ack = data(receive).toString().trim();
			//if acknowledgement is not correct, try again
			if(! "ack content".equals(content_ack)) {
				while(! "ack content".equals(content_ack)) {
					send = content.getBytes();
					sendPCK(ds_send, send_port, send);			
					System.out.println("send content again");	
					
					receive = new byte[len];
					receiveData(ds_receive, DpReceive, receive);
					content_ack = data(receive).toString().trim();
					System.out.println("send content again----------------------------");
				}
				System.out.println("get: " + content_ack);
			}else {
				System.out.println("get: " + content_ack);
			}
		} catch (Exception e) {
			// TODO: handle exception
			
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
