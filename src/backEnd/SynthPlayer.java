package backEnd;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import synth.GuitarString;
import utilities.AudioUtilities;

public class SynthPlayer {
	private float[] data;
	public SynthPlayer(){
		data = new float[88200];
	}
	public void setData(float... freq){
		GuitarString gs = new GuitarString();
		for (int i = 0; i < freq.length; i++) {
			gs.createString(freq[i]);
			gs.pluck();
			mixTracks(data, gs.getBuffer());
		}
	}
	public void playSynth(){
		new Thread(new Runnable(){
			@Override
			public void run() {	
				final AudioTrack audioTrack;
				byte[] rawData = AudioUtilities.floatToByteArray(data);
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,4096,	AudioTrack.MODE_STREAM);
				audioTrack.play();	
				audioTrack.write(rawData,0,rawData.length);
			}	
		}).start();
	}
	public void mixTracks(float[] sum, float[] element ){
		for (int i = 0; i < element.length; i++) {
			sum[i] += element[i];
		}
	}
}	
