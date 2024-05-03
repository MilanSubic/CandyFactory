package org.unibl.etf.rest.client.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.Proizvod;
import org.unibl.etf.model.Proizvodi;

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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.filter.LoggingFilter;

public class ProizvodiController implements Initializable{
	
	private static final String BASE_STRING = "http://localhost:8080/FactoryRESTServer/";

	@FXML
	Button backButton;
	
	@FXML
	Button deleteButton;
	
	@FXML
	Button createButton;
	
	@FXML
	Button izmijeniButton;
	
	@FXML
	Button publishButton;
	
	@FXML
	TextField idTextField;
	
	@FXML
	TextField nazivTextField;
	@FXML
	TextField tipSlatkisaTextField;
	@FXML
	TextField bojaTextField;
	@FXML
	TextField okusTextField;
	@FXML
	TextField kolicinaTextField;
	
	@FXML
	TextField promocijaTextField;
	
	@FXML
	TableView<Proizvod> tableView;
	
	@FXML
	TableColumn<Proizvod,Integer> id;
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
    		alert.setHeaderText("Greška pri brisanju proizvoda!");
    		alert.setContentText("Selektujte proizvod koji želite obrisati!");
    		alert.show();
		}
		else {
		    Proizvod p=tableView.getSelectionModel().getSelectedItem();
			Jedis jedis = new Jedis("localhost"); 
	    

	        
	        if (jedis.exists("20_"+p.getId())) {
	            
	            jedis.del("20_"+p.getId());
	            Alert alert=new Alert(AlertType.CONFIRMATION);
        		alert.setHeaderText("Uspješno brisanje proizvoda!");
        		alert.show();
        		readFromRedisAndRefreshTable();
	        } else {
	        	 	Alert alert=new Alert(AlertType.ERROR);
	        		alert.setHeaderText("Greška pri brisanju proizvoda!");
	        		alert.show();
	        }

	        
	        jedis.close();
		}
	 }
	 
	 public void handleCreateButtonAction() {
		// System.out.println("create");
		
		 JedisPoolConfig poolConfig = new JedisPoolConfig();
	     JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

	        try (Jedis jedis = jedisPool.getResource()) {
	        	 Set<String> productKeys = jedis.keys("20_*");
	        	if(!idTextField.getText().equals("") && !nazivTextField.getText().equals("") && !tipSlatkisaTextField.getText().equals("") && !bojaTextField.getText().equals("")  && !okusTextField.getText().equals("") && !kolicinaTextField.getText().equals("") && !productKeys.contains("20_"+idTextField.getText()))
	            {
	        		Proizvod p=new Proizvod(Integer.valueOf(idTextField.getText()),nazivTextField.getText(),tipSlatkisaTextField.getText(),bojaTextField.getText(),okusTextField.getText(),Integer.valueOf(kolicinaTextField.getText()));
	        		Map<String,String> map = new HashMap<>();
	        	
	            
	            
	            	map.put("id", String.valueOf(p.getId()));
	            	map.put("naziv", p.getNaziv());
	            	map.put("tipSlatkisa", p.getTipSlatkisa());
	            	map.put("boja",p.getBoja());
	            	map.put("okus",p.getOkus());
	            	map.put("kolicina", String.valueOf(p.getKolicina()));
	            	            	
	            	jedis.hmset("20_"+String.valueOf(p.getId()), map);
	            	Alert alert=new Alert(AlertType.CONFIRMATION);
	        		alert.setHeaderText("Uspješno kreiranje proizvoda!");
	        		alert.show();
	        		readFromRedisAndRefreshTable();
	        		
	            }
	        	else
	        	{
	        		Alert alert=new Alert(AlertType.ERROR);
	        		alert.setHeaderText("Greška pri kreiranju proizvoda!");
	        		alert.setContentText("Provjerite sva polja i unesite ID koji ne postoji");
	        		alert.show();
	        	}
	            
	           
	        } catch (Exception e) {
	        	  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
	        }
	       

	        
	        jedisPool.close();
		
	 }
	 
	 
	 public void handleIzmijeniButtonAction() {
		 
		 
		 JedisPoolConfig poolConfig = new JedisPoolConfig();
	     JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

	        try (Jedis jedis = jedisPool.getResource()) {
	        	 Set<String> productKeys = jedis.keys("20_*");
	        	if(!idTextField.getText().equals("") && !nazivTextField.getText().equals("") && !tipSlatkisaTextField.getText().equals("") && !bojaTextField.getText().equals("")  && !okusTextField.getText().equals("") && !kolicinaTextField.getText().equals("") && productKeys.contains("20_"+idTextField.getText()))
	            {
	        		Proizvod p=new Proizvod(Integer.valueOf(idTextField.getText()),nazivTextField.getText(),tipSlatkisaTextField.getText(),bojaTextField.getText(),okusTextField.getText(),Integer.valueOf(kolicinaTextField.getText()));
	        		Map<String,String> map = new HashMap<>();
	        	
	            
	            
	            	map.put("id", String.valueOf(p.getId()));
	            	map.put("naziv", p.getNaziv());
	            	map.put("tipSlatkisa", p.getTipSlatkisa());
	            	map.put("boja",p.getBoja());
	            	map.put("okus",p.getOkus());
	            	map.put("kolicina", String.valueOf(p.getKolicina()));
	            	    
	            	jedis.del("20_"+p.getId());
	            	jedis.hmset("20_"+String.valueOf(p.getId()), map);
	            	Alert alert=new Alert(AlertType.CONFIRMATION);
	        		alert.setHeaderText("Uspješna izmjena proizvoda!");
	        		alert.show();
	        		readFromRedisAndRefreshTable();
	        		
	            }
	        	else
	        	{
	        		Alert alert=new Alert(AlertType.ERROR);
	        		alert.setHeaderText("Greška pri izmjeni proizvoda!");
	        		alert.setContentText("Provjerite sva polja i unesite ID proizvoda kojeg želite promjeniti!");
	        		alert.show();
	        	}
	            
	           
	        } catch (Exception e) {
	        	  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
	        }
	       

	        
	        jedisPool.close();
		
	 }
	 
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			
			//call first time to write data in redis database
			//writeCandysInRedis();
			
			//getting data directly from redis
			
				readFromRedisAndRefreshTable();
				
				
			
			
		}

		public void readFromRedisAndRefreshTable() {
			List<Proizvod> list=new ArrayList<Proizvod>();
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

			
			
			   try (Jedis jedis = jedisPool.getResource()) {
			        // Find all candy product keys
			        Set<String> productKeys = jedis.keys("20_*");

			        for (String key : productKeys) {
			            Map<String, String> candyProductData = jedis.hgetAll(key);

			            Proizvod p=new Proizvod(Integer.valueOf(key.substring(3)),candyProductData.get("naziv"),candyProductData.get("tipSlatkisa"),candyProductData.get("boja"),candyProductData.get("okus"),Integer.valueOf(candyProductData.get("kolicina")));
			           
			            list.add(p);
			        }
			       
			       
			    } catch (Exception e) {
			    	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
			    }

			    
			    jedisPool.close();
			    System.out.println("ff");
			    

      

/*	Read from rest path not from redis
   
   ClientConfig clientConfig = new ClientConfig();
Client client = ClientBuilder.newClient(clientConfig);
WebTarget webTarget = client.target(getBaseURI()).path("rest").path("proizvodi");
Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
Response response = invocationBuilder.get();
List<Proizvod> proizvodi =parseJsonToProizvodList(response.readEntity(String.class));



*/	
			ObservableList<Proizvod> data = FXCollections.observableArrayList(list);
			id.setCellValueFactory(new PropertyValueFactory<>("id"));
			naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
			tipSlatkisa.setCellValueFactory(new PropertyValueFactory<>("tipSlatkisa"));
			boja.setCellValueFactory(new PropertyValueFactory<>("boja"));
			okus.setCellValueFactory(new PropertyValueFactory<>("okus"));
			kolicina.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
			tableView.setItems(data);
		}

		public void writeCandysInRedis() {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
	        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

	        try (Jedis jedis = jedisPool.getResource()) {
	            
	            List<Proizvod> proizvodi=createCandys();
	            Map<String,String> map = new HashMap<>();
	        	
	            for(Proizvod p:proizvodi)
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
		}
		
		public static List<Proizvod> parseJsonToProizvodList(String json) {
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            List<Proizvod> proizvodi = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Proizvod.class));
	            return proizvodi;
	        } catch (Exception e) {
	        	  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
	            return null; 
	        }
	    }
		
		public List<Proizvod> createCandys(){
			
			List<Proizvod> proizvodi=new ArrayList<Proizvod>();
			Proizvod p1=new Proizvod(	1,"Chocolate Bar", "Milk Chocolate", "Brown", "Sweet", 10);
			Proizvod p2=new Proizvod(	2, "Gummy Bears", "Gummy", "Multicolored", "Fruity", 20);
			Proizvod p3=new Proizvod(	3, "Lollipop", "Hard Candy", "Red", "Cherry", 30);
			Proizvod p4=new Proizvod(	4, "Jelly Beans", "Gummy", "Assorted", "Various", 15);
			Proizvod p5=new Proizvod(	5, "Peppermint Twist", "Hard Candy", "White and Red", "Minty", 25);
			Proizvod p6=new Proizvod(	6, "Sour Worms", "Gummy", "Green and Yellow", "Sour", 12);
			Proizvod p7=new Proizvod(   7, "Toffee Crunch", "Toffee", "Golden", "Buttery", 8);
			Proizvod p8=new Proizvod(	8, "Rock Candy", "Rock Candy", "Various", "Sweet", 5);
			Proizvod p9=new Proizvod(	9, "Cotton Candy", "Cotton Candy", "Pink and Blue", "Sugary", 18);
			Proizvod p10=new Proizvod(	10, "Licorice Twists", "Licorice", "Black", "Licorice", 15);
			Proizvod p11=new Proizvod(	11, "Fruit Chews", "Chewy", "Assorted", "Fruity", 22);
			Proizvod p12=new Proizvod(   12, "Caramel Squares", "Caramel", "Brown", "Buttery", 12);
			Proizvod p13=new Proizvod(	13, "Peppermint Patties", "Chocolate", "White", "Minty", 15);
			Proizvod p14=new Proizvod(   14, "Marshmallow Bites", "Marshmallow", "White", "Sweet", 10);
			Proizvod p15=new Proizvod(	15, "Taffy", "Chewy", "Assorted", "Various", 30);
			
			proizvodi.add(p1);
			proizvodi.add(p2);
			proizvodi.add(p3);
			proizvodi.add(p4);
			proizvodi.add(p5);
			proizvodi.add(p6);
			proizvodi.add(p7);
			proizvodi.add(p8);
			proizvodi.add(p9);
			proizvodi.add(p10);
			proizvodi.add(p11);
			proizvodi.add(p12);
			proizvodi.add(p13);
			proizvodi.add(p14);
			proizvodi.add(p15);
			
			return proizvodi;
		}
		
	public void handlePublishButtonAction() {
		try {
			if(!promocijaTextField.getText().equals(""))
			{
				MulticastPromotionService.publishPromotion(promocijaTextField.getText());
				Alert alert=new Alert(AlertType.INFORMATION);
        		alert.setHeaderText("Promocija je uspješno objavljena!");
        		alert.show();
			}
		
		} catch (IOException e) {
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
		}
	}


		private static URI getBaseURI() {
			return UriBuilder.fromUri(
					BASE_STRING).build();
		}

}
