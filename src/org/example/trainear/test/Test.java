package org.example.trainear.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import org.example.trainear.main.TetEar;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

public class Test {
	private AssetManager assets ;
	private Context context;
	private SoundPool sounds;
	private int soundNumber,noteContain,position=1;
	private final String TYPE, FOLDER;
	private String[] files;
	private HashMap<String, float[]> resultData;
	
	/** 
	 * Construct a Question Sound Pool
	 * @param context Main application context
	 * @param questionType Type of Sound Pool. It must be <strong>n</strong> for notes or <strong>r</strong> for rhythm
	 * @param noteNumber Number that file contains different note, it must be <strong>0</strong> for rhythm
	 * 
	 * @return Question object that is based on SoundPool features
	 */
	@SuppressWarnings("deprecation")
	public Test(Context con,String questionType, final int noteNumber) { 
		TetEar.status.post(new Runnable(){
			@Override
			public void run() {
				TetEar.status.setTextColor(Color.YELLOW);
				TetEar.status.setText("Wait !!!");
			}
		});	
		context = con; TYPE = questionType; noteContain = noteNumber;
		FOLDER = "audio/" + TYPE + "/" + String.valueOf(noteContain);
		assets = context.getAssets();
		try {
			resultData = deserialize(assets.open("data/"+TYPE+"_" + String.valueOf(noteContain)+".ser"));
			files = assets.list(FOLDER);
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i]);
			}
			soundNumber = files.length;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		sounds = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		try {
			for (int i = 0; i < files.length; i++) {
				sounds.load(assets.openFd(FOLDER+"/"+files[i]), 1);
			}
			TetEar.status.post(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(context, soundNumber + " files are loaded", Toast.LENGTH_SHORT).show();
					TetEar.status.setTextColor(Color.GREEN);
					TetEar.status.setText("Ready " + String.valueOf(position)+ " / " + String.valueOf(soundNumber));
				}
			});
		} catch ( IllegalArgumentException | IOException e) {
			e.printStackTrace();
			TetEar.status.post(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(context, "Error occured Ques, no file loaded", Toast.LENGTH_SHORT).show();
					TetEar.status.setTextColor(Color.RED);
					TetEar.status.setText("Error !!!");
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
	public int getNoteNumber() {
		return noteContain;
	}
	public float[] getQuestionResult(){
		return resultData.get(files[position-1]);
	}
	public String getQuestionType(){
		return TYPE;
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
		TetEar.status.post(new Runnable(){
			@Override
			public void run() {
				TetEar.status.setText("Ready " + String.valueOf(position) + " / " + String.valueOf(soundNumber));
			}
		});
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, float[]> deserialize(InputStream path){
        ObjectInputStream in;
        HashMap<String, float[]> newData = null;
		try {	        
	        in = new ObjectInputStream(path);
	        newData = (HashMap<String, float[]> ) in.readObject();
	        in.close();
	        path.close();
		}
		catch(Exception ex){
			Toast.makeText(context, "Data file cannot be read", Toast.LENGTH_SHORT).show();
		}
		return newData;
	}
}