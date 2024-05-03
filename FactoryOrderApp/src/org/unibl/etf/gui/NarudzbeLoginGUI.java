package org.unibl.etf.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.logger.CustomLogger;


public class NarudzbeLoginGUI extends Application{
	
	@FXML
	Button backButton;
	
	@FXML
	Button potvrdiButton;
	
	
	@FXML
	TextField nameTextField;
	
    public static void main(String[] args) {
    
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("OrderApp");
        
        Parent root;
      		try {
      			root = FXMLLoader.load(getClass().getResource("narudzbeLogin.fxml"));
      			primaryStage.setScene(new Scene(root, 400, 400));
      	        primaryStage.show();
      		} catch (IOException e) {
      			// TODO Auto-generated catch block
      			 CustomLogger.setKorisnikLogging();
      			 CustomLogger.logError("Exception", e); 
      		}
            
        
    }
	

	 
	 public void handlePotvrdiButtonAction() throws IOException {
		 System.out.println("potvrdi");
		
		 new ClientThread(nameTextField.getText(),potvrdiButton).start();
		  
		 System.out.println("kreirano"); 
		  	
	 }


	 
	

}