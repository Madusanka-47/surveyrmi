package com.survey.mongoclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class SurveyQuestionService {
    private static final String collectionName = "question_pane";

    public void addToQuestionPane(ArrayList<String> questions, List<String[]> options) {
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            MongoCollection<Document> collection = database.getCollection(collectionName);
            long quseId = collection.countDocuments();
            List<Document> questionSet = new ArrayList<Document>();
            for (int i = 0; i < questions.size(); i++) {
                questionSet.add(new Document("quesid", quseId + i).append("description", questions.get(i))
                        .append("ques_type", Arrays.asList(options.get(i))).append("basic", false));
            }
            collection.insertMany(questionSet);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {

        }
    }

    public ArrayList<Document> getBasicQuestions() {
        ArrayList<Document> questionDoc = null;
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            questionDoc = new ArrayList<Document>();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            FindIterable<org.bson.Document> cus = collection.find();
            for (org.bson.Document a : cus.collation(null)) {
                questionDoc.add(new Document("Question", a.get("description")).append("Answer", a.get("ques_type")));
            }
            return questionDoc;

        } catch (Exception ex) {
            System.out.println(ex);
            return questionDoc;
        } finally {

        }
    }

    public int updateQuestionPane(String description, int quesId) {
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            if (!description.isEmpty()) {
                MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
                collection.updateOne(eq("quesid", quesId),
                        new Document("$set", new Document("description", description)));
                return 1;
            } else {
                throw new Exception("Empty description");
            }

        } catch (Exception ex) {
            System.out.println(ex);
            return 0;
        } finally {

        }

    }
    // public void deleteQuestionPane(); ?
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */
/*
 * class StartService { public static void main(String[] args) {
 * SurveyQuestionService svy = new SurveyQuestionService();
 * 
 * } }
 */