package com.perisic.beds.rmiinterface;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
/**
 * RMI interface to enable to retrieve questions from the server and to submit data
 * to the server. 
 * @author Marc Conrad
 *
 */
public interface RemoteQuestions extends Remote {
	/**
	 * Number of questions on the server.
	 * @return
	 * @throws RemoteException
	 */
	public int getNumberOfQuestions() throws RemoteException; 
	/**
	 * Retrieve specific question from the server. 
	 * @param i number of the question. 
	 * @return the Question. 
	 * @throws RemoteException
	 */
	public Question getQuestion(int i) throws RemoteException; 
	/**
	 * Submit the answer to the question number i.
	 * @param i question where the answer belongs to.
	 * @param answer the answer given to this question. 
	 * @throws RemoteException
	 */
	void submitAnswer(int i, String answer) throws RemoteException;  
	/**
	 * Create a new questionar in the db. 
	 * @return 
	 * @throws RemoteException
	 */
	void createNewQuestion(int i, String question, String type) throws RemoteException;
	/**
	 * Store the relavent answers that user has been given 
	 * @return 
	 * @throws RemoteException
	 */
	void storeUserAnswers(int i, String answer) throws RemoteException;  
	/**
	 * Create a new question instance in the backend. 
	 * @return 
	 * @throws RemoteException
	 */
	
	public Vector<Question> getData() throws RemoteException;
	
}
