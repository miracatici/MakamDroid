package org.example.trainear.synth;

import java.util.HashMap;

import org.example.trainear.data.SynthData;
import org.example.trainear.utilities.AudioUtilities;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.TextView;

public class SynthPlayer {
	private int noteNumber, position = 0;
	private float duration;
	private float[] audioData, intervalData, freqData;
	private HashMap<String,SynthData> synthData;
	private String name;
	private AudioTrack audioTrack;
	private TextView status;
	
	public SynthPlayer(final TextView page,float duration){
		this.duration = duration;
		status = page;
		status.post(new Runnable(){
			@Override
			public void run() {
				status.setTextColor(Color.YELLOW);
				status.setText("Wait !!!");
			}
		});	
	}
	public void playSynth(String type){
		setData(type);
		new Thread(new Runnable(){
			@Override
			public void run() {	
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,4096,	AudioTrack.MODE_STREAM);
				byte[] rawData = AudioUtilities.floatToByteArray(audioData);
				audioTrack.play();	
				audioTrack.write(rawData,0,rawData.length);
			}	
		}).start();
	}
	public void stopSynth(){
		if(audioTrack.getState()== AudioTrack.STATE_INITIALIZED){
			audioTrack.stop();
			audioTrack.release();
		}
	}
	public float[] getQuestionResult(){
		return freqData; //synthData.get(name).getFreqAnswer();
	}
	public String getTheoryAnswer(){
		return synthData.get(name).getTheoryAnswer();
	}
	public String[] getOption(){
		return synthData.get(name).getOptionList();
	}

	public void setNoteNumber(int number){
		this.noteNumber = number;
	}
	public void setInterval(float[] interval){
		intervalData = interval.clone();
		freqData = new float[noteNumber];
		for (int i = position; i < position + noteNumber; i++) {
			freqData[i-position] = AudioUtilities.centToHertz(intervalData[i]);
		}
		status.post(new Runnable(){
			@Override
			public void run() {
				status.setTextColor(Color.GREEN);
				status.setText("Ready :)");
			}
		});	
	}
	public void previous(){
		if(position == 0){
			 
		} else {
			position--;
			freqData = new float[noteNumber];
			for (int i = position; i < position + noteNumber; i++) {
				freqData[i-position] = AudioUtilities.centToHertz(intervalData[i]);
			}
		}		
	}
	public void next(){
		if(position == intervalData.length - noteNumber){
			 
		} else {
			position++;
			freqData = new float[noteNumber];
			for (int i = position; i < position + noteNumber; i++) {
				freqData[i-position] = AudioUtilities.centToHertz(intervalData[i]);
			}
		}	
	}
	private float[] mixTracks(float[] sum, float[] element ){  // Play mix down of notes
		if (sum == null){
			sum = element.clone();
		} else {
			for (int i = 0; i < element.length; i++) {
				sum[i] += element[i];
			}
		}
		return sum;
	}
	private float[] seqTracks(float[] sum, float[] element){ // Play notes sequentially
		float[] newSum;
		if(sum == null){
			newSum = element.clone();
		} else {
			newSum = new float[sum.length+element.length];
			System.arraycopy(sum, 0, newSum, 0, sum.length);
			System.arraycopy(element, 0, newSum, sum.length, element.length);
		}
		return newSum;
	}
	public void setData(String type){
		if(audioData != null){
			audioData = null;
		}
		GuitarString gs = new GuitarString(duration);
		for (int i = 0; i < freqData.length; i++) {
			gs.createString(freqData[i]);
			gs.pluck();
			if(type.equals("seq") || type.equals("SEQ")){
				audioData = seqTracks(audioData,gs.getBuffer());
			} else if (type.equals("mix") || type.equals("MIX")) {				
				audioData = mixTracks(audioData, gs.getBuffer());
			} else {
				System.out.println("problem set data");
				throw new IllegalArgumentException("Player type must be SEQuential or MIX");
			}
		}
	}
	public void setPosition(int pos){
		this.position = pos;
	}
}	
