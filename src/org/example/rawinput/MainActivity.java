package org.example.rawinput;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import dataAndroid.Answer;
import dataAndroid.Question;
import fileChooser.FileChooser;

public class MainActivity extends Activity {
	private TextView res1, res2, res3, ans1, ans2, ans3;
	private Button btnSelQ, btnPlayQ,btnNext, btnRecA, btnPlayA, btnAnalyze;
	private ImageButton btnFolder;
	private int position = 1, streamID=0; 
	final int FILE_CHOOSER=1;
	private Question q1set;
	private Answer answer1;
	public static final int NN = 1;
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
		res1 = (TextView) findViewById(R.id.res1);
		res2 = (TextView) findViewById(R.id.res2);
		res3 = (TextView) findViewById(R.id.res3);
		ans1 = (TextView) findViewById(R.id.ans1);
		ans2 = (TextView) findViewById(R.id.ans2);
		ans3 = (TextView) findViewById(R.id.ans3);
		
		btnSelQ = (Button) findViewById(R.id.btnSelQ);
		btnPlayQ = (Button) findViewById(R.id.btnPlayQ);
		btnRecA = (Button) findViewById(R.id.btnRecA);
		btnAnalyze = (Button) findViewById(R.id.btnAnalyze);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnFolder = (ImageButton) findViewById(R.id.btnFolder);
		btnPlayA = (Button) findViewById(R.id.btnPlayA); 
		
		btnPlayA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayA.getText().toString()) {
				case "Play A":
					btnPlayA.setText("Stop A");
					answer1.startPlay();
					break;
				case "Stop A" :
					btnPlayA.setText("Play A");
					answer1.stopPlay();
					break;
				}						
			}
		});
		btnFolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				browseFiles(v);
			}
		});
		btnSelQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSelQ.setEnabled(false);
				q1set = new Question(MainActivity.this,"n",NN);
			}
		});
		btnPlayQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayQ.getText().toString()) {
					case "Play Q":
						btnPlayQ.setText("Stop Q");
						setQuestionResult(q1set.getQuestionResult(position));
						q1set.play(position);
						streamID++;
						break;
					case "Stop Q" :
						btnPlayQ.setText("Play Q");
						q1set.stop(streamID);
						break;
				}
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				q1set.stop(streamID);
				position++;
				btnPlayQ.setText("Play Q");
				if(position == q1set.getSoundNumber()){
					btnNext.setEnabled(false);
					Toast.makeText(MainActivity.this, "Maximum files is played", Toast.LENGTH_SHORT).show();
				} 
			}
		});
		
		btnRecA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnRecA.getText().toString()) {
					case "Rec A":
						btnRecA.setText("Stop A");;
						answer1 = new Answer();
						answer1.startRecord();
						break;
					case "Stop A" :
						btnRecA.setText("Rec A");
						answer1.stopRecord();
						break;						
				}
			}
		});
		btnAnalyze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float[] res = answer1.analyze(NN);
				setAnswerResult(res);
			}
		});
	}
	public void browseFiles(View view) {
		Intent intent = new Intent(this, FileChooser.class);
		startActivityForResult(intent, FILE_CHOOSER);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
	        String fileSelected = data.getStringExtra("fileSelected");
	        // dosyayla naapmak istersen
	        System.out.println(fileSelected);
	        Toast.makeText(this, "file selected "+fileSelected, Toast.LENGTH_SHORT).show();
	        FileChooser.currentDir = new File(fileSelected);
	    }                   
	}
	private void setQuestionResult(float... results){
		switch(results.length){
			case 1:
				res1.setText(String.valueOf(results[0]));
				break;
			case 2:
				res1.setText(String.valueOf(results[0]));
				res2.setText(String.valueOf(results[1]));
				break;	
			case 3:
				res1.setText(String.valueOf(results[0]));
				res2.setText(String.valueOf(results[1]));
				res3.setText(String.valueOf(results[2]));
				break;
		}
	}
	private void setAnswerResult(float... results){
		switch(results.length){
			case 1:
				ans1.setText(String.valueOf(results[0]));
				break;
			case 2:
				ans1.setText(String.valueOf(results[0]));
				ans2.setText(String.valueOf(results[1]));
				break;	
			case 3:
				ans1.setText(String.valueOf(results[0]));
				ans2.setText(String.valueOf(results[1]));
				ans3.setText(String.valueOf(results[2]));
				break;
		}
	}
}
