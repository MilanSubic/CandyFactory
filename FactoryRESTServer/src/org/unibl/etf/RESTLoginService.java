package org.unibl.etf;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/login")
public class RESTLoginService {
	
	UsersService service;
	
	public RESTLoginService() {
		this.service=new UsersService();
	}
	
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User checkLogin(LoginRequest request ) {
		if (service.validateLogin(request)) {
			return service.getByUsername(request.username);
		} else {
			return new User();
		}
	}
	
	
	
}
