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
            // MongoConnector dbo = new MongoConnector();
            final MongoDatabase database = MongoConnector.getInstance();
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
            final MongoDatabase database = MongoConnector.getInstance();
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
            final MongoDatabase database = MongoConnector.getInstance();
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

    public String finalSurveyAnalysis(String usrname) {
        SurveyAccessService sas = new SurveyAccessService();
        sas.updatePaneUser("@admin", usrname, "@", "@", false, false, true);
        ArrayList<Document> answerList = getAnswerPaneAnswers(Integer.parseInt(sas.getUserIdByUserName(usrname)));
        int freelancePoint = 0;
        int socialPoints = 0;
        for( Document answer : answerList) {
            String response = answer.get("Answers").toString();
            int quesId = Integer.parseInt(answer.get("QuesId").toString());

            switch(quesId){
                case 0:  if (Integer.parseInt(response) < 40){freelancePoint++;}else{socialPoints++;} break;
                case 1:  if (response.equals("Yes")){freelancePoint++;}else{socialPoints++;} break;
                case 2:  if (response.contains("freelance")){freelancePoint++;}else{socialPoints++;} break;
                case 3:  if (response.equals("O/L") || response.equals("A/L")) {socialPoints++;}else{freelancePoint++;} break;
                case 4:  if (response.equals("Poor")|| response.equals("Good")){socialPoints++;}else{freelancePoint++;} break;
                case 5:  if (response.equals("Poor")|| response.equals("Good")){freelancePoint++;}else{socialPoints++;} break;
                case 6:  if (response.equals("Ask people")){socialPoints++;}else{freelancePoint++;} break;
                case 7:  if (response.equals("Yes")){freelancePoint++;}else{socialPoints++;} break;
                case 8:  if (response.equals("Take it")){freelancePoint++;}else{socialPoints++;} break;
                case 9:  if (response.equals("Money")){freelancePoint++;}else{socialPoints++;} break;
                case 10:  if (response.contains("free")){freelancePoint++;}else{socialPoints++;} break;
                case 11:  if (response.equals("The Candle")){freelancePoint++;}else{socialPoints++;} break;
                default: break;
            }
            
        }
        if (freelancePoint >= socialPoints){
            return "freelancer";
        }else{
            return "social";
        }

    }
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */

class StartService {
    public static void main(String[] args) {
        SurveyAnswerService anwSvy = new SurveyAnswerService();
        anwSvy.finalSurveyAnalysis("madusanka.wettewa@gmail.com");
    }
}
