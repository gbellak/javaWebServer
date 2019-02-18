package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class WebRequestHandler {
	Request request;
	Date today = new Date();
	String httpResponse;
	
	String WEBROOT = "./webpages/";
	
		
	public WebRequestHandler(Request request){
		this.request = request;
		
		switch(this.request.header.get("method")){
			case "GET":
				this.httpResponse = processGET(request);
				break;
			
			
			case "POST":
				this.httpResponse = processPOST(request);
				break;
			
	
			case "HEAD":
				processHEAD(request);
				break;
			
			default:
				this.httpResponse = "HTTP/1.1 501 Method not implemented\r\n\r\n" + this.today;
			}
		
		
	
	}
	private String processGET(Request request) {
		String uri = request.header.get("uri");
		if(uri.equals("/") || uri.equals("/home")) {
			uri ="/index.html";
		}
		//borde ha filnamnet efter detta:
		uri = uri.replaceFirst("/", "");
		
		try {
		uri = uri.split("?",2)[0];
		} catch (Exception e) {
			
		}
		
		uri = this.WEBROOT + uri;
		
		System.out.println(uri);
		
		if (new File(uri).isFile()) {
			File fileName = new File(uri);
		
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder.append("HTTP/1.1 200 OK\r\n\r\n");
			
			try {
			    BufferedReader in = new BufferedReader(new FileReader(fileName));
			    String response;
			    while ((response = in.readLine()) != null) {
			        contentBuilder.append(response);
			    }
			    in.close();
			} catch (IOException e) {
			}

			return contentBuilder.toString();
				}
		
		
		
		return "";
	}
	
	private String processPOST(Request request) {
		return "HTTP/1.1 200 OK\r\n\r\n" + this.today;
	}
	
	private String processHEAD(Request request) {
		return "";
	}

}
