package com.survey.rmiinterface;

public enum PublicEnum {

	RANGE(new String[] { "< 18", " 18 =>", "18 <" }), SINGLEOPT(new String[] { "Yes", "No" }),
	JOB(new String[] { "Social", "Freelancing" }), EDULVL(new String[] { "OL", "AL", "Diploma", "HND", "Digree" }),
	RATING(new String[] { "Poor", "Good ", "Very Good", "Excelent" }),
	USEROPT(new String[] { "Ask people", "Search for it", "Reaserch" }), CHOICE(new String[] { "Take it", "Leave it" }),
	CHOICE2(new String[] { "Career", "Money" }), LOGICAL(new String[] { "A", "B", "C", "D" });

	private final String[] option;

	private PublicEnum(String[] optionEnum) {
		this.option = optionEnum;
	}

	/**
	 * @return the option
	 */
	public String[] getOption() {
		return option;
	}
	enum Option{
		RANGE,
		SINGLEOPT,
		JOB,
		EDULVL,
		USEROPT,
		CHOICE,
		CHOICE2,
		LOGICAL
	}

}
