package org.unibl.etf.rest.client;
import java.util.*;
public class Proizvod {
	Random random=new Random();
	public int id;
	public String naziv;
	public String tipSlatkisa;
	public String boja;
	public String okus;
	public int kolicina;
	
	public Proizvod() {}

	public Proizvod(int id, String naziv, String tipSlatkisa, String boja, String okus, int kolicina) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.tipSlatkisa = tipSlatkisa;
		this.boja = boja;
		this.okus = okus;
		this.kolicina = kolicina;
	}
	
	public Proizvod(String naziv, String tipSlatkisa, String boja, String okus, int kolicina) {
		super();
		this.id=(int) random.nextInt(10000000);
		this.naziv = naziv;
		this.tipSlatkisa = tipSlatkisa;
		this.boja = boja;
		this.okus = okus;
		this.kolicina = kolicina;
	}

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

	public String getTipSlatkisa() {
		return tipSlatkisa;
	}

	public void setTipSlatkisa(String tipSlatkisa) {
		this.tipSlatkisa = tipSlatkisa;
	}

	public String getBoja() {
		return boja;
	}

	public void setBoja(String boja) {
		this.boja = boja;
	}

	public String getOkus() {
		return okus;
	}

	public void setOkus(String okus) {
		this.okus = okus;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	

}
