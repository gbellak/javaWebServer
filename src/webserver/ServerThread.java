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
		Response response = new Response(request);
		
		//Do request routing in switch depending on url
		

		WebRequestHandler webrequesthandler = new WebRequestHandler(request, response);

			
		
		
		
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
