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
			// ServerSocket생성 및 client 소켓 accept
			serverSocket = new ServerSocket();
			// Socket에 SocketAddress(IpAddress + Port) 바인딩 함
				// InetSocketAdress는 호스트명, 아이피 지정한 주소로 들어오지 않을 시 accept하지 않음
				// new ServerSocket(8080);은 모든 디바이스 들어오는것 허용
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, port));
			System.out.println("server binding : "+localhost);
			// accept 클라이언트로 부터 요청을 기다린다.
			Socket socket = serverSocket.accept();
			InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			System.out.println("connected by client");
            System.out.println("Connect with : " + socketAddress.getHostString() + " " + socket.getPort());
            // client가 보낸 데이터를 출력한다.
				// client소켓의 InputStream을 가져와 InputStreamReader에 넣고 BufferedReader로 감싸서 BufferedReader로 사용 할 수 있게 만든다. 이 작업을 안하면 Byte배열로 값을 읽어야 할 수 있다.
					// socket.getInputStream().read()는 byte[]로 값을 리턴
					// new InputStreamReader(socket.getInputStream()).read()는 char[]로 값을 리턴
					// 그래서 BufferedReadr, InputStreamReader는 꼭 사용해야 하는 것은 아니나, String, char등을 사용해야 할 때 사용하면 좋다.
					// 간혹 byte[]로 받아야 할 때가 있는데 그땐 socket의 스트림을 그대로 사용하면 된다.
            		// 해당내용은 보조스트림 참조
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
 
                    // 정상종료 : remote socket close()
                    // 메소드를 통해서 정상적으로 소켓을 닫은 경우
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

