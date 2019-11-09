package com.perisic.beds.rmiserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.perisic.beds.rmiinterface.RemoteQuestions;

/**
 * Use this class to start the server. 
 * @author Marc Conrad
 *
 */
public class StartServer {
	/**
	 * Entry point of the server. 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		System.out.println("Attempting to start the Question Server..."); 
		try {
			RemoteQuestions questions = new QuestionServerImplementation();
			Registry reg = LocateRegistry.createRegistry(1099);
			reg.rebind("QuestionService1819",questions);

			System.out.println("Service started. Welcome to the RMI Question Service!");

		} catch (RemoteException e) {
			System.out.println("An error occured: "+e.toString()); 
			e.printStackTrace();
		} 

	}

}
