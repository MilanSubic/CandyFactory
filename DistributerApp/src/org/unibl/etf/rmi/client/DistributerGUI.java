package org.unibl.etf.rmi.client;


import java.io.IOException;


import org.unibl.etf.logger.CustomLogger;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class DistributerGUI extends Application {
	
	public static final String DISTRIBUTORS_PATH = "C:\\Users\\HP\\eclipse-workspace\\DistributerApp\\src\\org\\unibl\\etf\\rmi\\distributors\\";

	
	@FXML
	TextField firmaTextField;

	@FXML
	Button potvrdiButton;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	

    	
    	
        primaryStage.setTitle("DistributerApp");

        Parent root;
		try {
			
			root = FXMLLoader.load(getClass().getResource("distributerLogin.fxml"));
			primaryStage.setScene(new Scene(root, 400, 400));
	        primaryStage.show();
	        
		} catch (IOException e) {
			  CustomLogger.setDistributerLogging();
			  CustomLogger.logError("Exception", e); 
		}
      
        
      
    }
    
    public void handlePotvrdiButtonAction() {
    	System.out.println("potvrdi");
 
		
		if(!firmaTextField.getText().equals(""))
		{
			Parent p;
	        FXMLLoader loader=new FXMLLoader();
			loader.setLocation(getClass().getResource("distributerGui.fxml"));
			DistributerController distributerController = new DistributerController();
			distributerController.setFirma(firmaTextField.getText());

			loader.setController(distributerController);
			try{
				
			     p = loader.load();
			     Stage window=(Stage) potvrdiButton.getScene().getWindow();
					window.setScene(new Scene(p,700,500));
					window.setTitle("DistributerApp");
					window.show();
			   
			}catch(Exception e){
				  CustomLogger.setDistributerLogging();
				  CustomLogger.logError("Exception", e); 
				}
	
		}
		
	}


    	
    	
    	
 
}
    