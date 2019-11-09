package com.perisic.beds.mongoclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDb connection for the questionnaire service connection will be made into
 * cluster0-yx3nh.mongodb.net cluster.
 * 
 * @author Madusanka47
 *
 */
public class MongoConnector {

	MongoDatabase database = null;
	String tab_collection[] = { "question_pane", "answer_pane" };

	public MongoConnector() {
		try {

			MongoClientURI uri = new MongoClientURI(
					"mongodb+srv://dumalk:dumalk@cluster0-yx3nh.mongodb.net/test?retryWrites=true&w=majority");
			@SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient(uri);
			this.database = mongoClient.getDatabase("survey_core");
		} catch (Exception e) {

			System.out.print(e);
		}
	}

	/**
	 * Basic insert for the survey This implementaion use for only testing Purpose
	 * 
	 * @param collection
	 */
	public void InsertDocument(String desc[]) {
		MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[0]);
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 10; i++) {
			documents.add(new Document("quesid", i).append("description", desc[i])
					.append("ques_type", Arrays.asList("Yes", "No", "Maybe")).append("basic", false));
		}
		collection.insertMany(documents);
	}

	/**
	 * Implementaion of getting questionnaires
	 * 
	 * @return questionnaire document along with the possible answers
	 */
	public ArrayList<Document> getBasicQuestions() {

		ArrayList<Document> questionDoc = new ArrayList<Document>();
		try {

			MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[0]);
			FindIterable<org.bson.Document> cus = collection.find();
			for (org.bson.Document a : cus.collation(null)) {
				questionDoc.add(new Document("Question", a.get("description")).append("Answer", a.get("ques_type")));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("getBasicQuestions" + e);
		}

		return questionDoc;
	}

	/**
	 * Implementaion of saving new questionnaires
	 * 
	 * @param ques
	 * @param answertype
	 * @return
	 */
	public int addQuestion(String ques, String[] answertype) {

		try {
			MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[0]);
			if (ques != null && answertype != null) {
				collection.insertOne(new Document("quesid", 11).append("description", ques)
						.append("ques_type", Arrays.asList(answertype)).append("basic", false).append("questionnaireid", 1));
				return 1;
			}
		} catch (Exception e) {
			System.out.println("addQuestion" + e);
		}
		return 0;
	}

	/**
	 * Add completed questionnaires user responses
	 * @param respons
	 * @return
	 */
	public int addAnswers(String[] respons) {
		try {
			MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[1]);
			if (respons != null) {
				collection.insertOne(new Document("questionnaireid", 11).append("userid", 1)
						.append("respons", Arrays.asList(respons)).append("que_complete", true));
				return 1;
			}
		} catch (Exception e) {
			System.out.println("addQuestion" + e);
		}
		return 0;
	}
}

//added for dubging purpose
/*
 * class StartMongo { public static void main(String[] args) { MongoConnector
 * dbo = new MongoConnector(); dbo.InsertDocument(); dbo.getBasicQuestions();
 * for (org.bson.Document a : dbo.getBasicQuestions()) {
 * System.out.println(a.get("Question")); }
 * 
 * 
 * } }
 */
