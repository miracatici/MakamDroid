package dataAndroid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import utilities.AudioUtilities;

public class Answer {
	private final int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
	private boolean isRunning;
	private byte[] rawData;
	private ByteArrayOutputStream out; 													// Stream for byte array output
	private AudioRecord recorder;
	private AudioTrack audioTrack;

	public Answer(){
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, 
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);
	}
	public void startRecord(){
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
	}
	public float[] analyze(int noteNumber){
		PitchDetection pd = new PitchDetection(AudioUtilities.byteToFloatArray(rawData),1764);
		float[] pr = pd.getPitchResult();
		float[][] prc = pd.chunkPitchTrack(pr);
		float[][] prcl = pd.pickLongChunks(prc, noteNumber);
		float[] answerResult = new float[noteNumber];
		for (int i = 0; i < prcl.length; i++) {
			answerResult[i] = AudioUtilities.findMedian(prcl[i]);
		}
		return answerResult;
	}
}
