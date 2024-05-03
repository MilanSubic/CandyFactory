package org.unibl.etf.helper;

public class LoginRequest {

	public String username;
	public String password;
	
	public LoginRequest() {}
	
	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username=username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password)
	{
		this.password=password;
	}
	
}

