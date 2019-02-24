package webserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import personregister.DBHandler;
import personregister.Person;

public class RESTHandler {
	Request request;
	Response response;
	DBHandler db_handler;
	List<Person> persons;
	Gson gson;
	JsonParser jsonParser;


	public void manageRequest(Request request, Response response) throws SQLException {
		this.request = request;
		this.response = response;
		this.db_handler = new DBHandler();
		this.gson = new Gson();
		this.jsonParser = new JsonParser();
		
		switch (this.request.route.get(1)) {  //api/ routing table
			case "person":
				if(this.request.route.size()==2) { //if no more arguments
					personCollection();
				}
				
				else {
					personSingular(Integer.parseInt(this.request.route.get(2)));
				}
			break;
			
			default:
				break;
			
		}
				
		}
		

	
	private void personSingular(int index) throws SQLException {
		Person person;
		Person personTEMP;
		
		
		switch (this.request.header.get("method")) {
			
			case "GET":
				person = db_handler.getPerson_by_ID(index);
			    
			    this.response.setContentType("application/json");
			    this.response.setResponseTextFormat(gson.toJson(person));
			    this.response.send_200_OK();
			    break;
			    
			case "PATCH":
			case "PUT":  //Update Person Record
				person = db_handler.getPerson_by_ID(index);
				personTEMP = gson.fromJson(this.request.body, Person.class);
				
				person.setFirstName(personTEMP.getFirstName());
				person.setLastName(personTEMP.getLastName());
				person.setBirthYear(personTEMP.getBirthYear());
				person.setCity(personTEMP.getCity());
				
				
				
			    try {
			    	db_handler.updatePerson(person);
			    	
			    	this.response.send_200_OK();
					
			    } catch (SQLException e) {
			    	this.response.send_404_NotFound();
					e.printStackTrace();
				}
				
				this.response.send_200_OK();
				System.out.println(person +" updated in DB");

				break;
				
				
			case "POST":  //Add New Person Record to db
				personTEMP = gson.fromJson(this.request.body, Person.class);
				person = new Person(personTEMP.getFirstName(),personTEMP.getLastName(), personTEMP.getBirthYear(), personTEMP.getCity() );
				
				db_handler.addPerson(person);
				
				this.response.send_200_OK();
					
				System.out.println(person +" added to DB");
				break;	
				
			case "DELETE":  //Update Person Record
				db_handler.deletePerson(index);  //No questions asked :-)
				this.response.send_200_OK();
				break;
			
			default:
				//send method not supported
				break;
			
			
	}		
		
		
	}

	private void personCollection() throws SQLException {
		switch (this.request.header.get("method")) {
			case "GET":
	
				persons= new ArrayList<Person>();
				persons = db_handler.listPerson("");
				
				this.response.setStatus("200");
			    this.response.setStatusText("OK");
			    this.response.setResponseTextFormat(gson.toJson(persons));
			    
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
