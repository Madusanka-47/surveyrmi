package com.survey.rmiinterface;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
/**
 * RMI interface to enable to retrieve questions from the server and to submit data
 * to the server. 
 * @author Madusanka47
 *
 */
public interface RemoteQuestions extends Remote {
	
	public int getNumberOfQuestions() throws RemoteException; 
	
	public Question getQuestion(int i) throws RemoteException; 
	
	void submitAnswer(int i, String answer) throws RemoteException;  

	public Vector<Question> getData() throws RemoteException;
	
}
