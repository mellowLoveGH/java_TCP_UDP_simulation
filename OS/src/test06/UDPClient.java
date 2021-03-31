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

public class UDPClient {

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

	public static void sendPCK(DatagramSocket ds, InetAddress ip, int port, byte buf[]) {

		try {
			//
			DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, port);
			//
			ds.send(DpSend);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void main(String[] args) {
		String path = "file.txt";
		String header = metaFile(path);
		String content = readFile(path);
		// byte[] file_bt = str.getBytes();
		// byte[] pck_bt = new byte[file_bt.length + 1];
		// pck_bt[0] = 1;
		// for (int i = 1; i < pck_bt.length; i++) {
		// pck_bt[i] = file_bt[i - 1];
		// }
		// byte[] rec_bt = new byte[file_bt.length];
		// for (int i = 0; i < rec_bt.length; i++) {
		// rec_bt[i] = pck_bt[i + 1];
		// }
		// System.out.println(new String(rec_bt));
		
		
		try {
			DatagramSocket ds = new DatagramSocket();
			InetAddress ip = InetAddress.getLocalHost();
			int port = 1234;
			byte buf[] = null;
			
			buf = header.getBytes();
			sendPCK(ds, ip, port, buf);
			System.out.println("send header");
			
			buf = content.getBytes();
			sendPCK(ds, ip, port, buf);			
			System.out.println("send content");
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}

	}

}
