package org.unibl.etf;

public class FactoryUser {
	
	int id;
	String naziv;
	public FactoryUser(int id, String naziv) {
		super();
		this.id = id;
		this.naziv = naziv;
	}
	
	public FactoryUser() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	
	

}
