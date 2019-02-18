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
		String message =null; int row = 0;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		while ( (message = bufferedReader.readLine()) != null) {
			row +=1;
			System.out.println("Incoming message row " +row+ ": " + message);
		}
		socket.close();
		
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
