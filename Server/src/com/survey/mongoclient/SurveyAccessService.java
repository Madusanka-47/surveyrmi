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
            String userid = Long.toString(collection.countDocuments() + 1);

            for (org.bson.Document spruser : sprusr.collation(null)) {
                if (spruser.get("username").equals(currntUserName)) {
                    if (!usrname.isEmpty() && !pswd.isEmpty()) {
                        Document isAvilable = collection.find(eq("username", usrname)).first();
                        if (isAvilable == null) {
                            SurveyLogingService secure = new SurveyLogingService();
                            String hashcode = secure.encryptLogings(pswd, userid, null, true);
                            if (hashcode != null) {
                                newUser.add(new Document("userid", userid).append("username", usrname)
                                        .append("password", hashcode).append("superuser", isSuper));
                                collection.insertMany(newUser);
                                return 1;
                            } else {
                                throw new Exception("Failed hashed");
                            }
                        } else {
                            throw new Exception("User name alredy exist");
                        }
                    }
                }
            }
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
        acc.createPaneUser("admin", "Madusanka47", "Name128$", false);
    }
}