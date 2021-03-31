package test06;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class TCPClient {

	public static String metaFile(String path) {
		try {
			File file = new File(path);
			Path p = Paths.get(file.getAbsolutePath());
			BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			FileTime fileTime = view.creationTime();

			return (path + ", " + view.size() + ", "
					+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((fileTime.toMillis())));
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

	public static void main(String[] args) {
		
		String serverName = "localhost";
		int port = 6060;
		String path = "file.txt";
		try {
			System.out.println("connect to server: " + serverName + ", port is: " + port);
			Socket client = new Socket(serverName, port);
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			
			//send header
			String header = metaFile(path);
			out.writeUTF(header);
			System.out.println("acknowledgement from server: " + in.readUTF());
			
			//
			String content = readFile(path);
			out.writeUTF(content);
			System.out.println("acknowledgement from server: " + in.readUTF());
			
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
