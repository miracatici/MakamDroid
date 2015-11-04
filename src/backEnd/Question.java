package backEnd;

import java.lang.reflect.Field;

import org.example.rawinput.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) 
public class Question {
	
	private SoundPool sounds;
	private int soundNumber;
	private int noteContain;
	private String[][] questionResult;
	
	public Question(Context context, int noteNumber){
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			createNewSoundPool();
//		} else {
			createOldSoundPool();
//		}
		Field[] fields = R.raw.class.getFields();
		noteContain = noteNumber; soundNumber = fields.length;
		questionResult = new String[soundNumber][noteContain];
		int pos=0;
		try {
			for (int i = 0; i < fields.length ; i++) { 
				String name = context.getResources().getResourceEntryName(fields[i].getInt(fields[i]));
				String[] nameSplit = name.split("_");
				if(nameSplit[0] == "n" && nameSplit[1]==String.valueOf(noteContain)){					
					sounds.load(context, fields[i].getInt(fields[i]), 1);
					for (int j = 0; j < noteContain; j++) {
						questionResult[pos][j] = nameSplit[j+2];
					}
					System.out.println(name + " eklendi");
					pos++;
				}
			}
		} catch (IllegalAccessException e) {
			System.out.println("Hata1");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("Hata2");
			e.printStackTrace();
		}
	}
	public void play(int soundID){  // soundID is standard integer indexes. It's not RawID or any specific number
		if (soundID<=soundNumber){
			try {
				sounds.play(soundID, 0.8f, 0.8f, 1, 0, 1f); 
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("Hata3");
			} 
		} else {
			System.out.println("Index exceed limit : Field, play method");
		}
	}
	public void stop(int soundID){
		if (soundID<=soundNumber){
			try {
				sounds.stop(soundID);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("Hata4");
			} 
		} else {
			System.out.println("Index exceed limit : Field, stop method");
		}
	}
//	protected void createNewSoundPool(){
//		AudioAttributes attributes = new AudioAttributes.Builder()
//				.setUsage(AudioAttributes.USAGE_GAME)
//				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//				.build();
//		sounds = new SoundPool.Builder()
//				.setAudioAttributes(attributes)
//				.build();
//	}
	@SuppressWarnings("deprecation")
	protected void createOldSoundPool(){
		sounds = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
	}
}
