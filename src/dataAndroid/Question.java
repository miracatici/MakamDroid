package dataAndroid;

import java.lang.reflect.Field;

import org.example.rawinput.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

public class Question {
	private Context context;
	private SoundPool sounds;
	private int soundNumber,noteContain,position=1;
	private float[][] questionResult;
	public final String TYPE;
	
	/** 
	 * Construct a Question Sound Pool
	 * @param context Main application context
	 * @param questionType Type of Sound Pool. It must be <strong>n</strong> for notes or <strong>r</strong> for rhythm
	 * @param noteNumber Number that file contains different note, it must be <strong>0</strong> for rhythm
	 * 
	 * @return Question object that is based on SoundPool features
	 */
	@SuppressWarnings("deprecation")
	public Question(Context con,String questionType, int noteNumber){ 
		
		Field[] fields = R.raw.class.getFields();
		context = con;
		soundNumber = fields.length; TYPE = questionType; noteContain = noteNumber;
		sounds = new SoundPool(1,AudioManager.STREAM_MUSIC,0);

		switch(TYPE){
			case "n":
				questionResult = new float[soundNumber][noteContain];
				break;
			case "r":
				questionResult = new float[soundNumber][];
		}
		int pos = 0;
		try {
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
							// rhythm analysis function
							// questionResult object must be filled with rhythm results
							// questionResult[pos] = analysis of rhythm 
							break;
					}
					pos++;
				} 				
			}
			if(pos>0){
				soundNumber = pos;				
				Toast.makeText(context, soundNumber + " files loaded to pool", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "No files loaded", Toast.LENGTH_SHORT).show();
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	public int play(){  // soundID is standard integer indexes. It's not RawID or any specific number
		int streamID = 0;
		try {
			streamID = sounds.play(position, 0.9f, 0.9f, 1, 0, 1f);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return streamID;
	}
	public void stop(int soundID){
		try {
			sounds.stop(soundID);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
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
