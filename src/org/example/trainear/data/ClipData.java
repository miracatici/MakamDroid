package org.example.trainear.data;

import java.io.Serializable;

public class ClipData implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float[] freqAnswer;
	private String theoryAnswer, name;
	private String[] optionList;
	
	public ClipData(String nam, String ans, float[] ans2, String[] list){
		name = nam; theoryAnswer = ans; freqAnswer = ans2; optionList = list;
	}

	public float[] getFreqAnswer() {
		return freqAnswer;
	}
	public String getTheoryAnswer() {
		return theoryAnswer;
	}
	public String getName() {
		return name;
	}
	public String[] getOptionList() {
		return optionList;
	}
}
