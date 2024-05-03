package org.unibl.etf.rest.client.gui;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.FactoryUser;
import org.unibl.etf.model.UsersService;

public class SecureSocketOrderStatus extends Thread{
	
	public final static String ORDER_FILES_PATH="C:\\Users\\HP\\eclipse-workspace\\FactoryRESTServer\\src\\org\\unibl\\etf\\rest\\client\\gui\\orders\\";
	
	public UsersService service;   
	public Socket socket;
	public String[] orderFormated;
	
	

	  public SecureSocketOrderStatus(Socket s) {
	    socket = s;
	    service=new UsersService();
	  }
	 
	
	 public void run() {
		
		 try {
			 BufferedReader in = new BufferedReader(
			          new InputStreamReader(socket.getInputStream()));
		      PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
		      
		      String name;
		      String status;
		     
		     
		      while ((name = in.readLine()) != null) {
		    	  System.out.println(name);
		    	  orderFormated=name.split("#");
		    	  
		    	  
		    	  status=orderFormated[orderFormated.length-1];
		    	  System.out.println("ovo je ttt "+status);  
		    	  writeOrder();
		    	  out.println("status:"+status);
		    	  
		    	/*  if(name.startsWith("status:"))
		    	  {
		    		  status=name.substring(7);
		    		  System.out.println(status);
		    		  out.println("status:"+status);
		    		
		    	  }
		    	  */
		    	 
		      }
		     
		    }
		 catch(IOException e) {
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
		 }
		 finally {
		      if (socket != null) {
		        try {
		        	System.out.println("zatvoren socket status");
		          socket.close();
		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }
		      }
		    }
	 }
	 
	 public void writeOrder() {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");  
	 	    LocalDateTime now = LocalDateTime.now();
	 		
	 		try {
	 		      FileWriter myWriter = new FileWriter(ORDER_FILES_PATH+dtf.format(now));
	 		      myWriter.append("Naziv slatkisa: "+ orderFormated[0]+"\n");
	 		      myWriter.append("Ukupna kolicina: "+ orderFormated[1]+"\n");
	 		      myWriter.append("Porucena kolicina: "+ orderFormated[2]+"\n");
	 		      myWriter.append("Kontakt email: "+ orderFormated[3]+"\n");
	 		      myWriter.append("Status narudzbe: "+ orderFormated[4]);
	 		      myWriter.close();
	 		      
	 		    } catch (IOException e) {
	 		     
	 		    	  CustomLogger.setFabrikaLogging();
	 				  CustomLogger.logError("Exception", e); 
	 		    }
	 }
	    

}