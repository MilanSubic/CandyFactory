package org.unibl.etf.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.logger.CustomLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class ClientThread extends Thread {

  //  private final String serverAddress="127.0.0.1";
  //  private final int serverPort=9096;
 //   private final String trustStorePath="keystore.jks";
  //  private final String trustStorePassword="securesocket";
    private final String userInput;
    String status="";
    public Button potvrdiButton;
    
    public ClientThread(String userInput,Button potvrdiButton) {
    	this.userInput=userInput;
    	this.potvrdiButton=potvrdiButton;
    }
    

    @Override
    public void run() {
    	Properties prop;
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("factoryOrderApp.properties")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        System.setProperty("javax.net.ssl.trustStore",prop.getProperty("KEYSTORE"));
        System.setProperty("javax.net.ssl.trustStorePassword", prop.getProperty("KEYSTORE_PASSWORD"));

       
        try (SSLSocket s = (SSLSocket) SSLSocketFactory.getDefault().createSocket(prop.getProperty("serverAddress"), Integer.parseInt(prop.getProperty("serverPort")));
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
                            Stage window= (Stage) potvrdiButton.getScene().getWindow();
                            window.setTitle("Narudzba");
                            window.setScene(new Scene(secondPage, 600, 400));
                           // window.show();
                          //  Parent secondPage= FXMLLoader.load(getClass().getResource("registracija.fxml"));
                	       
                	      
                        	
                        } catch (Exception e) {
                        	  CustomLogger.setFabrikaLogging();
                			  CustomLogger.logError("Exception", e); 
                        }
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setHeaderText("Nije vam dozvoljeno da pristupite ovome dijelu aplikacije!");
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