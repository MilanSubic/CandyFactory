package org.unibl.etf.rest.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;

import org.unibl.etf.helper.CustomLogger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MulticastClientThread extends Thread {
	
	//private static final int PORT = 20000;
	//private static final String HOST = "224.0.0.11";
	
	
	public void run() {
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
		
		System.out.println("Multicast client thread started...");
		MulticastSocket socket = null;
		byte[] buf = new byte[256];
		try {
			socket = new MulticastSocket(Integer.parseInt(prop.getProperty("PORT")));
			InetAddress address = InetAddress.getByName(prop.getProperty("HOST"));
			socket.joinGroup(address);
			while(true) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				 System.out.println(received);
				
				 
				
			Platform.runLater(() -> {
				Alert alert=new Alert(AlertType.INFORMATION);
        		alert.setHeaderText("Posebna ponuda za korisnike!");
        		alert.setContentText(received);
        		alert.show();
			});
			
			
			}
		}
		catch(Exception e) {
			 CustomLogger.setKorisnikLogging();
			 CustomLogger.logError("Exception", e); 
		}
	}
	
   
}