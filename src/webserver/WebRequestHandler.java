package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class WebRequestHandler {
	Request request;
	Response response;
	private HashMap<String,String> MIMEtypeMap;
	
	Date today = new Date();
	String httpResponse;
	
	String WEBROOT = "./webpages/";
	
		
	public WebRequestHandler(Request request, Response response) throws IOException{
		this.request = request;
		this.response = response;

		
		switch(this.request.header.get("method")){
			case "GET":
				processGET();
				break;
			
			
			case "POST":
				System.out.println("Post request handling....."); //debug
				this.response.setStatus("200");
				this.response.setStatusText("OK");
			try {
				this.response.sendResponse();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			
	
			case "HEAD":
				processHEAD(request);
				break;
			
			default:

				this.response.setStatus("501");
			    this.response.setStatusText("Method not supported!");
			    try {
					this.response.sendResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
	
	}
	private void processGET() throws IOException {

		
		String requestedFile = this.WEBROOT + this.request.getURL().replaceFirst("/", "");
		String[] contentType;
		
		System.out.println(requestedFile); //debug
		
		if (new File(requestedFile).isFile()) {
			File fileName = new File(requestedFile);
			
			contentType = findContentType(requestedFile.substring(requestedFile.lastIndexOf('.') + 1));
			System.out.println(requestedFile.substring(requestedFile.lastIndexOf('.') + 1)); //debug
			
			if(contentType[0] =="text"){
	
				StringBuilder contentBuilder = new StringBuilder();	
				try {
					    BufferedReader in = new BufferedReader(new FileReader(fileName));
					    String response;
					    while ((response = in.readLine()) != null) {
					        contentBuilder.append(response);
					    }
					    in.close();
				    
					    this.response.setStatus("200");
					    this.response.setStatusText("OK");
					    
					    this.response.setContentType(contentType[0]+"/"+contentType[1]);
					    
					    this.response.setResponseTextFormat(contentBuilder.toString());   
					    
					    this.response.sendResponse();
					    
					} 
				catch (IOException e) {
						send404();
					}
			}
			
			else { //if file found but NOT html file
				
						FileInputStream fileIn = null;
						byte[] fileData = new byte[(int) fileName.length()];
						
						try {
							fileIn = new FileInputStream(fileName);
							fileIn.read(fileData);
						} finally {
							if (fileIn != null) 
								fileIn.close();
						}
						
						this.response.setStatus("200");
					    this.response.setStatusText("OK");
					    this.response.setContentType("image/webp"); //hårdkodad!!!
					    this.response.setResponseByteFormat(fileData);   
					    
					    this.response.sendResponse();
					    
						
				}
			
		}		
		else { //if requested file not found
			send404();
			}
		}
			
		

	
	
	
	private String processHEAD(Request request) {
		return "";
	}
	
	private void send404() {
		this.response.setStatus("404");
	    this.response.setStatusText("Page not found!");
	    try {
			this.response.sendResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String[] findContentType(String fileExtension) {
		String[] contentType = new String[2];
		switch(fileExtension) {
		
		case "html":
		case "htm":
			contentType[0]="text";
			contentType[1]="html";
			break;
			
		case "txt":
			contentType[0]="text";
			contentType[1]="plain";
			break;
			
		case "jpg":
			contentType[0]="image";
			contentType[1]="jpg";
			break;
		case "gif":

			contentType[0]="image";
			contentType[1]="gif";
			break;
		case "png":

			contentType[0]="image";
			contentType[1]="png";
			break;
		case "mp4":

			contentType[0]="video";
			contentType[1]="mp4";
			break;
		
		default:

			contentType[0]="application";
			contentType[1]="*";
			break;
		}
		
		return contentType;
	}

}
