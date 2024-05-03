package org.unibl.etf.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DistributerRMIInterface extends Remote {
	

	List<String> getDistributorProducts(String distributorName) throws RemoteException;

	List<String> getAllDistributors() throws RemoteException;

}
