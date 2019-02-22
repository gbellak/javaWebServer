package webserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class Request {
	Boolean hasBody; //Not implemented yet!!!
	Boolean hasParameters;
	
	private Socket socket;
	
	public HashMap<String,String> header = new HashMap<String,String>(); //should be private with getters
	public HashMap<String,String> parameters = new HashMap<String,String>(); //should be private with getters
	
	public String body;
	
	public Request(Socket socket) throws IOException {
		this.socket = socket;
		this.hasBody = false;
		this.hasParameters =false;
		parseRequest();
	}
	
	public Socket getSocket() {
		return this.socket;  //return the socket bound to this request
	}
	
	
	private void parseRequest() throws IOException {
	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

	String[] parts = null; String message;
	
	//Get first message header row = Request-Line:
	
	do {
		message = bufferedReader.readLine();
	} while ((message == null || message.isEmpty() ));  
	
	//In the interest of robustness, servers SHOULD ignore any empty line(s) received where a Request-Line is expected. In other words, if the server is reading the protocol stream at the beginning of a message and receives a CRLF first, it should ignore the CRLF.
	
	
	parts = message.trim().split("\\s+");
	
	this.header.put("method", parts[0]);
	this.header.put("uri", parts[1]);
	this.header.put("version", parts[2]);
	
	ParseURI(parts[1]);

	//Get remaining message header rows:
	
	while(true){
		message = bufferedReader.readLine();
	
		if (message != null && !message.isEmpty()){	
			parts = null;		
			parts = message.split(": ", 2);
			
			this.header.put(parts[0], parts[1]);

			}
		else {
			break;
			}
				
		}
	
	//Get message body if content length >0
	if(this.header.get("Content-Length") != null && Integer.parseInt(this.header.get("Content-Length"))>0){
		StringBuilder contentBuilder = new StringBuilder();
		while(true){
			message = bufferedReader.readLine();
			
		
			if (message != null && !message.isEmpty()){	
				contentBuilder.append(message);

				}
			else {
				break;
				}
					
			}
		this.body = contentBuilder.toString();
		this.hasBody = true;
		}
	
		
	System.out.println(this); //Debug
	}
	
	//Parsing the incoming uri into url + parameters
	private void ParseURI(String uri) {
		String url;
		String params = null;
		
		if(uri.equals("/") || uri.equals("/home")) {
			uri ="/index.html";
		}
		
	
		Integer index = uri.indexOf("?");
		
		if (index > 0) {
			params = uri.substring(index+1); //skip the ?
			url = uri.substring(0, index);
			this.hasParameters = true;
						
			ParseParameters(params);
		}
		
		else {
			url = uri;
			
		}
		
		header.put("url", url);

	}
	
	//parsing any incoming parameters from the uri
	private void ParseParameters(String params) {
		String[] param_pairs = params.split("&");
		String[] param_parts;
		
		for (String pair : param_pairs) {	
			param_parts = pair.split("=");
			this.parameters.put(param_parts[0], param_parts[1]);	
		}
		
		
	}
	
	public String getURL() {
		return this.header.get("url").toLowerCase().trim();
	}
	
	@Override
	public String toString() {
		
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append("Parsed Request*******\n");
		
		for (String key : header.keySet()) {
			contentBuilder.append(key +" : " + header.get(key)+"\n");
					}
		
		if (this.hasParameters) {
			contentBuilder.append("Parsed Parameters*****\n");
			
			for (String key : parameters.keySet()) {
				contentBuilder.append(key +" : " + parameters.get(key)+"\n");
						}
		}
		
		if (this.hasBody) {
			contentBuilder.append("Message Body*****\n");
			contentBuilder.append(this.body+"\n");
		}
				
		return contentBuilder.toString();
	

}
}
