package com.survey.rmiinterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.survey.mongoclient.SurveyAnswerService;
import com.survey.mongoclient.SurveyQuestionService;
import com.survey.rmiinterface.PublicEnum.Option;

public class Question implements Serializable {

	private static final long serialVersionUID = -7273230871957691871L;
	private String[] answers;
	private String questionText;
	private Hashtable<String, Integer> frequencies = new Hashtable<String, Integer>();
	
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

	public int getFrequency(String answer) {
		Integer n = frequencies.get(answer);
		if (n == null)
			return 0;
		else
			return n;
	}
	
	public void addAnswer(String answer) {
		int n = getFrequency(answer);
		frequencies.put(answer, n + 1);
		SurveyAnswerService aswr = new SurveyAnswerService();
		aswr.addToAnswerPane(answer, 1, 1 );
	}
	
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
			SurveyQuestionService svy = new SurveyQuestionService();
			svy.addToQuestionPane(questionList, optionList);

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}


/**
 * Debug main for SurveyQuestionService 
 * Remove once the implementaion completed
 * */
class QuestService {
	public static void main(String[] args) {
       
        
	}
}