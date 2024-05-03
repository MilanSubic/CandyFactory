package org.unibl.etf.rest.client.gui;

import java.io.IOException;
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

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.*;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.Proizvod;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class DistributeriNarudzba implements Initializable{
	
	 private final static String QUEUE_NAME = "myQueue";

	public List<Proizvod> selected=new ArrayList<>();

	
	public void setList(List<Proizvod> selected)
	{
		this.selected.addAll(selected);
		for(int i=0;i<selected.size();i++)
		{
			gridPane.add(new Label(selected.get(i).getNaziv()), 0, i);
			gridPane.add(new Label("Željena količina: "), 1, i);
			gridPane.add(new TextField(), 2, i);
		}
		
	
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
		 
		
	      Stage window=(Stage) backButton.getScene().getWindow();
	      window.close();
	      

	    }
	 
	 public void handlePotvrdiButtonAction() throws IOException {
		
		
		try {
			List<Proizvod> products=new ArrayList<>();
			 int size=gridPane.getChildren().size();
			 int j=0;
			 for(int i=0;i<size-1;i+=3)
			 {
				 products.add(new Proizvod(selected.get(j).naziv,selected.get(j).tipSlatkisa,selected.get(j).okus,selected.get(j).boja,Integer.valueOf(((TextField)gridPane.getChildren().get(i+2)).getText())));
				 j++;
			 }
			 
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

			  try (Jedis jedis = jedisPool.getResource()) {
			            
			            
			            Map<String,String> map = new HashMap<>();
			        	
			            for(Proizvod p:products)
			            {
			            
			            	map.put("id", String.valueOf(p.getId()));
			            	map.put("naziv", p.getNaziv());
			            	map.put("tipSlatkisa", p.getTipSlatkisa());
			            	map.put("boja",p.getBoja());
			            	map.put("okus",p.getOkus());
			            	map.put("kolicina", String.valueOf(p.getKolicina()));
			            	            	
			            	jedis.hmset("20_"+String.valueOf(p.getId()), map);
			            }
			            
			           
			        } catch (Exception e) {
			        	  CustomLogger.setFabrikaLogging();
						  CustomLogger.logError("Exception", e); 
			        }
			       

			        
			        jedisPool.close();
				
			    Alert alert =new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Uspješno ste naručili nove proizvode!");
				alert.show();
				handleBackButtonAction();
				
			
		}catch(NumberFormatException e) {
			Alert alert =new Alert(AlertType.ERROR);
			alert.setHeaderText("Greška pri naručivanju proizvoda!");
			alert.setContentText("Željena vrijednost proizvoda mora biti broj!");
			alert.show();
					
		}
		
		 
	 }
}
