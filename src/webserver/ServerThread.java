package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;

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
		
		
		//Do request routing in switch depending on url
		if(!request.route.isEmpty()) {
			
			switch (request.route.get(0)) {
				
				case "greetings":
				case "Greetings":
					GreetingsApp greetingsApp = new GreetingsApp();
					greetingsApp.manageRequest(request, response);
				break;
				
				case "api":
					RESTHandler resthandler = new RESTHandler();
					try {
						resthandler.manageRequest(request,response);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				break;
				
				default:
					WebRequestHandler webrequesthandler = new WebRequestHandler(request, response);	
			
			}
		}


		else {
			WebRequestHandler webrequesthandler = new WebRequestHandler(request, response);
		}	
		
		
		
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}


}
