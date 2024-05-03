package org.unibl.etf.helper;

public class RegistrationRequest {
	
		
	    public String naziv;
		public String adresa;
		public String broj_telefona;
		public String username;
		public String password;
		public String password2;
		
		
		public RegistrationRequest() {
			
		}
		
		public RegistrationRequest(String naziv, String adresa, String broj_telefona, String username,
				String password, String password2) {
			super();
			
			this.naziv = naziv;
			this.adresa = adresa;
			this.broj_telefona = broj_telefona;
			this.username = username;
			this.password = password;
			this.password2 = password2;
			
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

		public String getPassword2() {
			return password2;
		}

		public void setPassword2(String password2) {
			this.password2 = password2;
		}

	

		
		
		
		

}
