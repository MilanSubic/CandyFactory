package org.unibl.etf.rest.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

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

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.unibl.etf.helper.CustomLogger;
import org.unibl.etf.helper.User;
import org.unibl.etf.helper.LoginRequest;

public class KorisnikGUI extends Application {
	
	//private static final String BASE_STRING = "http://localhost:8080/FactoryRESTServer/";
	
	@FXML
	public Button prijaviButton;
	@FXML
	Button registrujButton;
	@FXML
	public TextField usernameTextField;
	@FXML
	PasswordField passwordTextField;
	
	
    public static void main(String[] args) {
    	MulticastClientThread mt=new MulticastClientThread();
    	mt.start();
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("KorisnikApp");
        
 
  
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("login.fxml"));
			primaryStage.setScene(new Scene(root, 400, 400));
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 CustomLogger.setKorisnikLogging();
			 CustomLogger.logError("Exception", e); 
		}
      
        
      
    }
    
    public void handlePrijaviButtonAction() {
    	
    		ClientConfig clientConfig =  new ClientConfig().register( LoggingFilter.class );
    		Client client = ClientBuilder.newClient(clientConfig);
    		client.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
    		
    		WebTarget webTarget = client.target(getBaseURI()).path("rest").path("login");
    		LoginRequest loginRequest=new LoginRequest(usernameTextField.getText(),passwordTextField.getText());
    		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
    		Response response = invocationBuilder.post(Entity.entity(loginRequest, MediaType.APPLICATION_JSON));
    		response.bufferEntity();
    		User user=new User(response.readEntity(User.class));
    			if(response.getStatus()==200 && user.naziv!=null && user.status.equals("registrovan"))
    			{
    				System.out.println("Korisnik je logovan");
    			//	User user=new User(response.readEntity(User.class));
    				System.out.println(user.naziv+" "+user.adresa);
    				//
    				//move to new page
    				
					FXMLLoader loader=new FXMLLoader();
					loader.setLocation(getClass().getResource("korisnikGui.fxml"));
					try{
					    loader.load();
					}catch(Exception e){
						CustomLogger.setKorisnikLogging();
					    CustomLogger.logError("Exception", e); 
					 }
					KorisnikController userController=loader.getController();
					userController.setUser(user);
					Parent p=loader.getRoot();  
					Stage window=(Stage) prijaviButton.getScene().getWindow();
					window.setScene(new Scene(p,750,500));
					window.setTitle("KorisnikApp");
					window.show();
    			      
    				
    			}else
    			{
    				Alert alert=new Alert(AlertType.ERROR);
    				alert.setHeaderText("Prijava nije moguća");
    				alert.setContentText("Provjerite korisničko ime i lozinku");
    				alert.show();
    				usernameTextField.clear();
    				passwordTextField.clear();
    			}
    		
    	
    	
    }
    
    
	 public void handleRegistrujButtonAction() throws IOException {
		    Parent secondPage= FXMLLoader.load(getClass().getResource("registracija.fxml"));
	        Stage window= (Stage) registrujButton.getScene().getWindow();
	        window.setTitle("KorisnikApp");

	        window.setScene(new Scene(secondPage, 400, 400));
	}

	 public URI getBaseURI() {
			Properties prop;
			prop = new Properties();
			try {
				prop.load(new FileInputStream(new File("clientApp.properties")));
			} catch (FileNotFoundException e) {
			 	 CustomLogger.setKorisnikLogging();
				 CustomLogger.logError("Exception", e); 
			} catch (IOException e) {
			 	 CustomLogger.setKorisnikLogging();
				 CustomLogger.logError("Exception", e); 
			}
			return UriBuilder.fromUri(prop.getProperty("BASE_STRING")).build();
		}
}
