package dataAndroid;

import java.lang.reflect.Field;

import org.example.rawinput.MainActivity;
import org.example.rawinput.R;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

public class Question {
	protected Context context;
	protected SoundPool sounds;
	protected int soundNumber,noteContain,position=1;
	protected float[][] questionResult;
	protected final String TYPE;
	
	/** 
	 * Construct a Question Sound Pool
	 * @param context Main application context
	 * @param questionType Type of Sound Pool. It must be <strong>n</strong> for notes or <strong>r</strong> for rhythm
	 * @param noteNumber Number that file contains different note, it must be <strong>0</strong> for rhythm
	 * 
	 * @return Question object that is based on SoundPool features
	 */
	@SuppressWarnings("deprecation")
	public Question(Context con,String questionType, final int noteNumber){ 
		MainActivity.status.post(new Runnable(){
			@Override
			public void run() {
				MainActivity.status.setTextColor(Color.YELLOW);
				MainActivity.status.setText("Wait !!!");
			}
		});	
		context = con;
		final Field[] fields = R.raw.class.getFields();
		soundNumber = fields.length; TYPE = questionType; noteContain = noteNumber;
		sounds = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		try {
			switch(TYPE){
				case "n":
					questionResult = new float[soundNumber][noteContain];
					break;
				case "r":
					questionResult = new float[soundNumber][];
			}
			int pos = 0;			
			for (int i = 0; i < fields.length ; i++) { 
				String name = context.getResources().getResourceEntryName(fields[i].getInt(fields[i]));
				String[] nameSplit = name.split("_");
				if(nameSplit[0].equals(TYPE) && nameSplit[1].equals(String.valueOf(noteNumber))){					
					sounds.load(context, fields[i].getInt(fields[i]), 1);			
					switch(TYPE){
						case "n":
							for (int j = 0; j < noteContain; j++) {
								questionResult[pos][j] = Float.valueOf(nameSplit[j+2]);
							}
							break;
						case "r":
							// functions of rhythm question results reading
							// questionResult object must be filled with rhythm results
							break;
					}
					pos++;
				} 				
			}
			if(pos>0){
				soundNumber = pos;
			} else {
				soundNumber = 0;
			}
			MainActivity.status.post(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(context, soundNumber + " files are loaded", Toast.LENGTH_SHORT).show();
					MainActivity.status.setTextColor(Color.GREEN);
					MainActivity.status.setText("Ready");
				}
			});
		} catch (IllegalAccessException | IllegalArgumentException e) {
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
	public int getNoteNumber() {
		return noteContain;
	}
	public float[] getQuestionResult(){
		return questionResult[position -1];
	}
	public String getQuestionType(){
		return TYPE;
	}
	public void next(){
		if(position == getSoundNumber()){
			Toast.makeText(context, "Maximum file is played", Toast.LENGTH_SHORT).show();
		} else {
			position++;			
		}
	}
	public void previous(){
		if(position == 1){
			Toast.makeText(context, "First question in the list", Toast.LENGTH_SHORT).show();
		} else {
			position--;			
		}
	}
}