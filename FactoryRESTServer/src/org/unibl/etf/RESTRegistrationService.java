package org.unibl.etf;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/register")
public class RESTRegistrationService {
	
	UsersService service;
	
	public RESTRegistrationService() {
		this.service=new UsersService();
	}
	
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String registerUser(RegistrationRequest request ) {
		if(request.password.equals(request.password2))
		{
			User user=new User(request.naziv,request.adresa,request.broj_telefona,request.username,request.password,new String("neregistrovan"),new Boolean(false));
			
				service.appendWriteToJson(user); 
				return new String("Vaš zahjev je poslan, molimo Vas sačekajte dok se odobri.");
		
		}
		return new String("Zahtjev za registraciju je neuspješan!");
	}
	
	
	
}
