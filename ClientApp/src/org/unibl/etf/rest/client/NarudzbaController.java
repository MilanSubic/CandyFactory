package org.unibl.etf.rest.client;


import java.net.URL;
import java.util.concurrent.TimeoutException;
import java.util.*;
import java.io.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.unibl.etf.helper.CustomLogger;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class NarudzbaController implements Initializable{
	
	// private final static String QUEUE_NAME = "myQueue";

	public List<Proizvod> selected=new ArrayList<>();
	
	public void setList(List<Proizvod> selected)
	{
		this.selected.addAll(selected);
		for(int i=0;i<selected.size();i++)
		{
			gridPane.add(new Label(selected.get(i).getNaziv()), 0, i);
			gridPane.add(new Label("Dostupna količina: "+String.valueOf(selected.get(i).getKolicina())), 1, i);
			gridPane.add(new Label("Poručena količina:"), 2, i);
			gridPane.add(new TextField(), 3, i);
		}
		gridPane.add(new Label("Unesite email:"), 0, selected.size());
		gridPane.add(new TextField(), 1, selected.size());
	
	}
	
	@FXML
	AnchorPane anchorPane;
	@FXML
	GridPane gridPane;
	
	@FXML
	Button backButton;
	
	@FXML
	Button potvrdiButton;
	
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		System.out.println(selected.size());
		//gridPane.add(new Label("fdfdf"), 1, 0);
		
		
	}
	
	 public void handleBackButtonAction() throws IOException {

	        Parent secondPage= FXMLLoader.load(getClass().getResource("korisnikGui.fxml"));
	        Stage window= (Stage) backButton.getScene().getWindow();
	        window.setTitle("KorisnikApp");

	        window.setScene(new Scene(secondPage, 750, 500));
	      

	    }
	 
	 public void handlePotvrdiButtonAction() throws IOException, TimeoutException, JAXBException {
		 
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
		
		    Connection connection = ConnectionFactoryUtil.createConnection();
			Channel channel = connection.createChannel();
			Order order;
			channel.queueDeclare( prop.getProperty("QUEUE_NAME"), false, false, false, null);	
			 int size=gridPane.getChildren().size();
			 for(int i=0;i<size-3;i+=4)
					
				{
				    order = new Order(((Label)gridPane.getChildren().get(i)).getText(),Integer.valueOf(((Label)gridPane.getChildren().get(i+1)).getText().substring(19)),Integer.valueOf(((TextField)gridPane.getChildren().get(i+3)).getText()),((TextField)gridPane.getChildren().get(size-1)).getText());
					//System.out.println(order.getNaziv()+order.getUkupanBroj()+order.getNaruceniBroj());
					JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
				    Marshaller marshaller = jaxbContext.createMarshaller();

				     
				    StringWriter xmlMessage = new StringWriter();
				    marshaller.marshal(order, xmlMessage);

				    System.out.println(xmlMessage.toString());
						
				    channel.basicPublish("",   prop.getProperty("QUEUE_NAME"), null, xmlMessage.toString().getBytes("UTF-8"));
				    
				}
			 Alert alert=new Alert(AlertType.CONFIRMATION);
			 alert.setHeaderText("Uspješno ste naručili proizvode!");
			 alert.show();

	       
			channel.close();
			connection.close();
			handleBackButtonAction();
		
		
		 
	 }
}
