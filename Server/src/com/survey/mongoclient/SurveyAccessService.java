package com.survey.mongoclient;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.survey.mongoclient.SurveyMailService;

/**
 * Service use for get access for survey related transactions
 */
public class SurveyAccessService {

    private static final String collectionName = "access_pane";

    public String getUserAccessHash(final String userid) {
        try {
            final MongoDatabase database = MongoConnector.getInstance();
            final Document usrDoc = database.getCollection(collectionName).find(eq("userid", userid)).first();
            return usrDoc.get("password").toString();
        } catch (final Exception ex) {
            System.out.println(ex);
            return null;
        } finally {

        }
    }

    public String getUserIdByUserName(final String usrname) {
        try {
            final MongoDatabase database = MongoConnector.getInstance();
            final Document usrDoc = database.getCollection(collectionName).find(eq("username", usrname)).first();
            return usrDoc.get("userid").toString();
        } catch (final Exception ex) {
            System.out.println(ex);
            return null;
        } finally {

        }
    }

    public int createPaneUser(final String currntUserName, final String usrname, final String pswd,
            final boolean isSuper) {
        try {
            final byte[] salt = new byte[0];
            final MongoDatabase database = MongoConnector.getInstance();
            final MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            final FindIterable<org.bson.Document> sprusr = collection.find(eq("superuser", true));
            final List<Document> newUser = new ArrayList<Document>();
            final String userid = Long.toString(collection.countDocuments() + 1);
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

            for (final org.bson.Document spruser : sprusr.collation(null)) {
                if (spruser.get("username").equals(currntUserName) && usrname.matches(regex)) {
                    if (!usrname.isEmpty() && !pswd.isEmpty()) {
                        final Document isAvilable = collection.find(eq("username", usrname)).first();
                        if (isAvilable == null) {
                            final SurveyLogingService secure = new SurveyLogingService();
                            final String hashcode = secure.encryptLogings(pswd, userid, salt, true);
                            if (hashcode != null) {
                                newUser.add(new Document("userid", userid).append("username", usrname)
                                        .append("password", hashcode).append("superuser", isSuper));
                                collection.insertMany(newUser);
                                SurveyMailService acc = new SurveyMailService();
                                acc.sentCreationEmail(pswd, usrname);
                                return 1;
                            } else {
                                throw new Exception("Failed hashed");
                            }
                        } else {
                            throw new Exception("User name alredy exist");
                        }
                    }
                } else {
                    System.out.println("User name doesn't meet mimium requirment");
                }
            }
        } catch (final Exception ex) {
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

// class StartAccessService {
//     public static void main(final String[] args) throws Exception {
//         final SurveyAccessService acc = new SurveyAccessService();
//         acc.createPaneUser("admin", "madusanka.wettewa@gmail.com", "105admin", false);

//     }
// }
