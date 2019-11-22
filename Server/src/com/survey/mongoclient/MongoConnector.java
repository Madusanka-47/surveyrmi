package com.survey.mongoclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

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

	// public void addToQuestionPane
	public void addToQuestionPane(ArrayList<String> questions, List<String[]> options) {
		try {
			MongoCollection<Document> collection = this.database.getCollection(this.tab_collection[0]);
			long quseId = collection.countDocuments();
			List<Document> questionSet = new ArrayList<Document>();
			for (int i = 0; i < questions.size(); i++) {
				questionSet.add(new Document("quesid", quseId + i).append("description", questions.get(i))
						.append("ques_type", Arrays.asList(options.get(i))).append("basic", false));
			}
			collection.insertMany(questionSet);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public ArrayList<Document> getBasicQuestions() {
		ArrayList<Document> questionDoc = null;
		try {

			questionDoc = new ArrayList<Document>();
			MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[0]);
			FindIterable<org.bson.Document> cus = collection.find();
			for (org.bson.Document a : cus.collation(null)) {
				questionDoc.add(new Document("Question", a.get("description")).append("Answer", a.get("ques_type")));
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}

		return questionDoc;
	}

	public int updateQuestionPane(String description, int quesId) {
		try {
			if (!description.isEmpty()) {
				MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[0]);
				collection.updateOne(eq("quesid", quesId), new Document("$set", new Document("description", description)));
				return 1;
			} else {
				throw new Exception("Empty description");
			}

		} catch (Exception ex) {
			System.out.println(ex);
			return 0;
		}

	}

	public int addQuestion(String ques, String[] answertype) {
		try {
			MongoCollection<org.bson.Document> collection = this.database.getCollection(this.tab_collection[0]);
			if (ques != null && answertype != null) {
				collection.insertOne(new Document("quesid", 11).append("description", ques)
						.append("ques_type", Arrays.asList(answertype)).append("basic", false)
						.append("questionnaireid", 1));
				return 1;
			}
		} catch (Exception e) {
			System.out.println("addQuestion" + e);
		}
		return 0;
	}

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

// added for dubging purpose

class StartMongo {
	public static void main(String[] args) {

		MongoConnector dbo = new MongoConnector();
		dbo.updateQuestionPane("");
		// String a[] = PublicEnum.NETBOOK.getName2();
		// System.out.print(a[1]);

		// dbo.InsertDocument();
		// dbo.getBasicQuestions();
		// for (org.bson.Document a : dbo.getBasicQuestions()) {
		// List<String> quesOptions = new ArrayList<String>();
		// quesOptions = (List<String>) a.get("Answer");
		// for (Object option : quesOptions) {
		// System.out.println(option.toString());
		// }

		// }

	}
}