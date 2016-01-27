package org.example.trainear.synth;

import java.util.HashMap;

import org.example.trainear.data.SynthData;
import org.example.trainear.main.MicroEar;
import org.example.trainear.utilities.AudioUtilities;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SynthPlayer {
	private float[] data;
	private HashMap<String,SynthData> synthData;
	private String name;
	private AudioTrack audioTrack;
	
	public SynthPlayer(){
		MicroEar.status.post(new Runnable(){
			@Override
			public void run() {
				MicroEar.status.setTextColor(Color.YELLOW);
				MicroEar.status.setText("Wait !!!");
			}
		});	
	}
	public void setData(String type, float... freq){
		if(data != null){
			data = null;
		}
		GuitarString gs = new GuitarString();
		for (int i = 0; i < freq.length; i++) {
			gs.createString(freq[i]);
			gs.pluck();
			if(type.equals("seq") || type.equals("SEQ")){
				data = seqTracks(data,gs.getBuffer());
			} else if (type.equals("mix") || type.equals("MIX")) {				
				data = mixTracks(data, gs.getBuffer());
			} else {
				throw new IllegalArgumentException("Player type must be SEQuential or MIX");
			}
		}
		MicroEar.status.post(new Runnable(){
			@Override
			public void run() {
				MicroEar.status.setTextColor(Color.GREEN);
				MicroEar.status.setText("Ready :)");
			}
		});	
	}
	public void playSynth(){
		new Thread(new Runnable(){
			@Override
			public void run() {	
				byte[] rawData = AudioUtilities.floatToByteArray(data);
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,4096,	AudioTrack.MODE_STREAM);
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
		return new float[]{330,440}; //synthData.get(name).getFreqAnswer();
	}
	public String getTheoryAnswer(){
		return synthData.get(name).getTheoryAnswer();
	}
	public String[] getOption(){
		return synthData.get(name).getOptionList();
	}
	private float[] mixTracks(float[] sum, float[] element ){  // Play mix down of notes
		if (sum == null){
			sum = element.clone();
		}
		for (int i = 0; i < element.length; i++) {
			sum[i] += element[i];
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
}	
