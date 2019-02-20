package webserver;

import java.io.IOException;
import java.util.HashMap;

public class GreetingsApp {
	Request request;
	Response response;
	String htmlPrefix;
	String htmlPostfix;
	
	public GreetingsApp(Request request, Response response) throws IOException{
		this.request = request;
		this.response = response;
		
		this.htmlPrefix = "<!DOCTYPE html><html><head><meta charset='ISO-8859-1'><title>Greetings!</title></head><body>"
				+ "<h2>Welcome to the Greeting App</h2><h3>The most friendly app on the Internet</h3><hr><br>";
		
		this.htmlPostfix = "<hr></body></html>";
		
		switch(this.request.header.get("url")){
			case "/Greetings/":
			case "/greetings/":
				greetingForm();
				break;
			
				
			default:
				displayGreeting();
				break;
			}
	
	}
	
	private void displayGreeting() {
		String[]greetingParts = this.request.header.get("url").split("/");
		greetingParts[1]="";
		
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append(this.htmlPrefix);
		contentBuilder.append("<h1>A really warm Welcome to you: ");
		
		for(String part: greetingParts) {
			contentBuilder.append(part + " ");
						
		}
		
		contentBuilder.append("</h1>" + this.htmlPostfix);
		
		this.response.setStatus("200");
	    this.response.setStatusText("OK");
	    
	    this.response.setContentType("text/html");
	    
	    this.response.setResponseTextFormat(contentBuilder.toString());   
	    
	    try {
			this.response.sendResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void greetingForm() {
		switch(this.request.header.get("method")){
		
		
		case "POST":
			if (this.request.hasBody) {
				System.out.println("has body!");// debug
				
				StringBuilder contentBuilder = new StringBuilder();		
				contentBuilder.append("/Greetings/");
				
				String[] bodyparts  = this.request.body.split("&");
				for(String part : bodyparts) {
					
					Integer index = part.indexOf("=");
					contentBuilder.append(part.substring(index+1)+"/"); //skip the =
				}
		
				this.response.setStatus("302");
			    this.response.setStatusText("Relocate");
			    this.response.setLocation(contentBuilder.toString());
			    this.response.setContentType("text/html");
			    
			    System.out.println(contentBuilder.toString());// debug
			    try {
					this.response.sendResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		break;
		
		case "GET":
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder = new StringBuilder();
			contentBuilder.append(this.htmlPrefix);
			contentBuilder.append("<h2>Greeting Request</h2><form  method='post'>Salutation:<br><input type='text' name='salutation' value='' placeholder = 'Mr'>");
			contentBuilder.append("<br>First name:<br><input type='text' name='firstname' value='' placeholder = 'Bob'>");
			contentBuilder.append("<br>Last name:<br><input type='text' name='lastname' value='' placeholder = 'Dobalina'><br><br>");
			contentBuilder.append("<input type='submit' value='Submit'></form>");
			contentBuilder.append(this.htmlPostfix);
			
			this.response.setStatus("200");
		    this.response.setStatusText("OK");
		    	    
		    this.response.setContentType("text/html");
		    this.response.setResponseTextFormat(contentBuilder.toString());
		    
		    try {
				this.response.sendResponse();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
		 break;
		 
		 //add default: method not implemented message
	    
		}
		
	}
}


