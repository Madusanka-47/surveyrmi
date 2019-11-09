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
		MongoConnector dbo =  new MongoConnector();
		
		System.out.println("QuestionServerImplementation Created");
		String[] answers = {"Yes", "No", "Maybe" }; 

		Question question1 = new Question("Are you happy now?", answers ); 
		myQuestions.add(question1); 

		Question question2 = new Question("Do you like Java?", answers );
		myQuestions.add(question2); 

		Question question3 = new Question("Do you understand event-driven programming?", answers );
		myQuestions.add(question3); 
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
	 * Implementation of remote interface method. 
	 */	
	@Override
	public Vector<Question> getData() { 
		return myQuestions; 
	}

}
