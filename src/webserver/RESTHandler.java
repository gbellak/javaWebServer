package webserver;

import java.io.IOException;
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
			System.out.println(this.request.getURL()+"is NOT correct");
			
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
					contentBuilder.append(person.toString());				
					}
				
				contentBuilder.append("]");
				
				this.response.setStatus("200");
			    this.response.setStatusText("OK");
			    
			    this.response.setContentType("application/json");
			    this.response.setResponseTextFormat(contentBuilder.toString());
			    try {
					this.response.sendResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    
		}
	}
}
