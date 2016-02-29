package org.example.trainear.main;

import java.io.IOException;

import org.example.trainear.R;
import org.example.trainear.backEnd.Answer;
import org.example.trainear.backEnd.AudioRead;
import org.example.trainear.menu.FileChooser;
import org.example.trainear.synth.SynthPlayer;
import org.example.trainear.utilities.AudioUtilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
	private Answer answer;
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
		answer = new Answer();
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
		final Button btnPlaySA = (Button) findViewById(R.id.btnPlaySA);
		final Button btnRecS = (Button) findViewById(R.id.btnRecS);
		Button btnNextS = (Button) findViewById(R.id.btnNextS);
		Button btnPrevS = (Button) findViewById(R.id.btnPrevS);
		Button btnMenuS = (Button) findViewById(R.id.btnMenuSel);
		btnNextS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				question.stopSynth();
				question.next();
				btnPlayS.setText("Play");
				setResultImage(0,0,0,0);			
			}
		});
		btnRecS.setOnTouchListener(new View.OnTouchListener() {
	        @SuppressLint("ClickableViewAccessibility")
			@Override
	        public boolean onTouch(View v, MotionEvent event) {
	            switch(event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                	answer.startRecord();
	                	btnRecS.setPressed(true);
						btnPlaySA.setEnabled(false);
	                    return true;
	                case MotionEvent.ACTION_UP:
	                	answer.stopRecord();
	                	btnRecS.setPressed(false);
						btnPlaySA.setEnabled(true);
						try {
							noteCompare(question,answer);							
						} catch (Exception e){
							e.printStackTrace();
							Toast.makeText(SongEar.this, "Press long and record, then release", Toast.LENGTH_SHORT).show();
						}
	                    return true;
	            }
	            return false;
	        }
	    });
	    btnPlaySA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlaySA.getText().toString()) {
				case "Play":
					btnPlaySA.setText("Stop");
					answer.startPlay();
					break;
				case "Stop" :
					btnPlaySA.setText("Play");
					answer.stopPlay();
					break;
				}						
			}
		});
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
		btnMenuS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SongEar.this, FileChooser.class);
				startActivityForResult(intent, FILE_CHOOSER);
			}
		});
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
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
	        String fileSelected = data.getStringExtra("fileName");
	        if(fileSelected.endsWith(".wav") || fileSelected.endsWith(".mp3")){
	        	songName.setText(fileSelected);
				new AnalyzeAudio().execute(data.getStringExtra("filePath"));
	        } else {
				Toast.makeText(this, "Please select mp3 or wav file", Toast.LENGTH_SHORT).show();
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
	private void noteCompare(SynthPlayer q, Answer a) {
		float[] resQues = q.getQuestionResult();
		float[] resAns = a.analyze(NN);
		int[] answers = new int[NN];
		int[] difference = new int[NN];
		for (int i = 0; i < answers.length; i++) {
			int qCent = AudioUtilities.hertzToCent(resQues[i]);
			int aCent = AudioUtilities.hertzToCent(resAns[i]);
			int[] temp = centCompare(qCent, aCent);
			answers[i] = temp[0];
			difference[i] = temp[1];
		}
//		setDifferenceResult(difference);
		setResultImage(answers);
	}
	private int[] centCompare(float testCent, float recCent){
		if(Math.abs(testCent - recCent)<50.5f){
			System.out.println(testCent - recCent);
			return new int[]{1,Math.round((Math.abs(testCent - recCent)))};
		} else if (Math.abs(testCent - (recCent-1200))<50.5f){
			System.out.println(testCent - (recCent-1200));
			return new int[]{1,Math.round((Math.abs(testCent - (recCent-1200))))};
		} else if (Math.abs(testCent - (recCent+1200))<50.5f){
			System.out.println(testCent - (recCent+1200));
			return new int[]{1,Math.round((Math.abs(testCent - (recCent+1200))))};
		} else if (Math.abs(testCent - (recCent-2400))<50.5f){
			System.out.println(testCent - (recCent-2400));
			return new int[]{1,Math.round((Math.abs(testCent - (recCent-1200))))};
		} else if (Math.abs(testCent - (recCent+2400))<50.5f){
			System.out.println(testCent - (recCent+2400));
			return new int[]{1,Math.round((Math.abs(testCent - (recCent+1200))))};
		} else {
			System.out.println(testCent - recCent);
			return new int[]{2,Math.round((Math.abs(testCent - recCent)))};
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
