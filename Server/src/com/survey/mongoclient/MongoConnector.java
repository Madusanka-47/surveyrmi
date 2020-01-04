package com.survey.mongoclient;

import java.net.UnknownHostException;

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
public class MongoConnector extends MongoClient {
	private static MongoDatabase instance = null;

	protected MongoConnector() throws UnknownHostException, UnsupportedOperationException {

	}
	public static synchronized MongoDatabase getInstance() throws UnknownHostException {

		if (instance == null) {
			instance = new MongoClient(new MongoClientURI(
					"mongodb+srv://dumalk:dumalk@cluster0-yx3nh.mongodb.net/test?retryWrites=true&w=majority"))
							.getDatabase("survey_core");
		}

		return instance;
	}
}