package com.survey.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

import com.survey.mongoclient.SurveyQuestionService;
import com.survey.rmiinterface.Question;
import com.survey.rmiinterface.RemoteQuestions;


public class SurveyImplementation extends UnicastRemoteObject implements RemoteQuestions {
    private static final long serialVersionUID = -3763231206310491048L;
    Vector<Question> myQuestions = new Vector<Question>();

    SurveyImplementation() throws RemoteException {
        super();
        // System.out.println("QuestionServerImplementation Created");
        

        SurveyQuestionService svy = new SurveyQuestionService();
        for (org.bson.Document a : svy.getBasicQuestions()) {
            List<String> quesOptions = new ArrayList<String>();
            quesOptions = (List<String>) a.get("Answer");
            String[] answers = new String[quesOptions.size()];
            answers = quesOptions.toArray(answers);
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
