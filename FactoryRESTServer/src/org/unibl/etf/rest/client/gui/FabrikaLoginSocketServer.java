package org.unibl.etf.rest.client.gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.unibl.etf.logger.CustomLogger;

public class FabrikaLoginSocketServer {

	 public static void main(String args[]) throws Exception {
		    
		 System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
		 System.setProperty("javax.net.ssl.keyStorePassword", "securesocket");
		    
		 ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
		 ServerSocket ss = ssf.createServerSocket(9096);
		 
		 
		 
		
		
		 System.out.println("Login secure socket started...");
		  while (true) {
			  try {
	            // Handle login connections
				  Socket loginSocket = ss.accept();
			      System.out.println("Accepted connection ");
			      SecureSocketLogin secureSocket1=new SecureSocketLogin(loginSocket);
			      secureSocket1.start();

				  
	           
			  }catch(IOException e) {
				  CustomLogger.setFabrikaLogging();
				  CustomLogger.logError("Exception", e); 
			  }
	        }
		  
	 }
}
