package com.survey.mongoclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class SurveyAccessService {

    private static final String collectionName = "access_pane";

    public int createPaneUser(String currntUserName, String usrname, String pswd, boolean isSuper) {
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            FindIterable<org.bson.Document> sprusr = collection.find(eq("superuser", true));
            List<Document> newUser = new ArrayList<Document>();

            for (org.bson.Document spruser : sprusr.collation(null)) {
                if (spruser.get("username").equals(currntUserName)) {
                    if (!usrname.isEmpty() && !pswd.isEmpty()) {
                        Document isAvilable = collection.find(eq("username", usrname)).first();
                        if (isAvilable == null) {
                            newUser.add(new Document("userid", collection.countDocuments() + 1)
                                    .append("username", usrname).append("password", pswd).append("superuser", isSuper));
                            collection.insertMany(newUser);
                            return 1;
                        } else {
                            throw new Exception("User name alredy exist");
                        }
                    }
                }
            }
            // long quseId = collection.countDocuments();
            // List<Document> questionSet = new ArrayList<Document>();
            // questionSet.add(new Document("quesid", quseId + i).append("description",
            // questions.get(i))
            // .append("ques_type", Arrays.asList(options.get(i))).append("basic", false));

            // collection.insertOne(questionSet);
        } catch (Exception ex) {
            System.out.println(ex);
            return 0;

        } finally {

        }
        return 0;
    }
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */
class StartAccessService {
    public static void main(String[] args) {
        SurveyAccessService acc = new SurveyAccessService();
        acc.createPaneUser("admin", "dulanjan", "1235", false);
    }
}