package org.unibl.etf.rest.client.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.User;
import org.unibl.etf.model.UsersService;
import org.unibl.etf.model.Proizvod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class KorisniciController implements Initializable{
	
	public UsersService usersService=new UsersService();
	
	@FXML
	Button backButton;
	
	@FXML
	Button deleteButton;
	
	@FXML
	Button blockButton;
	
	@FXML
	Button aktivirajButton;
	
	@FXML
	Button odbijButton;
	@FXML
	TableView<User> tableView;
	
	
	@FXML
	TableColumn<User,String> naziv;
	@FXML
	TableColumn<User,String> adresa;
	@FXML
	TableColumn<User,String> broj_telefona;
	@FXML
	TableColumn<User,String> status;
	
	 public void handleBackButtonAction() throws IOException {

	        Parent secondPage= FXMLLoader.load(getClass().getResource("fabrikaGui.fxml"));
	        Stage window= (Stage) backButton.getScene().getWindow();
	        window.setTitle("FabrikaApp");

	        window.setScene(new Scene(secondPage, 600, 400));

	    }
	 
	 public void handleDeleteButtonAction() {
		 if(tableView.getSelectionModel().getSelectedItem()==null)
			{
				Alert alert=new Alert(AlertType.ERROR);
	    		alert.setHeaderText("Greška pri brisanju korisnika!");
	    		alert.setContentText("Selektujte korisnika čiji nalog želite obrisati!");
	    		alert.show();
			}
			else {
				try {
		            User user=tableView.getSelectionModel().getSelectedItem();
		            ObjectMapper objectMapper = new ObjectMapper();
		            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		            File jsonFile = new File(UsersService.USERS_DATA_FILE);

		            if (!jsonFile.exists()) {
		                throw new IOException("JSON file not found.");
		            }

		            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);

		            List<User> existingDataList = objectMapper.readValue(jsonFile, listType);

		            
		            for(User u:existingDataList)
		            {
		            	if(u.getId()==user.getId())
		            	{
		            		
			            			u.setStatus("obrisan");
				            		objectMapper.writeValue(jsonFile, existingDataList);
						            
						            Alert alert=new Alert(AlertType.CONFIRMATION);
						    		alert.setHeaderText("Uspješno ste obrisali nalog korisnika!");
						    		alert.show();
						    		refreshTable();
				    		
				    		
		            	}
		            	
		            }
		    		

		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }

				 
			}
	 }
	 
	 public void handleBlockButtonAction() {
		 if(tableView.getSelectionModel().getSelectedItem()==null)
			{
				Alert alert=new Alert(AlertType.ERROR);
	    		alert.setHeaderText("Greška pri blokiranju korisnika!");
	    		alert.setContentText("Selektujte korisnika čiji nalog želite blokirati!");
	    		alert.show();
			}
			else {
				try {
		            User user=tableView.getSelectionModel().getSelectedItem();
		            ObjectMapper objectMapper = new ObjectMapper();
		            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		            File jsonFile = new File(UsersService.USERS_DATA_FILE);

		            if (!jsonFile.exists()) {
		                throw new IOException("JSON file not found.");
		            }

		            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);

		            List<User> existingDataList = objectMapper.readValue(jsonFile, listType);

		            
		            for(User u:existingDataList)
		            {
		            	if(u.getId()==user.getId())
		            	{
		            		if(user.getStatus().equals("registrovan"))
			            	{
			            			u.setStatus("blokiran");
				            		objectMapper.writeValue(jsonFile, existingDataList);
						            
						            Alert alert=new Alert(AlertType.CONFIRMATION);
						    		alert.setHeaderText("Uspješno ste blokirali nalog korisnika!");
						    		alert.show();
						    		refreshTable();
				    		}
		            		else
		            		{
		            			    Alert alert=new Alert(AlertType.ERROR);
						    		alert.setHeaderText("Ne možete blokirati nalog koji nije aktivan!");
						    		alert.show();	
		            		}
				    		
		            	}
		            	
		            }
		    		

		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }

				 
			}
	 }
	 
	 public void handleAktivirajButtonAction() {
		 
			if(tableView.getSelectionModel().getSelectedItem()==null)
			{
				Alert alert=new Alert(AlertType.ERROR);
	    		alert.setHeaderText("Greška pri aktiviranju korisnika!");
	    		alert.setContentText("Selektujte korisnika čiji nalog želite aktivirati!");
	    		alert.show();
			}
			else {
				try {
		            User user=tableView.getSelectionModel().getSelectedItem();
		            ObjectMapper objectMapper = new ObjectMapper();
		            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		            File jsonFile = new File(UsersService.USERS_DATA_FILE);

		            if (!jsonFile.exists()) {
		                throw new IOException("JSON file not found.");
		            }

		            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);

		            List<User> existingDataList = objectMapper.readValue(jsonFile, listType);

		            
		            for(User u:existingDataList)
		            {
		            	if(u.getId()==user.getId())
		            	{
		            		if(user.getStatus().equals("neregistrovan"))
			            	{
			            			u.setStatus("registrovan");
				            		objectMapper.writeValue(jsonFile, existingDataList);
						            
						            Alert alert=new Alert(AlertType.CONFIRMATION);
						    		alert.setHeaderText("Uspješno ste aktivirali nalog korisnika!");
						    		alert.show();
						    		refreshTable();
				    		}
		            		else
		            		{
		            			    Alert alert=new Alert(AlertType.ERROR);
						    		alert.setHeaderText("Ne možete aktivirati nalog koji je već obrađen!");
						    		alert.show();	
		            		}
				    		
		            	}
		            	
		            }
		    		

		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }

				 
			}
	 }
	 
	 
	 public void handleOdbijButtonAction() {
		 if(tableView.getSelectionModel().getSelectedItem()==null)
			{
				Alert alert=new Alert(AlertType.ERROR);
	    		alert.setHeaderText("Greška pri odbijanju korisnika!");
	    		alert.setContentText("Selektujte korisnika čiji zahtjev ne želite aktivirati!");
	    		alert.show();
			}
			else {
				try {
		            User user=tableView.getSelectionModel().getSelectedItem();
		            ObjectMapper objectMapper = new ObjectMapper();
		            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		            File jsonFile = new File(UsersService.USERS_DATA_FILE);

		            if (!jsonFile.exists()) {
		                throw new IOException("JSON file not found.");
		            }

		            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);

		            List<User> existingDataList = objectMapper.readValue(jsonFile, listType);

		            
		            for(User u:existingDataList)
		            {
		            	if(u.getId()==user.getId())
		            	{
		            		if(user.getStatus().equals("neregistrovan"))
			            	{
			            			u.setStatus("odbijen");
				            		objectMapper.writeValue(jsonFile, existingDataList);
						            
						            Alert alert=new Alert(AlertType.CONFIRMATION);
						    		alert.setHeaderText("Uspješno ste odbili zahtjev korisnika!");
						    		alert.show();
						    		refreshTable();
				    		}
		            		else
		            		{
		            			    Alert alert=new Alert(AlertType.ERROR);
						    		alert.setHeaderText("Ne možete odbiti zahtjev koji je već obrađen!");
						    		alert.show();	
		            		}
				    		
		            	}
		            	
		            }
		    		

		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }

				 
			}
		
	 }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		refreshTable();
	}

	public void refreshTable() {
		List<User> users=usersService.userList();
		
		
		naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		adresa.setCellValueFactory(new PropertyValueFactory<>("adresa"));
		broj_telefona.setCellValueFactory(new PropertyValueFactory<>("broj_telefona"));
		status.setCellValueFactory(new PropertyValueFactory<>("status"));
		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		List<User> activeUsers=new ArrayList<>();
		for(User u:users)
		{
			if(!u.getStatus().equals("obrisan"))
			{
				activeUsers.add(u);
			}
				
		}
		ObservableList<User> lista = FXCollections.observableArrayList(activeUsers);
		tableView.setItems(lista);
	}

	

}
