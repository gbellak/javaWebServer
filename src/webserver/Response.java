package webserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Response {
	
	private Request request;
	private String version;
	private String status; //A status code, indicating success or failure of the request
	private String statusText; //A status text. A brief, purely informational, textual description of the status code to help a human understand the HTTP message.
	private String contentLength;
	private String contentType;
	private String location;
	
	private byte[] responseByteFormat;
	private String responseTextFormat;
	
	public Response(Request request) {
		this.request = request;
		this.version = "HTTP/1.1";
		this.contentType = null;
		this.contentLength = null;
		this.responseByteFormat = null;
		this.responseTextFormat = null;
		this.location = null; //needed for redirect
			
	}
	

	public void sendResponse() throws IOException { //Creates and sends HTML response based on set properties
		
		PrintWriter out = new PrintWriter(request.getSocket().getOutputStream());
		out.println(generateStatusLine());
		System.out.println("StatusLine: \n"+generateStatusLine());// debug
		out.println(generateMsgHeader());
		
		if (this.location != null) {
			out.println("Location: "+this.location);
			System.out.println("Location: "+this.location +"\n");// debug
		}
		
		if (this.contentType != null) {
			out.println("Content-Type: "+this.contentType);
		}
		
		if (this.contentLength != null) {
			out.println("Content-Type: "+this.contentLength);
		}

		out.println(); // blank line between headers and content
		out.flush();
		
		//send text body if any
		if (this.responseTextFormat != null) {
			out.write(this.responseTextFormat);
			out.flush();
		}
		
		//or else send "byte-body" if any
		else if (this.responseByteFormat != null) {
			BufferedOutputStream dataOut = new BufferedOutputStream(request.getSocket().getOutputStream());
			dataOut.write(responseByteFormat, 0, responseByteFormat.length);
			dataOut.flush();
			
		}
		
		request.getSocket().close();
		
	}
	
//Setters for all properties below
	private String generateStatusLine() {
		return this.version + " " + this.status + " " + this.statusText;
	}
	
	private String generateMsgHeader() {
		String headerString = "Server: Java HTTP Server \r\n"+
							  "Date: " + new Date();
		return headerString;
	}
	

	public void setVersion(String version) {
		this.version = version;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}


	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setResponseByteFormat(byte[] responseByteFormat) {
		this.responseByteFormat = responseByteFormat;
	}


	public void setResponseTextFormat(String responseTextFormat) {
		this.responseTextFormat = responseTextFormat;
	}


	public void setLocation(String location) {
		this.location = location;
	}

	public void send_200_OK() {
		this.setStatus("200");
	    this.setStatusText("OK");
	    try {
			this.sendResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void send_404_NotFound() {
		this.setStatus("404");
	    this.setStatusText("Page not found!");
	    try {
			this.sendResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    
	public void send_501_NotSupported() {
		this.setStatus("501");
	    this.setStatusText("Method not supported!");
	    try {
			this.sendResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}