package org.unibl.etf.rest.client;



import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.unibl.etf.helper.CustomLogger;
import org.unibl.etf.helper.RegistrationRequest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class RegistracijaController {
	
	private static final String BASE_STRING = "http://localhost:8080/FactoryRESTServer/";
	
	@FXML
	Button registrujButton;
	@FXML
	Button backButton;
	@FXML
	TextField nazivTextField;
	@FXML
	TextField adresaTextField;
	@FXML
	TextField telefonTextField;
	@FXML
	TextField usernameTextField;
	@FXML
	PasswordField passwordTextField;
	@FXML
	PasswordField password2TextField;
	
	public void handleRegistrujButtonAction() {
		
		if(nazivTextField.getText().equals("") || adresaTextField.getText().equals("") || telefonTextField.getText().equals("") || usernameTextField.getText().equals("") || passwordTextField.getText().equals("") || password2TextField.getText().equals(""))
		{
			Alert alert =new Alert(AlertType.ERROR);
			alert.setHeaderText("Greška pri registraciji!");
			alert.setContentText("Morate unijeti sva zahtjevana polja!");
			alert.show();
		}
		else {
		
			ClientConfig clientConfig =  new ClientConfig().register( LoggingFilter.class );
			Client client = ClientBuilder.newClient(clientConfig);
			client.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
			
		
			WebTarget webTarget = client.target(getBaseURI()).path("rest").path("register");
			RegistrationRequest registrationRequest=new RegistrationRequest(nazivTextField.getText(),adresaTextField.getText(),telefonTextField.getText(),usernameTextField.getText(),passwordTextField.getText(),password2TextField.getText());
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.post(Entity.entity(registrationRequest, MediaType.APPLICATION_JSON));
			//System.out.println(response.readEntity(String.class));
			
			if(response.getStatus()==200)
			{
				   Alert a = new Alert(AlertType.CONFIRMATION);
				   a.setHeaderText("Registracija uspješna");
				   a.setContentText(response.readEntity(String.class));
	               a.show();
	               try {
					handleBackButtonAction();
				} catch (IOException e) {
					
					 CustomLogger.setKorisnikLogging();
					 CustomLogger.logError("Exception", e); 
				}
			}
		}
		
	}
	
	
	public void handleBackButtonAction() throws IOException {
	    Parent secondPage= FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage window= (Stage) backButton.getScene().getWindow();
        window.setTitle("KorisnikApp");
        window.setScene(new Scene(secondPage, 400, 400));
		
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				BASE_STRING).build();
	}
	
	
	
	

}
