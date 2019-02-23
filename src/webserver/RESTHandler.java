package webserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import personregister.DBHandler;
import personregister.Person;

public class RESTHandler {
	Request request;
	Response response;
	DBHandler db_handler;
	List<Person> persons;


	public void manageRequest(Request request, Response response) throws SQLException {
		this.request = request;
		this.response = response;
		this.db_handler = new DBHandler();
		

		if(this.request.getURL().equals("/api/person/")) {
			personCollection();
			
		
			}
		
		else {
			try {
			String suffix = this.request.getURL().substring(12); //lenght of "/api/person/"		
			int index = Integer.parseInt(suffix);
			personDetail(index);
			}
			catch (Exception e) {
				//return page not found from here
			}
		}

	}
	
	private void personDetail(int index) throws SQLException {
		
		Person person = db_handler.getPerson_by_ID(index);
		
		switch (this.request.header.get("method")) {
			
			case "GET":
				this.response.setStatus("200");
			    this.response.setStatusText("OK");
			    
			    this.response.setContentType("application/json");
			    this.response.setResponseTextFormat(person.toString());
			    try {
					this.response.sendResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    break;
			    
			case "PUT":
				String body = request.body;
				System.out.println("message body:  "+ body);
				//db_handler.updatePerson(person2);
				
			 
			default:
				//send method not supported
				break;
			
	}		
		
		
	}

	private void personCollection() throws SQLException {
		switch (this.request.header.get("method")) {
			case "GET":
				StringBuilder contentBuilder = new StringBuilder();
				persons= new ArrayList<Person>();
				persons = db_handler.listPerson("");
				
				contentBuilder.append("[");
				for(Person person:persons) {
					contentBuilder.append(person.toString()+",");				
					}
				contentBuilder.setLength(contentBuilder.length() - 1); //remove last ","
				contentBuilder.append("]");

				
				this.response.setStatus("200");
			    this.response.setStatusText("OK");
			    this.response.setResponseTextFormat(contentBuilder.toString());
			    
			    this.response.setContentType("application/json");
			    
			    try {
					this.response.sendResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    break;
			
			default:
				//send method not supported
				break;
			    
		}
	}
}
