package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {
	Socket socket;
	String requestURL;
	
	
	ServerThread(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		
		try {

		Request request = new Request(socket);
		Response response = new Response(request);
		requestURL = request.header.get("url");
		
		//Do request routing in switch depending on url
		if(requestURL.startsWith("/greetings/") || requestURL.startsWith("/Greetings/")) {
			GreetingsApp greetingsApp = new GreetingsApp(request, response);
		}

		else {
			WebRequestHandler webrequesthandler = new WebRequestHandler(request, response);
		}

			
		
		
		
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
