package org.unibl.etf.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Order {
	
	public String naziv;
	public int ukupanBroj;
	public int naruceniBroj;
	public String email;
	
	public Order() {}
	public Order(String naziv, int ukupanBroj, int naruceniBroj,String email) {
		super();
		this.naziv = naziv;
		this.ukupanBroj = ukupanBroj;
		this.naruceniBroj = naruceniBroj;
		this.email=email;
	}
	
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public void setUkupanBroj(int ukupanBroj) {
		this.ukupanBroj = ukupanBroj;
	}
	
	public void setNaruceniBroj(int naruceniBroj) {
		this.naruceniBroj = naruceniBroj;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	

	

}
