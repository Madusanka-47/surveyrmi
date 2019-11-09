package com.perisic.beds.rmiinterface;

import java.io.Serializable;
import java.util.Hashtable;

import com.mongodb.MongoClient;
import com.perisic.beds.mongoclient.MongoConnector;
import com.perisic.beds.rmiinterface.PublicEnum.QuestionType;

/**
 * Question and how often the question was answered.
 * 
 * @author Marc Conrad
 *
 */
public class Question implements Serializable {
	/**
	 * A question together with the options that can be chosen to answer and the
	 * answer. This class must be available on both client and server.
	 */
	private static final long serialVersionUID = -7273230871957691871L;
	private String[] answers;
	private String questionText;
	private Hashtable<String, Integer> frequencies = new Hashtable<String, Integer>();

	/**
	 * A question is defined by the text and possible answers.
	 * 
	 * @param questionText Text of the questions.
	 * @param answers      Options to choose from.
	 */
	public Question(String questionText, String[] answers) {
		super();
		this.answers = answers;
		this.questionText = questionText;
	}

	/**
	 * What is the question?
	 * 
	 * @return the question.
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * What are the options?
	 * 
	 * @return all the possible answers from which to choose from.
	 */

	public String[] getAnswers() {
		return answers;
	}

	/**
	 * Hoq many was this answer choose to this specific question?
	 * 
	 * @param answer a specific answer.
	 * @return how often this answer was choosen.
	 */

	public int getFrequency(String answer) {
		Integer n = frequencies.get(answer);
		if (n == null)
			return 0;
		else
			return n;
	}

	/**
	 * The answer has been chosen. Increases the counter for this answer by one.
	 * 
	 * @param answer The answer that has been chosen.
	 */

	public void addAnswer(String answer) {
		int n = getFrequency(answer);
		frequencies.put(answer, n + 1);
	}
	
	/**
	 * validate the question type then transfer the value to the add method
	 * @param question
	 * @param type
	 */
	public void addNewQuestion(String question, String type) {

		/*switch ((QuestionType.valueOf(type))) {
		case Matching:
			// code block
			break;
		case MultipleChoice:
			// code block
			break;
		case Option:
			// code block
			break;
		case TrueFalse:
			// code block
			break;
		default:
			// code block
		}*/
		
		try {
			//TODO: This need to change properly
			String[] samples = {"Yes", "No", "Maybe"};
			int a  = new MongoConnector().addQuestion(question, samples);
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	public void addUserAnswers() {
		
	}
}
