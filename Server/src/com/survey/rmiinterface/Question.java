package com.survey.rmiinterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.text.Document;

// import com.mongodb.MongoClient;
import com.survey.mongoclient.MongoConnector;
// import com.survey.rmiinterface.PublicEnum.QuestionType;
import com.survey.rmiinterface.PublicEnum.Option;

public class Question implements Serializable {

	private static final long serialVersionUID = -7273230871957691871L;
	private String[] answers;
	private String questionText;
	// private Hashtable<String, Integer> frequencies = new Hashtable<String,
	// Integer>();

	public Question(String questionText, String[] answers) {
		super();
		this.answers = answers;
		this.questionText = questionText;
	}

	public String getQuestionText() {
		return questionText;
	}

	public String[] getAnswers() {
		return answers;
	}
	// public void addNewQuestion(String question, String type) {

	// /*switch ((QuestionType.valueOf(type))) {
	// case Matching:
	// // code block
	// break;
	// case MultipleChoice:
	// // code block
	// break;
	// case Option:
	// // code block
	// break;
	// case TrueFalse:
	// // code block
	// break;
	// default:
	// // code block
	// }*/

	// try {
	// //TODO: This need to change properly
	// String[] samples = {"Yes", "No", "Maybe"};
	// int a = new MongoConnector().addQuestion(question, samples);

	// } catch (Exception e) {
	// // TODO: handle exception
	// }

	// }

	public void addQuestionToSurvey(String questionDesc, String option) {

		List<String[]> optionList = new ArrayList<>();
		ArrayList<String> questionList = new ArrayList<String>();
		PublicEnum opt_ = null;
		switch (Option.valueOf(option)) {
		case RANGE:
			opt_ = PublicEnum.RANGE;
			break;
		case SINGLEOPT:
			opt_ = PublicEnum.SINGLEOPT;
			break;
		case JOB:
			opt_ = PublicEnum.JOB;
			break;
		case EDULVL:
			opt_ = PublicEnum.EDULVL;
			break;
		case USEROPT:
			opt_ = PublicEnum.USEROPT;
			break;
		case CHOICE:
			opt_ = PublicEnum.CHOICE;
			break;
		case CHOICE2:
			opt_ = PublicEnum.CHOICE2;
			break;
		case LOGICAL:
			opt_ = PublicEnum.LOGICAL;
			break;
		}

		if (opt_ != null && questionDesc != null){
			questionList.add(questionDesc);
			optionList.add(opt_.getOption());
		}
		try {
			MongoConnector dbo = new MongoConnector();
			dbo.addToQuestionPane(questionList, optionList);

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}


// class QueTest {
// 	public static void main(String[] args) {
// 		addQuestionToSurvey("I am in", "CHOICE2")

// 	}
// }