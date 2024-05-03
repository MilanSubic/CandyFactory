package org.unibl.etf.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;


import org.xml.sax.SAXException;
import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.ConnectionFactoryUtil;
import org.unibl.etf.model.Order;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.util.*;
import java.io.*;
import java.net.Socket;

public class NarudzbeController {
	
	public final static String XSD_FILE="C:\\Users\\HP\\eclipse-workspace\\FactoryOrderApp\\order.xsd";
	public final static String QUEUE_NAME = "myQueue";
	
	public String username;
	public String password;
	
	
	public String orderStatus;
	@FXML
	TextArea textArea;
	

	
	public Channel channel;
	public Envelope envelopa;
	public Order order = null;
	@FXML
	Button preuzmiButton;
	@FXML
	Button prihvatiButton;
	@FXML
	Button odbijButton;
	@FXML
	Button backButton;
	
	public void handlePreuzmiButtonAction() throws IOException, TimeoutException {
		Connection connection = ConnectionFactoryUtil.createConnection();
	    channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
			
				envelopa=envelope;
				String message = new String(body, "UTF-8");
				System.out.println("Message received: '" + message + "'");
				JAXBContext context = null;
				try {
					context = JAXBContext.newInstance(Order.class);
				} catch (JAXBException e) {
					  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
				}
				Unmarshaller unmarshaller = null;
				try {
					unmarshaller = context.createUnmarshaller();
				} catch (JAXBException e) {
					  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
				}
				StringReader reader = new StringReader(message);
				try {
				    order= (Order) unmarshaller.unmarshal(reader);
				} catch (JAXBException e) {
					  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
				}
				
		        boolean isValid = validateXMLAgainstXSD(message, XSD_FILE);
		        if (isValid) {
		            System.out.println("XML is valid.");
		           
		            textArea.setText("");  
		            textArea.appendText("Naziv slatkiša: "+order.naziv+"\n");
					textArea.appendText("Ukupna količina: "+order.ukupanBroj+"\n");
					textArea.appendText("Poručena količina: "+order.naruceniBroj+"\n");
					textArea.appendText("Kontakt email: "+order.email+"\n");
		        } else {
		            System.out.println("XML is not valid.");
		        }
			 
				
				
				
			}
		};
		channel.basicConsume(QUEUE_NAME, false, consumer);
       
		
		
		
	}
	
	 public static boolean validateXMLAgainstXSD(String xmlString, String xsdFilePath) {
	        try {
	            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            File xsdFile = new File(xsdFilePath);
	            Schema schema = factory.newSchema(xsdFile);

	            javax.xml.validation.Validator validator = schema.newValidator();


	            StringReader reader = new StringReader(xmlString);
	            validator.validate(new StreamSource(reader));

	           return true;
	           
	        } catch (SAXException | IOException e) {
	        	  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
	              return false;
	        }
	 }
	 
	 public Properties loadMailConfig() throws FileNotFoundException, IOException {
			Properties serverprop = new Properties();
			serverprop.load(new FileInputStream(new File("./factoryOrderApp.properties")));
			String mailProvider = serverprop.getProperty("mail_provider");

			System.out.println("Koristi se " + mailProvider);
			
			Properties mailProp = new Properties();
			mailProp.load(new FileInputStream(new File("./mail" + File.separator + mailProvider + ".properties")));

			 username = mailProp.getProperty("mail.smtp.user");
			 password = mailProp.getProperty("mail.smtp.password");
			 System.out.println(username+"   "+password);
			return mailProp;
		}

		public boolean sendMail(String to, String title, String body) throws FileNotFoundException, IOException {

			Properties props = loadMailConfig();
			System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "securesocket");
			System.out.println("test");
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					System.out.println(username+"  "+password);
					return new PasswordAuthentication(username, password);
				}
			});
			System.out.println(username+"  "+password);
			//session.setDebug(true);
		

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				message.setSubject(title);
				message.setText(body);
				Transport.send(message,username,"password");
				return true;
			} catch (MessagingException e) {
				  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
				return false;
			}
		}
	
	public void handlePrihvatiButtonAction()  throws IOException, TimeoutException, MessagingException {
		
		
		System.out.println("Prihvacena narudzba");
		
		 System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
		 System.setProperty("javax.net.ssl.trustStorePassword", "securesocket");
		 
		 Socket s = null;
		    try {
		    	SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			    s = sf.createSocket("127.0.0.1", 9097);
		     
		      PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		      BufferedReader in = new BufferedReader(
		         new InputStreamReader(s.getInputStream()));
		    
		      String formatedOrder=order.naziv+"#"+String.valueOf(order.ukupanBroj)+"#"+String.valueOf(order.naruceniBroj)+"#"+order.email+"#"+"prihvaceno";
		     // out.println("status:prihvaćeno");
		      out.println(formatedOrder);
		      out.flush();
		      System.out.println("poslano");
		      String line="";
		      if((line=in.readLine())!=null)
		      {
		    	  System.out.println("Ovo je vracena vrijednost "+line);
		    	  orderStatus=line.substring(7);
		      }
		   
		        
		    } finally {
		      if (s != null) {
		        try {
		          s.close();
		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }
		      }
		    }
		    
		    JedisPoolConfig poolConfig = new JedisPoolConfig();
		     JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

		        try (Jedis jedis = jedisPool.getResource()) {
		        	 Set<String> productKeys = jedis.keys("20_*");
		        	
		        	 for(String key:productKeys)
		        	 {
		        		 Map<String,String> map = jedis.hgetAll(key);
		        		 if(map.get("naziv").equals(order.naziv) )
		        		 {
		        			 if(Integer.valueOf(map.get("kolicina"))>order.naruceniBroj)
		        			 {
		        		 
			        			 System.out.println(order.naziv);
			        			 Map<String,String> newMap=new HashMap<String, String>();
	
			        			 	newMap.put("id", map.get("id"));
				 	            	newMap.put("naziv", map.get("naziv"));
				 	            	newMap.put("tipSlatkisa", map.get("tipSlatkisa"));
				 	            	newMap.put("boja",map.get("boja"));
				 	            	newMap.put("okus",map.get("okus"));
				 	            	newMap.put("kolicina", String.valueOf(Integer.valueOf(map.get("kolicina"))-order.naruceniBroj));
				 	            	    
				 	            	jedis.del("20_"+map.get("id"));
				 	            	jedis.hmset("20_"+String.valueOf(map.get("id")), newMap);
				 	            	
				 	           	Alert alert=new Alert(AlertType.CONFIRMATION);
			 	        		alert.setHeaderText("Uspješno ste potvrdili narudzbu!");
			 	        		alert.show();
		        			 }
		        			 else
			        		 {
			        			  	Alert alert=new Alert(AlertType.ERROR);
				 	        		alert.setHeaderText("Narudzba ne može biti potvrđena zbog dostupne količine proizvoda!");
				 	        		alert.show();
			        		 }
		        			 
		        		 }
		        		
		    	
		            }        
		           
		        } catch (Exception e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }
		       

		        
		        jedisPool.close();
		
 	/*	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");  
 	    LocalDateTime now = LocalDateTime.now();
 		
 		try {
 		      FileWriter myWriter = new FileWriter(ORDER_FILES_PATH+dtf.format(now));
 		      myWriter.append("Naziv slatkiša: "+ order.naziv+"\n");
 		      myWriter.append("Ukupna količina: "+ order.ukupanBroj+"\n");
 		      myWriter.append("Poručena količina: "+ order.naruceniBroj+"\n");
 		      myWriter.append("Kontakt email: "+ order.email+"\n");
 		      myWriter.append("Status narudzbe: "+ orderStatus);
 		      myWriter.close();
 		      
 		    } catch (IOException e) {
 		     
 		    	  CustomLogger.setFabrikaLogging();
 				  CustomLogger.logError("Exception", e); 
 		    }
		
		*/
		
		
	        
	 
	      //  sendMail("milan.subic99@gmail.com","Status Vaše narudzbe je promjenjen!","Vaša narudzba je prihvaćena!");
	 	 	channel.basicAck(envelopa.getDeliveryTag(), false);
		
		
    
		
	}

	public void handleOdbijButtonAction() throws IOException {
		System.out.println("Odbijena narudzba");
		
		 System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
		 System.setProperty("javax.net.ssl.trustStorePassword", "securesocket");
		 
		 Socket s = null;
		    try {
		    	SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			    s = sf.createSocket("127.0.0.1", 9097);
		     
		      PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		      BufferedReader in = new BufferedReader(
		         new InputStreamReader(s.getInputStream()));
		    
		      String formatedOrder=order.naziv+"#"+String.valueOf(order.ukupanBroj)+"#"+String.valueOf(order.naruceniBroj)+"#"+order.email+"#"+"odbijeno";
		      out.println(formatedOrder);
		    //  out.println("status:odbijeno");
		      out.flush();
		      System.out.println("poslano");
		      String line="";
		      if((line=in.readLine())!=null)
		      {
		    	  System.out.println("Ovo je vracena vrijednost "+line);
		    	  orderStatus=line.substring(7);
		      }
		   
		        
		    } finally {
		      if (s != null) {
		        try {
		          s.close();
		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }
		      }
		    }
		
 	
 		
 		//sendMail(order.email,"Status Vaše narudzbe je promjenjen!","Vaša narudzba je odbijena!");
 		channel.basicAck(envelopa.getDeliveryTag(), false);
 		
     	Alert alert=new Alert(AlertType.CONFIRMATION);
 		alert.setHeaderText("Uspješno ste odbili narudzbu!");
 		alert.show();
 
		
 		
 		
	}
	
	public void handleBackButtonAction() throws IOException {

		
	      
	        Stage window= (Stage) backButton.getScene().getWindow();
	        window.close();
	        Platform.exit();
	        System.exit(0);
	        

	}



}
