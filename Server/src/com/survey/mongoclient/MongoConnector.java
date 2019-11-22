package com.survey.mongoclient;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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

	public MongoDatabase getConnection() {
		return this.database;
	}
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */
class StartMongo {
	public static void main(String[] args) {
		MongoConnector dbo = new MongoConnector();
	}
}