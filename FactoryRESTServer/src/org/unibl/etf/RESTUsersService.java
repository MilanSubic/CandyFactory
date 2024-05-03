
package org.unibl.etf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class RESTUsersService {
	private Users list = new Users();

	{
		//list.getUsersList().add(0, new User(0, "ABC Inc","123 Main Street, City, Country","+1234567890","user1","password1","registrovan",false));
		//list.getUsersList().add(0, new User(0, "ABC Inc","123 Main Street, City, Country","+12345678545","user2","password2","registrovan",false));
		//list.getUsersList().add(0, new User(0, "ABC Inc","123 Main Street, City, Country","+534567890","user3","password3","registrovan",false));
	
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Users getAllStudents() {
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{user_id}")
	public User getStudent(@PathParam("user_id") int id) {
		if (id < list.getUsersList().size()) {
			return list.getUsersList().get(id);
		}
		return new User();
	}
}
