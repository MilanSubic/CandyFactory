package org.unibl.etf.rmi;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.unibl.etf.logger.CustomLogger;

import java.util.*;
import java.io.*;

public class DistributerRMIServer  implements DistributerRMIInterface {
	
	
	public static final String PATH = "resources";
	public static final String DISTRIBUTORS_PATH = "C:\\Users\\HP\\eclipse-workspace\\DistributerApp\\src\\org\\unibl\\etf\\rmi\\distributors\\";

	
	public DistributerRMIServer() throws RemoteException {

	}

	@Override
	public List<String> getAllDistributors() throws RemoteException {
		
		List<String> distributors = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(DISTRIBUTORS_PATH+"distributors.txt"))) {
			
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	distributors.add(line);
		    }
		    
		} catch (IOException e) {
			  CustomLogger.setDistributerLogging();
			  CustomLogger.logError("Exception", e); 
		}
		
		return distributors;
	}

	
	@Override
	public List<String> getDistributorProducts(String distributorName) throws RemoteException {
		
		List<String> products = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(DISTRIBUTORS_PATH+distributorName + ".txt"))) {
			
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	products.add(line);
		    }
		    
		} catch (IOException e) {
			  CustomLogger.setDistributerLogging();
			  CustomLogger.logError("Exception", e); 
		}
		
		return products;
	}
	
	public static void main(String args[]) {
		System.setProperty("java.security.policy", PATH + File.separator + "server_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			DistributerRMIServer server = new DistributerRMIServer();
			DistributerRMIInterface stub = (DistributerRMIInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("distributeri", stub);
			System.out.println("RMI server started...");
		} catch (Exception e) {
			  CustomLogger.setDistributerLogging();
			  CustomLogger.logError("Exception", e); 
		}
	}


}
