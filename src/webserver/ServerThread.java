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
		int row = 0; String[] parts = null; String message;
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Request request = new Request();
		
		do {
			message = bufferedReader.readLine();
		} while ((message == null || message.isEmpty() ));
		
		
		parts = message.trim().split("\\s+");
		
		request.setHeader("method", parts[0]);
		request.setHeader("uri", parts[1]);
		request.setHeader("version", parts[2]);

		System.out.println(request.header.get("method"));
		
		System.out.println("1st message row " +request.header.get("method")+ "-" + request.header.get("uri")+ "-" + request.header.get("version"));
		
		while (message != null && !message.isEmpty()) {
			row +=1;
			message = bufferedReader.readLine();
			
			
			System.out.println("Incoming message row " +row+ ": " + message);
			if (!message.isEmpty()){	
				parts = null;		
				parts = message.split(": ", 2);
			
				request.setHeader(parts[0], parts[1]);
				}
			}
		
		System.out.println(request.header.get("method"));

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
