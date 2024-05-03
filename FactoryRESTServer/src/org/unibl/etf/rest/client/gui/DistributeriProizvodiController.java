package org.unibl.etf.rest.client.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.rmi.DistributerRMIInterface;
import org.unibl.etf.model.*;
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

public class DistributeriProizvodiController implements Initializable {
	
	
	public String distributer;
	List<Proizvod> products=new ArrayList<>();
	
    public static final String PATH = "resources";
	
	
	DistributerRMIInterface rmiInterface;
	
	
	public DistributeriProizvodiController() {}
	
	public void setDistributer(String distributer)
	{
		this.distributer=distributer;
	}
	
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
	Button naruciButton;
	@FXML
	Button backButton;
	


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		System.setProperty("java.security.policy", PATH + File.separator + "client_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			String name = "distributeri";
			Registry registry = LocateRegistry.getRegistry(1099);
		    rmiInterface = (DistributerRMIInterface) registry.lookup(name);
			List<String> proizvodi=rmiInterface.getDistributorProducts(distributer);
			
			if(proizvodi.size()!=0)
			{
				for(String proizvod:proizvodi) {
				String[] parts = proizvod.split("#");
                products.add(new Proizvod(parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4])));
				
				}
			
				ObservableList<Proizvod> data = FXCollections.observableArrayList(products);
				
				naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
				tipSlatkisa.setCellValueFactory(new PropertyValueFactory<>("tipSlatkisa"));
				boja.setCellValueFactory(new PropertyValueFactory<>("boja"));
				okus.setCellValueFactory(new PropertyValueFactory<>("okus"));
				kolicina.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
				tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				tableView.setItems(data);
				
			}
			
			
		}catch(Exception e) {
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
		}
	}
	
	
	public void handleNaruciButtonAction() {
		 ObservableList<Proizvod> selectedItems = tableView.getSelectionModel().getSelectedItems();
		 List<Proizvod> regularList = new ArrayList<>(selectedItems);
 		 
		 FXMLLoader loader=new FXMLLoader();
			loader.setLocation(getClass().getResource("distributeriNarudzba.fxml"));
			try{
			    loader.load();
			}catch(Exception e){
				  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
			}
			DistributeriNarudzba controller=loader.getController();
			controller.setList(regularList);
			Parent p=loader.getRoot();  
			//Stage window=(Stage) naruciButton.getScene().getWindow();
			Stage window=new Stage();
			window.setScene(new Scene(p,600,400));
			window.setTitle("Rad sa distributerima");
			window.show();
		
	}
	
	public void handleBackButtonAction() throws IOException {
		

	    Parent secondPage= FXMLLoader.load(getClass().getResource("distributeri.fxml"));
        Stage window= (Stage) backButton.getScene().getWindow();
        window.setTitle("Rad sa distributerima");
        window.setScene(new Scene(secondPage, 600, 400));
		
	}

}
