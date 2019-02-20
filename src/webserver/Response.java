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
	
	private byte[] responseByteFormat;
	private String responseTextFormat;
	
	public Response(Request request) {
		this.request = request;
		this.version = "HTTP/1.1";
		this.contentType = null;
		this.contentLength = null;
		this.responseByteFormat = null;
		this.responseTextFormat = null;
			
	}
	

	public void sendResponse() throws IOException { //Creates and sends HTML response based on set properties
		PrintWriter out = new PrintWriter(request.getSocket().getOutputStream());
		out.println(generateStatusLine());	
		out.println(generateMsgHeader());
		
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

	
}