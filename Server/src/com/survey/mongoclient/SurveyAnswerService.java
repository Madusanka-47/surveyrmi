package com.survey.mongoclient;

import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class SurveyAnswerService {
    private static final String collectionName = "answer_pane";

    public int addToAnswerPane(String response, int userid, int quesid) {
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            if (quesid != 0 && quesid != 0) {
                collection.insertOne(
                        new Document("userid", userid).append("quesid", quesid).append("response", response));
                return 1;
            } else {
                throw new Exception("param cannot be 0");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
        }
        return 0;
    }

    public int updateAnswerPane(String response, int userid, int quesid) {
        // Update condition needs to re modify
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            if (!response.isEmpty()) {
                collection.updateOne(eq("userid", userid), new Document("$set", new Document("response", response)));
                return 1;
            } else {
                throw new Exception("Empty description");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {

        }
        return 0;
    }

    public ArrayList<Document> getAnswerPaneAnswers(int userid) {
        ArrayList<Document> answerList = null;
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            answerList = new ArrayList<Document>();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            FindIterable<org.bson.Document> cus = collection.find(eq("userid", userid));
            for (org.bson.Document a : cus.collation(null)) {
                answerList.add(new Document("Answers", a.get("response")).append("UserId", a.get("userid"))
                        .append("QuesId", a.get("quesid")));
            }
            return answerList;

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {

        }
        return answerList;
    }
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */
/*
 * class StartService { public static void main(String[] args) {
 * SurveyAnswerService anwSvy = new SurveyAnswerService(); } }
 */