package com.perisic.beds.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import com.perisic.beds.rmiinterface.Question;
import com.perisic.beds.rmiinterface.RemoteQuestions;
import com.perisic.beds.mongoclient.*;

/**
 * Implementation of the questionnaire. Note that chosen answers are collected in this
 * object as well. That means that if the object is destroyed, for instance server restart
 * the collected data is all gone. 
 * To do: make data persistent, for instance link collected data to a database or save data
 * in a text file.  
 * @author Marc Conrad
 *
 */
public class QuestionServerImplementation 
extends UnicastRemoteObject implements RemoteQuestions{
	private static final long serialVersionUID = -3763231206310491048L;
	Vector<Question> myQuestions = new Vector<Question>(); 
	/**
	 * All questions and answers are initialised in the constructor of this class. 
	 * To do: read questions and options from an external data file. 
	 * @throws RemoteException
	 */
	QuestionServerImplementation() throws RemoteException {
		super();
		System.out.println("QuestionServerImplementation Created");
		//TODO: this needs to change 
		String[] answers = {"Yes", "No", "Maybe" }; 
		/**
		 * below implementaion added to extract the data from the database @Madusanka47
		 */
		MongoConnector dbo =  new MongoConnector();
		for (org.bson.Document a : dbo.getBasicQuestions()) {
			myQuestions.add(new Question((String) a.get("Question"), answers));
		}
	}

	/**
	 * Implementation of remote interface method. 
	 */
	@Override
	public int getNumberOfQuestions() throws RemoteException {
		return myQuestions.size();
	}
	/**
	 * Implementation of remote interface method. 
	 */
	@Override
	public Question getQuestion(int i) throws RemoteException {
		return myQuestions.elementAt(i);
	}
	/**
	 * Implementation of remote interface method. 
	 */	
	@Override
	public void submitAnswer(int i, String answer) throws RemoteException {
		myQuestions.elementAt(i).addAnswer(answer);
	}
	/**
	 * below method create a new question in the database
	 * Implementation of remote interface method. 
	 */	
	@Override
	public void createNewQuestion(int i, String question, String type) throws RemoteException {
		myQuestions.elementAt(i).addNewQuestion(question, type);
	}
	/**
	 * below method store all the answers which user has been given
	 * Implementation of remote interface method. 
	 */	
	@Override
	public void storeUserAnswers(int i, String answer) throws RemoteException {
		
	}
	/**
	 * Implementation of remote interface method. 
	 */
	@Override
	public Vector<Question> getData() { 
		return myQuestions; 
	}
	

}
