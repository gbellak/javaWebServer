package webserver;

import java.util.HashMap;

public class Request {
	public HashMap<String,String> header = new HashMap<String,String>();
	
	public void setHeader (String key, String value) {
		this.header.put(key, value);
	}
	
	

}
