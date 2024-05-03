package org.unibl.etf.rest.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;



import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.glassfish.jersey.client.ClientConfig;
import org.unibl.etf.helper.CustomLogger;
import org.unibl.etf.helper.User;

public class KorisnikController implements Initializable {
	
	//private static final String BASE_STRING = "http://localhost:8080/FactoryRESTServer/";

	public User user;
	
	@FXML
	Button backButton;
	
	@FXML
	Button naruciButton;
	
	@FXML
	Label usernameLabel;
	
	@FXML
	TableView<Proizvod> tableView;
	
	@FXML
	TableColumn<Proizvod,String> naziv;
	@FXML
	TableColumn<Proizvod,String> tipSlatkisa;
	@FXML
	TableColumn<Proizvod,String> boja;
	@FXML
	TableColumn<Proizvod,String> okus;
	@FXML
	TableColumn<Proizvod,Integer> kolicina;
	
	
	
	public void setUser(User user) {
		this.user=user;
		this.usernameLabel.setText(user.getUsername());
	}
	
	

	
	 public void handleBackButtonAction() throws IOException {

	        Parent secondPage= FXMLLoader.load(getClass().getResource("login.fxml"));
	        Stage window= (Stage) backButton.getScene().getWindow();
	        window.setTitle("KorisnikApp");

	        window.setScene(new Scene(secondPage, 400, 400));

	    }
	 
	 public void handleNaruciButtonAction() {
		 System.out.println("naruci");
		 ObservableList<Proizvod> selectedItems = tableView.getSelectionModel().getSelectedItems();
		 if(selectedItems.size()!=0)
		 {
			 List<Proizvod> regularList = new ArrayList<>(selectedItems);
		 
 		 
		    FXMLLoader loader=new FXMLLoader();
			loader.setLocation(getClass().getResource("narudzba.fxml"));
			try{
			    loader.load();
			}catch(Exception e){
				 CustomLogger.setKorisnikLogging();
				 CustomLogger.logError("Exception", e); 
			}
			NarudzbaController narudzbaController=loader.getController();
			narudzbaController.setList(regularList);
			Parent p=loader.getRoot();  
			Stage window=(Stage) naruciButton.getScene().getWindow();
			window.setScene(new Scene(p,600,400));
			window.setTitle("Narudzba");
			window.show();
		 }
	 }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		
			
		 	ClientConfig clientConfig = new ClientConfig();
			Client client = ClientBuilder.newClient(clientConfig);
			WebTarget webTarget = client.target(getBaseURI()).path("rest").path("proizvodi");
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			List<Proizvod> proizvodi =parseJsonToProizvodList(response.readEntity(String.class));
		
			
			
			
				ObservableList<Proizvod> data = FXCollections.observableArrayList(proizvodi);
				//id.setCellValueFactory(new PropertyValueFactory<>("id"));
				naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
				tipSlatkisa.setCellValueFactory(new PropertyValueFactory<>("tipSlatkisa"));
				boja.setCellValueFactory(new PropertyValueFactory<>("boja"));
				okus.setCellValueFactory(new PropertyValueFactory<>("okus"));
				kolicina.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
				tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				tableView.setItems(data);
				
		
		
	    }
	
	public List<Proizvod> parseJsonToProizvodList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Proizvod> proizvodi = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Proizvod.class));
            return proizvodi;
        } catch (Exception e) {
       	 CustomLogger.setKorisnikLogging();
		 CustomLogger.logError("Exception", e); 
            return null; 
        }
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
	 
	 

