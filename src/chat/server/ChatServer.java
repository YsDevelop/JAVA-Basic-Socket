package chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {
	private ServerSocket[] servers;
	private Socket[] sockets;
	private int[] ports;
	
	public static void main(String[] args) {
		
		int port=6077;
		
		ServerSocket serverSocket = null;
		
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufReader = null;
		
		OutputStream outStream = null;
		OutputStreamWriter outStreamWriter = null;
		PrintWriter printWriter = null;
		Scanner scanner = new Scanner(System.in);
		
		
		try {
			// ServerSocket���� �� client ���� accept
			serverSocket = new ServerSocket();
			// Socket�� SocketAddress(IpAddress + Port) ���ε� ��
				// InetSocketAdress�� ȣ��Ʈ��, ������ ������ �ּҷ� ������ ���� �� accept���� ����
				// new ServerSocket(8080);�� ��� ����̽� �����°� ���
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, port));
			System.out.println("server binding : "+localhost);
			// accept Ŭ���̾�Ʈ�� ���� ��û�� ��ٸ���.
			Socket socket = serverSocket.accept();
			InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			System.out.println("connected by client");
            System.out.println("Connect with : " + socketAddress.getHostString() + " " + socket.getPort());
            // client�� ���� �����͸� ����Ѵ�.
				// client������ InputStream�� ������ InputStreamReader�� �ְ� BufferedReader�� ���μ� BufferedReader�� ��� �� �� �ְ� �����. �� �۾��� ���ϸ� Byte�迭�� ���� �о�� �� �� �ִ�.
					// socket.getInputStream().read()�� byte[]�� ���� ����
					// new InputStreamReader(socket.getInputStream()).read()�� char[]�� ���� ����
					// �׷��� BufferedReadr, InputStreamReader�� �� ����ؾ� �ϴ� ���� �ƴϳ�, String, char���� ����ؾ� �� �� ����ϸ� ����.
					// ��Ȥ byte[]�� �޾ƾ� �� ���� �ִµ� �׶� socket�� ��Ʈ���� �״�� ����ϸ� �ȴ�.
            		// �ش系���� ������Ʈ�� ����
            while(true) {
            	inputStream = socket.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            	bufReader = new BufferedReader(inputStreamReader);
            	
            	outStream = socket.getOutputStream();
            	outStreamWriter = new OutputStreamWriter(outStream);
            	printWriter = new PrintWriter(outStreamWriter, true);
            	
            	String buffer = null;
                buffer = bufReader.readLine(); // Blocking
                if (buffer == null) {
 
                    // �������� : remote socket close()
                    // �޼ҵ带 ���ؼ� ���������� ������ ���� ���
                    System.out.println("closed by client");
                    break;
 
                }
 
                System.out.println("[server] recived : " + buffer);
                printWriter.println(buffer);
            	
            }			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Chat Server Error : "+e.toString());
		}finally {
			try {
				 
                if (serverSocket != null && !serverSocket.isClosed())
                    serverSocket.close();
 
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            scanner.close();
		}
	}
	
}

