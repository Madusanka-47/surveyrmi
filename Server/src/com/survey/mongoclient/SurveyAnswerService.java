package com.survey.mongoclient;

import java.util.Arrays;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SurveyAnswerService {
    private static final String collectionName = "answer_pane";

    public int addQuestion(String ques, String[] answertype) {
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            if (ques != null && answertype != null) {
                collection.insertOne(new Document("quesid", 11).append("description", ques)
                        .append("ques_type", Arrays.asList(answertype)).append("basic", false)
                        .append("questionnaireid", 1));
                return 1;
            }
        } catch (Exception e) {
            System.out.println("addQuestion" + e);
        } finally {
        }
        return 0;
    }

    public int addAnswers(String[] respons) {
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            if (respons != null) {
                collection.insertOne(new Document("questionnaireid", 11).append("userid", 1)
                        .append("respons", Arrays.asList(respons)).append("que_complete", true));
                return 1;
            }
        } catch (Exception e) {
            System.out.println("addQuestion" + e);

        } finally {

        }
        return 0;
    }
}


/**
 * Debug main for SurveyQuestionService 
 * Remove once the implementaion completed
 * */
class StartService {
	public static void main(String[] args) {
        SurveyAnswerService anwSvy =  new SurveyAnswerService();
        
	}
}