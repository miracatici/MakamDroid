package org.example.rawinput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import be.tarsos.dsp.pitch.Yin;
import dataAndroid.Question;
import fileChooser.FileChooser;
import utilities.AudioUtilities;

public class MainActivity2 extends Activity {
	private TextView txtPitch; 
	private Button btnStart, btnStop, btnSelect, btnPlay, btnPStop, btnPlayWav, btnStopWav, btnSelPlay, 
					btnSelQ, btnPlayQ,btnNext, btnRecA, btnAnalyze;
	private ImageButton nextTab,graph;
//	private TextView res1, res2, res3, ans1, ans2, ans3;
	private Yin yin; 
	private AudioRecord recorder; 
	private boolean isRunning;
	private final int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
	private MediaPlayer mpintro; 
	private AudioTrack at; 
	private Field[] fields; 
	private int position = 1; 
	final int FILE_CHOOSER=1;
	Question q1set;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setProperties();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void setProperties(){
		fields = R.raw.class.getFields();
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, 
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);
//		btnStart = (Button) findViewById(R.id.btnStart);
//		btnStop = (Button) findViewById(R.id.btnStop);
//		btnPlay = (Button) findViewById(R.id.btnPlay);
//		btnSelect = (Button) findViewById(R.id.btnSelect);
//		btnPStop = (Button) findViewById(R.id.btnPStop);
//		btnPlayWav = (Button) findViewById(R.id.btnPlayWav);
//		btnStopWav = (Button) findViewById(R.id.btnStopWav);
		btnSelQ = (Button) findViewById(R.id.btnSelQ);
		btnPlayQ = (Button) findViewById(R.id.btnPlayQ);
		btnRecA = (Button) findViewById(R.id.btnRecA);
		btnAnalyze = (Button) findViewById(R.id.btnAnalyze);
		btnNext = (Button) findViewById(R.id.btnNext);

//		nextTab = (ImageButton) findViewById(R.id.nextTab);
//		txtPitch = (TextView) findViewById(R.id.txtPitch);
//		btnSelPlay = (Button) findViewById(R.id.btnSelPlay);
//		graph = (ImageButton) findViewById(R.id.graph);
		graph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				graph(v);
			}
		});
		yin = new Yin(44100,bufferSize/2,0.1);	isRunning = false;
		
		nextTab.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sendMessage(v);
			}
		});
		btnStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				Toast.makeText(MainActivity2.this, "Basliyor", Toast.LENGTH_SHORT).show();
				startTracking();
			}
		});
		btnStop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				recorder.stop();
				isRunning = false;
				Toast.makeText(MainActivity2.this, "Durduruldu", Toast.LENGTH_SHORT).show();
			}
		});
		btnPlay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity2.this, "Basliyor", Toast.LENGTH_SHORT).show();
				mpintro.start();
				btnPStop.setEnabled(true);
				btnPlay.setEnabled(false);
			}
		});
		btnPStop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mpintro.pause();
				btnPStop.setEnabled(false);
				btnPlay.setEnabled(true);
				Toast.makeText(MainActivity2.this, "Durduruldu", Toast.LENGTH_SHORT).show();
			}
		});
		btnSelect.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					audioPlayer(fields[position].getInt(fields[position])); //fields[position].getInt((fields[position])) R.raw.mrc
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				position ++;
			}
		});
		btnPlayWav.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					btnStopWav.setEnabled(true);
					btnPlayWav.setEnabled(false);
					Toast.makeText(MainActivity2.this, "Basliyor", Toast.LENGTH_SHORT).show();
					readWav(Environment.getExternalStorageDirectory()+"/Download/test.mp3/");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnStopWav.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				at.pause();
				btnStopWav.setEnabled(false);
				btnPlayWav.setEnabled(true);
				Toast.makeText(MainActivity2.this, "Durduruldu", Toast.LENGTH_SHORT).show();
			}
		});
		btnSelPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mpintro.start();
			}
		});
		btnSelQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSelQ.setEnabled(false);
				q1set = new Question(MainActivity2.this,"n",1);
			}
		});
		btnPlayQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayQ.getText().toString()) {
					case "Play Q":
						btnPlayQ.setText("Stop Q");
						// Question Play Function
						q1set.play(position);
						break;
					case "Stop Q" :
						btnPlayQ.setText("Play Q");
						// Stop function
						q1set.stop(position);
						break;
				}
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				q1set.stop(position);
				position++;
				btnPlayQ.setText("Play Q");
				if(position == q1set.getSoundNumber()){
					btnNext.setEnabled(false);
					Toast.makeText(MainActivity2.this, "Maximum files is played", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnRecA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnRecA.getText().toString()) {
					case "Rec A":
						btnRecA.setText("Stop A");;
						// Recording Function
						break;
					case "Stop A" :
						btnRecA.setText("Rec A");
						// Stop function
						// Object create function
						break;						
				}
			}
		});
		btnAnalyze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Analyze and comparison function
				// Function may be in the Answer Class
			}
		});
	}
	private void send(final float freq){
		txtPitch.post(new Runnable(){
			@Override
			public void run() {
				if(freq>0){
					txtPitch.setText(String.valueOf(freq));
				}
			}	
		});	
	}
	private void readWav(final String path) throws IOException{
		new Thread(new Runnable(){
			@Override
			public void run() {
				FileInputStream is;
				File temp;
				try {
					temp = AudioUtilities.convertMP3toWAV(path);
					is = new FileInputStream(temp);
					at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,4096,	AudioTrack.MODE_STREAM);
					byte[] buffer  = new byte[4096];
					at.play();
					while(is.available()>0){
						is.read(buffer);
						at.write(buffer,0,buffer.length);
					}
					is.close();				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}).start();
	}
	private void startTracking(){
		new Thread (new Runnable(){
			@Override
			public void run() {
				byte[] buffer = new byte[bufferSize];
				recorder.startRecording();
				isRunning = true;									// Is recording?
				while (isRunning) {
					int count = recorder.read(buffer,0, 			// Reading samples to buffer 
											 bufferSize);
					if (count > 0) {								// If reading is true, write to output stream
						float result = yin.getPitch(AudioUtilities.byteToFloatArray(buffer)).getPitch();
						send(result);
					}
				}							
			}
		}).start();
	}
	private void audioPlayer(final Object path){
		btnPlay.setEnabled(true);
		if(mpintro!=null){
			mpintro.release();
			mpintro = null;
		}
		try {
			if(path instanceof String){
				mpintro = new MediaPlayer();
				playerListener();
				mpintro.setDataSource((String)path);
				mpintro.prepareAsync();
			} else if (path instanceof Integer){
				mpintro = MediaPlayer.create(MainActivity2.this, (Integer) path);
				playerListener();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 			
	}
	private void playerListener(){
		mpintro.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				System.out.println("Hazirlandi");
				System.out.println(mp.getDuration());
			}
		});
		mpintro.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
				System.out.println("Sarki bitti");
				btnPStop.post(new Runnable(){
					@Override
					public void run() {
						btnPStop.performClick();
					}
				});
            }
        });
		mpintro.setOnErrorListener(new OnErrorListener(){
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				try {
					System.out.println("Hata Verdim");
					mp.release();
					mp = null;
					return true;
				} catch (Exception e){
					return false;
				}
			}
		});	
	}
	public void sendMessage(View view) {
		Intent intent = new Intent(this, FileChooser.class);
		startActivityForResult(intent, FILE_CHOOSER);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
	        String fileSelected = data.getStringExtra("fileSelected");
	        audioPlayer(fileSelected);
	        Toast.makeText(this, "file selected "+fileSelected, Toast.LENGTH_SHORT).show();
	        FileChooser.currentDir = new File(fileSelected);
	    }                   
	}
	public void graph(View view){
//		Intent intent2 = new Intent(this,SecondActivity.class);
//		startActivity(intent2);
	}
}
