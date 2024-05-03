package org.unibl.etf.rmi.client;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.*;
import java.lang.*;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.Proizvod;

import javafx.application.Application;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

 



public class DistributerController implements Initializable {
	
	public DistributerController() {}
	
	
public static final String DISTRIBUTORS_PATH = "C:\\Users\\HP\\eclipse-workspace\\DistributerApp\\src\\org\\unibl\\etf\\rmi\\distributors\\";

 public String nazivFirme;
 public boolean postoji=false;

@FXML
TextField nazivTextField;
@FXML
TextField tipTextField;
@FXML
TextField okusTextField;
@FXML
TextField bojaTextField;
@FXML
TextField kolicinaTextField;

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

@FXML
Button kreirajButton;
@FXML
Button backButton;

@FXML
Label firmaLabel=new Label();
	

 
	public void setFirma(String text) {
		
		nazivFirme=text;
		
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		firmaLabel.setText(nazivFirme);
		postoji=false;
	List<Proizvod> proizvodi=new ArrayList<>();
    List<String> distributors = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(DISTRIBUTORS_PATH+"distributors.txt"))) {
			
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	distributors.add(line);
		    }
		    
		} catch (IOException e) {
			  CustomLogger.setDistributerLogging();
			  CustomLogger.logError("Exception", e); 
		}
	
		for(String firma : distributors)
		{
			
			
			if(firma.equals(nazivFirme))
			{
				postoji=true;
				
				List<String> products = new ArrayList<>();
				
				try (BufferedReader reader = new BufferedReader(new FileReader(DISTRIBUTORS_PATH+ nazivFirme + ".txt"))) {
					
				    String line;
				    while ((line = reader.readLine()) != null) {
				    	products.add(line);
				    	System.out.println(line);
				    	if(!line.equals(""))
			            {
				    		String[] parts = line.split("#");
			                proizvodi.add(new Proizvod(parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4])));
			            }
				    }
				   
				    
				} catch (IOException e) {
					  CustomLogger.setDistributerLogging();
					  CustomLogger.logError("Exception", e); 
				}
				
				System.out.println(proizvodi.size());
				
				ObservableList<Proizvod> data = FXCollections.observableArrayList(proizvodi);
				
				naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
				tipSlatkisa.setCellValueFactory(new PropertyValueFactory<>("tipSlatkisa"));
				boja.setCellValueFactory(new PropertyValueFactory<>("boja"));
				okus.setCellValueFactory(new PropertyValueFactory<>("okus"));
				kolicina.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
				tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				tableView.setItems(data);
				
			}
			
				
				
				
			
			
			
		}
		if(!postoji)
		{
			try {
				PrintWriter pw=new PrintWriter(new FileWriter(new File(DISTRIBUTORS_PATH+"distributors.txt"),true));
				pw.write(nazivFirme+"\n");
				pw.close();
			} catch (IOException e) {
				  CustomLogger.setDistributerLogging();
				  CustomLogger.logError("Exception", e); 
			}
		}
		
	}
	
	
	
	
	 public void handleKreirajButtonAction() {
		 
	    	System.out.println("kreiraj");
	    	try {
		    	if(!nazivTextField.getText().equals("") && !tipTextField.getText().equals("") && !okusTextField.getText().equals("") && !bojaTextField.getText().equals("") && !kolicinaTextField.getText().equals("") && Integer.valueOf(kolicinaTextField.getText())!=0 )
		    	{
		    		if(postoji)
		    		{
		    			System.out.println("postoji txt");
		    			
		    			try {
		    				PrintWriter pw=new PrintWriter(new FileWriter(new File(DISTRIBUTORS_PATH + nazivFirme + ".txt"),true));
		    				pw.write(nazivTextField.getText()+"#"+tipTextField.getText()+"#"+okusTextField.getText()+"#"+bojaTextField.getText()+"#"+kolicinaTextField.getText()+"\n");
		    				pw.close();
		    				refreshTable();
		    			} catch (IOException e) {
		    				  CustomLogger.setDistributerLogging();
		    				  CustomLogger.logError("Exception", e); 
		    			} 
		    		
		    		}
		    		else
		    		{
		    			System.out.println("ne postoji txt");
		    			try {
		    				PrintWriter pw=new PrintWriter(new FileWriter(new File(DISTRIBUTORS_PATH + nazivFirme + ".txt"),false));
		    				pw.write(nazivTextField.getText()+"#"+tipTextField.getText()+"#"+okusTextField.getText()+"#"+bojaTextField.getText()+"#"+kolicinaTextField.getText()+"\n");
		    				pw.close();
		    				refreshTable();
		    			} catch (IOException e) {
		    				  CustomLogger.setDistributerLogging();
		    				  CustomLogger.logError("Exception", e); 
		    			}
		    			
		    		}
		    		Alert alert=new Alert(AlertType.CONFIRMATION);
		    		alert.setHeaderText("Uspješno ste kreirali proizvod!");
		    		alert.show();
		    	}
		    	else {
		    		Alert alert=new Alert(AlertType.ERROR);
		    		alert.setHeaderText("Greška pri kreiranju proizvoda!");
		    		alert.setContentText("Da bi kreirali proizvod sva polja moraju biti popunjena!");
		    		alert.show();
		    	}
	    	}catch(NumberFormatException e) {
	    		Alert alert=new Alert(AlertType.ERROR);
	    		alert.setHeaderText("Greška pri kreiranju proizvoda!");
	    		alert.setContentText("Vrijednost količina mora biti broj!");
	    		alert.show();
	    			}
	    	
	 }
	
	
	
	 public void refreshTable() {
		 List<String> products = new ArrayList<>();
		 List<Proizvod> proizvodi=new ArrayList<>();
			
			try (BufferedReader reader = new BufferedReader(new FileReader(DISTRIBUTORS_PATH+ nazivFirme + ".txt"))) {
				
			    String line;
			    while ((line = reader.readLine()) != null) {
			    	products.add(line);
			    	System.out.println(line);
			    	if(!line.equals(""))
		            {
			    		String[] parts = line.split("#");
		            
		                proizvodi.add(new Proizvod(parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4])));
		            }
			    }
			   
			    
			} catch (IOException e) {
				  CustomLogger.setDistributerLogging();
				  CustomLogger.logError("Exception", e); 
			}
			
			
			ObservableList<Proizvod> data = FXCollections.observableArrayList(proizvodi);
	
			naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
			tipSlatkisa.setCellValueFactory(new PropertyValueFactory<>("tipSlatkisa"));
			boja.setCellValueFactory(new PropertyValueFactory<>("boja"));
			okus.setCellValueFactory(new PropertyValueFactory<>("okus"));
			kolicina.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
			tableView.setItems(data);
		}
	
	
	
	
	
	
	
	
	
	   public void handleBackButtonAction() throws IOException {
    	   Parent secondPage= FXMLLoader.load(getClass().getResource("distributerLogin.fxml"));
	        Stage window= (Stage) backButton.getScene().getWindow();
	        window.setTitle("DistributerApp");

	        window.setScene(new Scene(secondPage, 400, 400));

    	
    }
}