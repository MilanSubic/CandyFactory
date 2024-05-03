package org.unibl.etf.rest.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.FactoryUser;
import org.unibl.etf.model.UsersService;

public class SecureSocketLogin extends Thread{
	
	public UsersService service;   
	public Socket socket;
	public BufferedReader in;
	public PrintWriter out;

	
	

	  public SecureSocketLogin(Socket loginSocket) throws IOException {
	    socket = loginSocket;
	    service=new UsersService();
	   
	  }
	 
	
	 public void run() {
		
		
		 try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			  PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
		      
			 String name="";
		 
		      while ((name = in.readLine()) != null) {
		    	//  System.out.println("ovo je name in secure login "+name);
		    	  boolean accept=false;
			        for(FactoryUser u:service.factoryUserList())
			        {
			        	
			        	if(u.getNaziv().equals(name))
			        	{
			        		System.out.println("ovo je dozvoljeno iz secure login ");
			        		accept=true;
			        		out.println("dozvoljeno");
			        		out.flush();
			        	}
			        	
			        }
			      //  System.out.println("ovo je greska iz secure login ");
			        if(accept==false)
			        {
			        	out.println("greska");
			        	out.flush();
			        }
			      }
		      
		     
		    }
		 catch(IOException e) {
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
		 }
		 finally {
		      if (socket != null) {
		        try {
		        	System.out.println("zatvoren socket login");
		        	
		            socket.close();
		        } catch (IOException e) {
		        	  CustomLogger.setFabrikaLogging();
					  CustomLogger.logError("Exception", e); 
		        }
		      }
		    }
		    
	 }
	    

}
