package org.example.trainear.main;

import java.io.IOException;

import org.example.trainear.R;
import org.example.trainear.backEnd.AudioRead;
import org.example.trainear.menu.FileChooser;
import org.example.trainear.synth.SynthPlayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SongEar extends Activity {
	public static SynthPlayer question;
	private TextView songName, peakRes;
	public static TextView statusS;
	public static int NN = 2;
	public static float[] peaks;
	public static byte[] rawData;
	final int FILE_CHOOSER=1;
	private ImageView resImg1S,resImg2S,resImg3S,resImg4S,resImg5S;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_ear);
		setProps();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.song_ear, menu);
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
	private void setProps(){
		resImg1S = (ImageView) findViewById(R.id.resImg1S);
		resImg2S = (ImageView) findViewById(R.id.resImg2S);
		resImg3S = (ImageView) findViewById(R.id.resImg3S);
		resImg4S = (ImageView) findViewById(R.id.resImg4S);
		resImg5S = (ImageView) findViewById(R.id.resImg5S);
		statusS = (TextView) findViewById(R.id.statusS);
		question = new SynthPlayer(statusS,1f);
		songName = (TextView) findViewById(R.id.songName);
		peakRes = (TextView) findViewById(R.id.peakRes);
		final Button btnPlayS = (Button) findViewById(R.id.btnPlayS);
		Button btnNextS = (Button) findViewById(R.id.btnNextS);
		btnNextS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				question.stopSynth();
				question.next();
				btnPlayS.setText("Play");
				setResultImage(0,0,0,0);			
			}
		});
		Button btnPrevS = (Button) findViewById(R.id.btnPrevS);
		btnPrevS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				question.stopSynth();
				question.previous();
				btnPlayS.setText("Play");
				setResultImage(0,0,0,0);			
			}
		});
		btnPlayS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(statusS.getText().equals("WAIT !!!") || statusS.getText().equals("ERROR !!!") ){
					Toast.makeText(SongEar.this, "Please wait !!!", Toast.LENGTH_SHORT).show();	
				} else {
					switch(btnPlayS.getText().toString()) {
					case "Play":
						peakRes.setText(String.valueOf(peaks.length));
						btnPlayS.setText("Stop");
						question.playSynth("seq");
						break;
					case "Stop" :
						btnPlayS.setText("Play");
						question.stopSynth();
						break;
					}				
				}
			}
		});
		Button btnMenuS = (Button) findViewById(R.id.btnMenuSel);
		Spinner quesListS = (Spinner) findViewById(R.id.spinnerS);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.quesListM, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quesListS.setAdapter(adapter);
		quesListS.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				NN = (int) arg3 + 2;
				question.setPosition(0);
				question.setNoteNumber(NN);
				btnPlayS.setText("Play");
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		btnMenuS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SongEar.this, FileChooser.class);
				startActivityForResult(intent, FILE_CHOOSER);
			}
		});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
	        String fileSelected = data.getStringExtra("fileName");
	        if(fileSelected.endsWith(".wav") || fileSelected.endsWith(".mp3")){
	        	songName.setText(fileSelected);
				new AnalyzeAudio().execute(data.getStringExtra("filePath"));
	        } else {
				Toast.makeText(this, "Please select mp3 of wav file", Toast.LENGTH_SHORT).show();
	        }
	    }                   
	}
	private void setResultImage(int... results){
		switch(results.length){
			case 1:
				changeImage(resImg1S,results[0]);
				break;
			case 2:
				changeImage(resImg1S,results[0]);
				changeImage(resImg2S,results[1]);
				break;	
			case 3:
				changeImage(resImg1S,results[0]);
				changeImage(resImg2S,results[1]);
				changeImage(resImg3S,results[2]);
				break;
			case 4:
				changeImage(resImg1S,results[0]);
				changeImage(resImg2S,results[1]);
				changeImage(resImg3S,results[2]);
				changeImage(resImg4S,results[3]);
				break;
			case 5:
				changeImage(resImg1S,results[0]);
				changeImage(resImg2S,results[1]);
				changeImage(resImg3S,results[2]);
				changeImage(resImg4S,results[3]);
				changeImage(resImg5S,results[4]);
				break;		
		}		
	}
	private void changeImage(ImageView img, int res){   // 0 is non available ** 1 is true ** 2 is false
		switch(res){
			case 0:
				img.setImageResource(android.R.drawable.presence_invisible);
				break;
			case 1:
				img.setImageResource(android.R.drawable.presence_online);
				break;
			case 2:
				img.setImageResource(android.R.drawable.presence_busy);
		}
	}
}
class AnalyzeAudio extends AsyncTask<String, Void, float[]> {
	
	@Override
	protected void onPreExecute() {
		SongEar.statusS.post(new Runnable(){
			@Override
			public void run() {
				SongEar.statusS.setTextColor(Color.YELLOW);
				SongEar.statusS.setText("WAIT !!!");
			}
		});
	}
	@Override
	protected float[] doInBackground(String... params) {
		float[] data = null;
		try {
			data = AudioRead.read(params[0]);
			SongEar.peaks = AudioRead.analyze(data);
			return SongEar.peaks;
		} catch (IOException e) {
			SongEar.statusS.post(new Runnable(){
				@Override
				public void run() {
					TetEar.status.setTextColor(Color.RED);
					TetEar.status.setText("ERROR !!!");
				}
			});
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(float[] result) {
		if(result == null){
			SongEar.statusS.post(new Runnable(){
				@Override
				public void run() {
					TetEar.status.setTextColor(Color.RED);
					TetEar.status.setText("ERROR !!!");
				}
			});
		} else {
			SongEar.question.setPosition(0);
			SongEar.question.setNoteNumber(SongEar.NN);
			SongEar.question.setInterval(SongEar.peaks);
			SongEar.question.setData("seq");
			SongEar.statusS.post(new Runnable(){
				@Override
				public void run() {
					SongEar.statusS.setTextColor(Color.GREEN);
					SongEar.statusS.setText("Ready :)");
				}
			});
		}
	}
	
}
