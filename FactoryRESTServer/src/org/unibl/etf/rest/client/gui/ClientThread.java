package org.unibl.etf.rest.client.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.logger.CustomLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private final String serverAddress="127.0.0.1";
    private final int serverPort=9096;
    private final String trustStorePath="keystore.jks";
    private final String trustStorePassword="securesocket";
    private final String userInput;
    String status="";

    public ClientThread(String userInput) {
    	this.userInput=userInput;
    }
    

    @Override
    public void run() {
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

       
        try (SSLSocket s = (SSLSocket) SSLSocketFactory.getDefault().createSocket(serverAddress, serverPort);
        	     PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        	     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
        		
      
        	System.out.println("ovo je socket "+s);
        		System.out.println("ovo je out "+out);
        	System.out.println("ovo je userInput iz clienta "+userInput);
            out.println(userInput);
            out.flush();
            System.out.println("ovo je userInput iz clienta poslije flush "+userInput);

            status=in.readLine();
            System.out.println("ovo je status iz clienta "+status);
            
      
            if (status != null) {
            	System.out.println("ovo je status "+status);
                Platform.runLater(() -> {
                    if (status.equals("dozvoljeno")) {
                        try {
                            Parent secondPage = FXMLLoader.load(getClass().getResource("narudzbe.fxml"));
                            Stage window = new Stage();
                            window.setTitle("Narudzba");
                            window.setScene(new Scene(secondPage, 600, 400));
                            window.show();
                        } catch (Exception e) {
                        	  CustomLogger.setFabrikaLogging();
                			  CustomLogger.logError("Exception", e); 
                        }
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setHeaderText("Nije Vam dozvoljeno da pristupite ovome dijelu aplikacije!");
                        alert.show();
                    }
                });
            }
            else {
            	System.out.println("status je null");
            }

        } catch (Exception e) {
        	  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
        }
    }
}