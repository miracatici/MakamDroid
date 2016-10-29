package org.example.trainear.backEnd;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.example.trainear.utilities.AudioUtilities;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class Answer {
	private boolean isRunning;
	private byte[] rawData;
	private ByteArrayOutputStream out; 									// Stream for byte array output
	private AudioRecord recorder;
	private AudioTrack audioTrack;

	public Answer(){
		
	}
	public void startRecord(){
		final int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, 
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);
		new Thread (new Runnable(){
			@Override
			public void run() {
				byte[] buffer = new byte[bufferSize];
				out = new ByteArrayOutputStream();					// Creating stream to able to write buffer pack
				recorder.startRecording();
				isRunning = true;									// Is recording?
				try {
					while (isRunning) {
						int count = recorder.read(buffer,0, 			// Reading samples to buffer 
												 bufferSize);
						if (count > 0) {								// If reading is true, write to output stream
							out.write(buffer, 0, count);
						}
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void stopRecord(){
		recorder.stop();
		recorder.release();
		recorder = null;
		isRunning = false;
		rawData = out.toByteArray();
	}
	public void startPlay(){
		new Thread(new Runnable(){
			@Override
			public void run() {	
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,4096,	AudioTrack.MODE_STREAM);
				audioTrack.play();	
				audioTrack.write(rawData,0,rawData.length);
			}	
		}).start();
	}
	public void stopPlay(){
		audioTrack.stop();
		audioTrack.release();
	}
	public float[] analyze(int noteNumber){
		float[] answerResult = null;
		switch(noteNumber){
			case 0:
				answerResult = new float[10];
				//rhythm analysis methods comes here
				break;
			default :
				PitchDetection pd = new PitchDetection(AudioUtilities.byteToFloatArray(rawData),44100);
				answerResult = pd.getChunkResults(noteNumber);
		}
		return answerResult;
	}
}
