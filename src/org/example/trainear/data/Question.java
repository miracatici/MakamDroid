package org.example.trainear.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

import org.example.trainear.MainActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

public class Question {
	private Context context;
	private SoundPool sounds;
	private int soundNumber,position=1;
	private String[] files;
	private HashMap<String, ClipData> clipDatas;
	
	/** 
	 * Construct a Question Sound Pool
	 * @param context Main application context
	 * @param questionType Type of Sound Pool. It must be <strong>n</strong> for notes or <strong>r</strong> for rhythm
	 * @param noteNumber Number that file contains different note, it must be <strong>0</strong> for rhythm
	 * 
	 * @return Question object that is based on SoundPool features
	 */
	@SuppressWarnings("deprecation")
	public Question(Context con,String questionType, final int noteNumber) { 
		MainActivity.status.post(new Runnable(){
			@Override
			public void run() {
				MainActivity.status.setTextColor(Color.YELLOW);
				MainActivity.status.setText("Wait !!!");
			}
		});	
		sounds = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		context = con; 
		String FOLDER = "audio/" + questionType + "/" + String.valueOf(noteNumber);
		AssetManager assets = context.getAssets();
		try {
			clipDatas =  deserialize(assets.open(("data/"+questionType+"_" + String.valueOf(noteNumber)+".ser"),AssetManager.ACCESS_BUFFER));
			files = assets.list(FOLDER);
			soundNumber = files.length;
			for (int i = 0; i < files.length; i++) {
				sounds.load(assets.openFd(FOLDER+"/"+files[i]), 1);
			}
			MainActivity.status.post(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(context, soundNumber + " files are loaded", Toast.LENGTH_SHORT).show();
					MainActivity.status.setTextColor(Color.GREEN);
					MainActivity.status.setText("Ready " + String.valueOf(position)+ " / " + String.valueOf(soundNumber));
				}
			});
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
			MainActivity.status.post(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(context, "Error occured Ques, no file loaded", Toast.LENGTH_SHORT).show();
					MainActivity.status.setTextColor(Color.RED);
					MainActivity.status.setText("Error !!!");
				}
			});
		}			
	}
	public int play(){  // soundID is standard integer indexes. It's not RawID or any specific number
		int streamID = 0;
		try {
			streamID = sounds.play(position, 0.9f, 0.9f, 1, 0, 1f);
		} catch (IllegalArgumentException e) {
			Toast.makeText(context, "Sound file couldn't played", Toast.LENGTH_SHORT).show();
		}
		return streamID;
	}
	public void stop(int soundID){
		try {
			sounds.stop(soundID);
		} catch (IllegalArgumentException e) {
			Toast.makeText(context, "Sound file couldn't stopped", Toast.LENGTH_SHORT).show();
		} 
	}
	public int getSoundNumber() {
		return soundNumber;
	}
	public float[] getQuestionResult(){
		return clipDatas.get(files[position-1]).getFreqAnswer();
	}
	public String getTheoryAnswer(){
		return clipDatas.get(files[position-1]).getTheoryAnswer();
	}
	public String[] getOption(){
		return clipDatas.get(files[position-1]).getOptionList();
	}
	public void next(){
		if(position == getSoundNumber()){
			Toast.makeText(context, "Last question in the list", Toast.LENGTH_SHORT).show();
		} else {
			position++;			
		}
		setPositionText();
	}
	public void previous(){
		if(position == 1){
			Toast.makeText(context, "First question in the list", Toast.LENGTH_SHORT).show();
		} else {
			position--;			
		}
		setPositionText();
	}
	public void setPositionText(){
		MainActivity.status.post(new Runnable(){
			@Override
			public void run() {
				MainActivity.status.setText("Ready " + String.valueOf(position) + " / " + String.valueOf(soundNumber));
			}
		});
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, ClipData> deserialize(InputStream is) throws StreamCorruptedException, IOException, ClassNotFoundException{
		ObjectInput oi = null;
		HashMap<String, ClipData> data  = null;
		oi = new ObjectInputStream(is);
		data =  (HashMap<String, ClipData>) oi.readObject();
        oi.close();
        is.close();
        return data;
	}
}