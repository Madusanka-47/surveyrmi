package com.survey.mongoclient;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.survey.mongoclient.SurveyMailService;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

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

    public int createPaneUser(final String currntUserName, final String usrname, final String fname, final String lname,
            final boolean isSuper) {
        try {
            String pswd = generateRandomPassword(8).toString();
            System.out.println(pswd);
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
                                        .append("firstname", fname).append("lastname", lname)
                                        .append("password", hashcode).append("superuser", isSuper)
                                        .append("activeuser", true));
                                collection.insertMany(newUser);
                                SurveyMailService acc = new SurveyMailService();
                                acc.sentCreationEmail(pswd, usrname, fname, true);
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

    public int updatePaneUser(final String currntUserName, final String usrname, final String fname, final String lname,
            final boolean isSuper, final boolean isPasswordReset, final boolean active) {
        try {

            String pswd = generateRandomPassword(8).toString();
            System.out.println(pswd);
            final MongoDatabase database = MongoConnector.getInstance();
            final MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            final FindIterable<org.bson.Document> sprusr = collection.find(eq("superuser", true));
            for (final org.bson.Document spruser : sprusr.collation(null)) {
                if (spruser.get("username").equals(currntUserName)) {
                    if (!usrname.equals("") && usrname != null) {
                        FindIterable<org.bson.Document> isAvilable = collection.find(eq("username", usrname));
                        String userid = null;
                        String fname_ = null;
                        String lname_ = null;
                        Document updateSet = null;
                        for (org.bson.Document doc : isAvilable.collation(null)) {
                            userid = doc.get("userid").toString();
                            fname_ = doc.get("firstname").toString();
                            lname_ = doc.get("lastname").toString();
                        }
                        updateSet = new Document("firstname", !fname.equals(fname) ? fname_ : fname)
                                .append("lastname", !lname.equals(lname) ? lname_ : lname).append("activeuser", active);
                        if (isPasswordReset) {
                            final SurveyLogingService secure = new SurveyLogingService();
                            final String hashcode = secure.decryptLoggins(pswd, userid);
                            if (hashcode != null) {
                                updateSet.append("password", hashcode);
                            }
                        }
                        if (userid != null) {
                            collection.updateOne(eq("username", usrname), new Document("$set", updateSet));
                            if (isPasswordReset) {
                                SurveyMailService acc = new SurveyMailService();
                                acc.sentCreationEmail(pswd, usrname, fname_, false);
                                return 1;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }

    public ArrayList<Document> getUserDetails(String PARAM_) {
        ArrayList<Document> userList = null;
        try {
            final MongoDatabase database = MongoConnector.getInstance();
            userList = new ArrayList<Document>();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            FindIterable<org.bson.Document> cus = null;
            if (PARAM_ == null) {
                cus = collection.find();
            } else {
                cus = collection.find(eq("username", PARAM_));
            }
            for (org.bson.Document doc : cus.collation(null)) {
                if (!Boolean.parseBoolean(doc.get("superuser").toString())) {
                    userList.add(new Document("email", doc.get("username")).append("firstname", doc.get("firstname"))
                            .append("lastname", "lastname").append("activeuser", doc.get("activeuser"))
                            .append("isSuper", doc.get("superuser")));
                }
            }
            return userList;

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {

        }
        return userList;
    }

    public String generateRandomPassword(int length) {
        final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        final String NUMBER = "0123456789";
        final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
        final String PASSWORD_ALLOW_BASE = CHAR_LOWER + OTHER_CHAR + CHAR_UPPER + NUMBER;
        final String PASSWORD_ALLOW_BASE_SHUFFLE = shuffleString(PASSWORD_ALLOW_BASE);
        final String PASSWORD_ALLOW = PASSWORD_ALLOW_BASE_SHUFFLE;
        SecureRandom random = new SecureRandom();

        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(PASSWORD_ALLOW.length());
            char rndChar = PASSWORD_ALLOW.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    public static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */

class StartAccessService {
    public static void main(final String[] args) throws Exception {
        final SurveyAccessService acc = new SurveyAccessService();
        // acc.createPaneUser("admin@survey.com", "www.ruxian@gmail.com", "madusanka",
        // "wettewa", false);
        // acc.updatePaneUser("admin@survey.com", "madusanka.wettewa@gmail.com",
        // "dulanjan", "madusanka", false, false);
        // System.out.println( acc.getUserDetails("madusanka.wettewa@gmail.com"));
    }
}
