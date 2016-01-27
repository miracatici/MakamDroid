package org.example.trainear.data;

import java.io.Serializable;

public class SynthData implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float[] freqAnswer;
	private String theoryAnswer;
	private String[] optionList;
	
	public SynthData(String ans, float[] ans2, String[] list){
		theoryAnswer = ans; freqAnswer = ans2; optionList = list;
	}

	public float[] getFreqAnswer() {
		return freqAnswer;
	}
	public String getTheoryAnswer() {
		return theoryAnswer;
	}
	public String[] getOptionList() {
		return optionList;
	}
}
