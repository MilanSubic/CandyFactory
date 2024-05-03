package org.unibl.etf.rest.client.gui;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.logger.CustomLogger;
import org.unibl.etf.model.FactoryUser;
import org.unibl.etf.model.UsersService;
import org.unibl.etf.model.Proizvod;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
 



public class  FabrikaGUI extends Application {
	
public static boolean run=false;
	
	 
	
	@FXML
	Button korisniciButton;
	@FXML
	Button narudzbeButton;
	@FXML
	Button proizvodiButton;
	@FXML
	Button distributeriButton;
	
	
	
	
    public static void main(String[] args) {
    
    			launch(args);
      
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {

			primaryStage.setTitle("FabrikaApp");
        

        Parent root;
		try {
			
			root = FXMLLoader.load(getClass().getResource("fabrikaGui.fxml"));
			primaryStage.setScene(new Scene(root, 600, 400));
	        primaryStage.show();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			  CustomLogger.setFabrikaLogging();
			  CustomLogger.logError("Exception", e); 
		}
      
		
      
    }
    
  

    

    
    
    
    public void handleKorisniciButtonAction(ActionEvent event) throws IOException {
        System.out.println("korisnici");

            Parent secondPage= FXMLLoader.load(getClass().getResource("korisnici.fxml"));
            Stage window= (Stage) korisniciButton.getScene().getWindow();
            window.setTitle("Rad sa korisnicima");
            window.setScene(new Scene(secondPage, 700, 500));
            

        
    }

    public void handleProizvodiButtonAction(ActionEvent event) throws IOException {
        System.out.println("proizvodi");
        Parent secondPage= FXMLLoader.load(getClass().getResource("proizvodi.fxml"));
        Stage window= (Stage) proizvodiButton.getScene().getWindow();
        window.setTitle("Rad sa proizvodima");
        window.setScene(new Scene(secondPage, 935, 500));

    }
    
    public void handleDistributeriButtonAction(ActionEvent event) throws IOException {
        
    	 Parent secondPage= FXMLLoader.load(getClass().getResource("distributeri.fxml"));
         Stage window= (Stage) distributeriButton.getScene().getWindow();
         window.setTitle("Rad sa distributerima");
         window.setScene(new Scene(secondPage, 600, 400));
    }

   

}