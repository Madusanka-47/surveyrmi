package com.perisic.beds.mongoclient;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
//import com.mongodb.MongoClientURI;
//import com.mongodb.ServerAddress;
//
//import java.util.Arrays;
//import java.util.ArrayList;
//import java.util.List;

/**
 * MongoDb connection for the questionnaire service
 * connection will be made into cluster0-yx3nh.mongodb.net cluster.
 * @author Madusanka47
 *
 */
public class MongoConnector {

	public MongoConnector() {
		try {

			MongoClientURI uri = new MongoClientURI(
					"mongodb+srv://dumalk:dumalk@cluster0-yx3nh.mongodb.net/test?retryWrites=true&w=majority");

			@SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient(uri);
			MongoDatabase database = mongoClient.getDatabase("survey_core");

//			MongoClient mongo = new MongoClient(
//					"mongodb+srv://dumalk:dumalk@cluster0-yx3nh.mongodb.net/shuttle_core?retryWrites=true&w=majority");
//			DB db = mongo.getDB("shuttle_core");
			MongoCollection<org.bson.Document> collection = database.getCollection("question_pane");
			FindIterable<org.bson.Document> cus = collection.find();
			for (org.bson.Document a : cus.collation(null)) {
				System.out.println(a.get("title"));
			}
			InsertDocument(collection);

		} catch (Exception e) {

			System.out.print(e);
		}
	}
	public void InsertDocument(MongoCollection<org.bson.Document> collection) {
		
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 10; i++) {
		    documents.add(new Document("i", i));
		}
		collection.insertMany(documents);
	}
}

class StartMongo {
	public static void main(String[] args) {
		MongoConnector dbo = new MongoConnector();

	}
}
