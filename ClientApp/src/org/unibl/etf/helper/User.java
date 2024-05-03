package org.unibl.etf.helper;

import java.util.Random;
import java.util.UUID;

public class User {
	Random random=new Random();
	
    public  int id;
    public String naziv;
	public String adresa;
	public String broj_telefona;
	public String username;
	public String password;
	public String status;
	public Boolean obrisan;
	
	public User() {}
	
	public User(String naziv, String adresa, String broj_telefona, String username, String password,String status,Boolean obrisan) {
		super();
		this.id=(int) random.nextInt(10000000);
		this.naziv = naziv;
		this.adresa = adresa;
		this.broj_telefona = broj_telefona;
		this.username = username;
		this.password = password;
		this.status=status;
		this.obrisan=obrisan;
	}
	
	public User(User user) {
		
		this.naziv = user.naziv;
		this.adresa = user.adresa;
		this.broj_telefona = user.broj_telefona;
		this.username = user.username;
		this.password = user.password;
		this.status=user.status;
		this.obrisan=user.obrisan;
		
	}

	public int getId() {
		return this.id;
	}

    public void setId(int id)
    {
    	this.id=id;
    }
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBroj_telefona() {
		return broj_telefona;
	}

	public void setBroj_telefona(String broj_telefona) {
		this.broj_telefona = broj_telefona;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Boolean isObrisan()
	{
		return this.obrisan;
	}
	
	public void setObrisan(Boolean obrisan) {
		this.obrisan=obrisan;
	}

	
	
}
