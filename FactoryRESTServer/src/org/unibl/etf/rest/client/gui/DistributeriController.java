package org.unibl.etf.rest.client.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.rmi.DistributerRMIInterface;

public class DistributeriController implements Initializable {

	public static final String PATH = "resources";

	private static final String REGISTRY_NAME = "distributeri";
	
	public List<String> distributeri=new ArrayList<>();
	DistributerRMIInterface rmiInterface;
	
	
	@FXML
	TableView<String> tableView;
	@FXML
	TableColumn<String,String> naziv;
	@FXML
	Button backButton;
	@FXML
	Button prikaziButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.setProperty("java.security.policy", PATH + File.separator + "client_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
		
			Registry registry = LocateRegistry.getRegistry(1099);
		    rmiInterface = (DistributerRMIInterface) registry.lookup(REGISTRY_NAME);
			distributeri=rmiInterface.getAllDistributors();
			
			
			ObservableList<String> data = FXCollections.observableArrayList(distributeri);
			naziv.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
			tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tableView.setItems(data);
		}
		 catch (Exception e) {
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
			}
		
	}
	
	public void handlePrikaziButtonAction() throws RemoteException {
		 ObservableList<String> selectedDistributer = tableView.getSelectionModel().getSelectedItems();
		// System.out.println(selectedDistributer.get(0));
		 
		 if(selectedDistributer!=null)
		 {
			
			
			    Parent p;
		        FXMLLoader loader=new FXMLLoader();
				loader.setLocation(getClass().getResource("distributeriProizvodi.fxml"));
				DistributeriProizvodiController distributeriProizvodiController = new DistributeriProizvodiController();
				distributeriProizvodiController.setDistributer(selectedDistributer.get(0));

				loader.setController(distributeriProizvodiController);
				try{
					
				     	p = loader.load();
				     	Stage window=(Stage) prikaziButton.getScene().getWindow();
						window.setScene(new Scene(p,700,500));
						window.setTitle("Rad sa distributerima");
						window.show();
				   
				}catch(Exception e){
					  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
					}
			
		 }
		 
		
	}
	
	public void handleBackButtonAction() throws IOException {
		
		    Parent secondPage= FXMLLoader.load(getClass().getResource("fabrikaGui.fxml"));
	        Stage window= (Stage) backButton.getScene().getWindow();
	        window.setTitle("FabrikaApp");

	        window.setScene(new Scene(secondPage, 600, 400));
	}

	
}
