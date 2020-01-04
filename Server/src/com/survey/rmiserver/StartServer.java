package com.survey.rmiserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.survey.mongoclient.SurveyAnswerService;
import com.survey.rmiinterface.RemoteQuestions;

public class StartServer {
/**
 * RMI server for the questionnaire service
 * @param args
 */
	public static void main(String[] args) {
		System.out.println("Attempting to start the Survey Server...");
		try {
			RemoteQuestions questions = new SurveyImplementation();
			Registry reg = LocateRegistry.createRegistry(1099);
			reg.rebind("QuestionnaireService", questions);
			System.out.println("Survey online..!");

		} catch (RemoteException e) {
			System.out.println("An error occured: " + e.toString());
			e.printStackTrace();
		}

	}

}
