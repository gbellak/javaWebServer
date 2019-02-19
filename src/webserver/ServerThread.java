package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {
	Socket socket;
	
	
	ServerThread(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		
		try {

		Request request = new Request(socket);
		

		//Do request routing in switch
		

		WebRequestHandler webrequesthandler = new WebRequestHandler(request);
		String httpResponse = webrequesthandler.httpResponse;
		
		try {
		if (httpResponse.length()>0) {
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			} 
		}
		catch (Exception e){socket.getOutputStream().write("HTTP/1.1 404 Page could not be returned\r\n\r\n".getBytes("UTF-8"));}
			
		
		socket.close();
		
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
