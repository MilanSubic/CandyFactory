package org.unibl.etf.rest.client.gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.unibl.etf.logger.CustomLogger;

public class FabrikaOrderStatusSocketServer {

	 public static void main(String args[]) throws Exception {
		    
		 System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
		 System.setProperty("javax.net.ssl.keyStorePassword", "securesocket");
		    
		 ServerSocketFactory ssf = SSLServerSocketFactory.getDefault(); 
		 ServerSocket ss2=ssf.createServerSocket(9097);
		 
		 
		
		
		 System.out.println("Order status secure socket started...");
		  while (true) {
			  try {
	 
			          // Handle order status connections
			          Socket orderStatusSocket = ss2.accept();
			          new SecureSocketOrderStatus(orderStatusSocket).start();
				  
	           
			  }catch(IOException e) {
				  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
			  }
	        }
		  
	 }
}
