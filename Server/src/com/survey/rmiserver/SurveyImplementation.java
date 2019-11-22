package com.survey.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

import com.survey.mongoclient.MongoConnector;
import com.survey.rmiinterface.Question;
import com.survey.rmiinterface.RemoteQuestions;


public class SurveyImplementation extends UnicastRemoteObject implements RemoteQuestions {
    private static final long serialVersionUID = -3763231206310491048L;
    Vector<Question> myQuestions = new Vector<Question>();

    SurveyImplementation() throws RemoteException {
        super();
        // System.out.println("QuestionServerImplementation Created");
        String[] answers = new String[2];

        MongoConnector dbo = new MongoConnector();
        for (org.bson.Document a : dbo.getBasicQuestions()) {
            List<String> quesOptions = new ArrayList<String>();
            quesOptions = (List<String>) a.get("Answer");
            answers = quesOptions.toArray(answers);
            System.out.println(answers[0]);
            myQuestions.add(new Question((String) a.get("Question"), answers));
        }
    }

    @Override
    public Question getQuestion(int i) throws RemoteException {
        return myQuestions.elementAt(i);
    }

    @Override
    public int getNumberOfQuestions() throws RemoteException {
        return myQuestions.size();
    }
    // @Override
    // public Vector<Question> getData() {
    // return myQuestions;
    // }

}
