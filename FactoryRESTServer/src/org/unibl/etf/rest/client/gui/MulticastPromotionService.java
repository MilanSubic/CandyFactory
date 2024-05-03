package org.unibl.etf.rest.client.gui;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.unibl.etf.logger.CustomLogger;


public class MulticastPromotionService {
	private static final int PORT = 20000;
	private static final String HOST = "224.0.0.11";
	
	
	public static void publishPromotion(String message) throws FileNotFoundException, IOException {
		
		
		MulticastSocket socket = null;
		byte[] buf = new byte[256];
		try {
			socket = new MulticastSocket();
			InetAddress address = InetAddress.getByName(HOST);
		
			socket.joinGroup(address);
			
			buf = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address,PORT);
			socket.send(packet);
		}
		catch(Exception e) {
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
		}
	}
	
   
}