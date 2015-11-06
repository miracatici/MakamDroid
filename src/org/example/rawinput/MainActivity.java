package org.example.rawinput;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import dataAndroid.Answer;
import dataAndroid.Question;
import fileChooser.FileChooser;

public class MainActivity extends Activity {
	private Question q1set;
	private Answer answer1;
	private TextView res1, res2, res3, ans1, ans2, ans3;
	private Button btnSelQ, btnPlayQ,btnNext, btnRecA, btnPlayA, btnAnalyze,btnPrev;
	private ImageButton btnFolder;
	private Spinner questionList;
	private String QT = "n";
	private int streamID=0; 
	private int NN = 1;
	private final int FILE_CHOOSER=1;
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
		btnPlayQ.setEnabled(false);
		btnRecA = (Button) findViewById(R.id.btnRecA);
		btnAnalyze = (Button) findViewById(R.id.btnAnalyze);
		btnAnalyze.setEnabled(false);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setEnabled(false);
		btnFolder = (ImageButton) findViewById(R.id.btnFolder);
		btnPlayA = (Button) findViewById(R.id.btnPlayA);
		btnPlayA.setEnabled(false);
		btnPrev = (Button) findViewById(R.id.btnPrev);
		btnPrev.setEnabled(false);
		questionList = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.quesList, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		questionList.setAdapter(adapter);
		questionList.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch((String) questionList.getSelectedItem()){
					case "1 Note":
						NN = 1;
						QT = "n";
						break;
					case "2 Note":
						NN = 2;
						QT = "n";
						break;
					case "3 Note":
						NN = 3;
						QT = "n";
						break;	
					case "Rhythm":
						NN = 0;
						QT = "r";
						break;	
				}
				btnSelQ.setEnabled(true);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		answer1 = new Answer();
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
				btnPlayQ.setEnabled(true);
				btnNext.setEnabled(true);
				btnPrev.setEnabled(true);
				q1set = new Question(MainActivity.this,QT,NN);
				answer1 = new Answer();
			}
		});
		btnPlayQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayQ.getText().toString()) {
					case "Play Q":
						btnPlayQ.setText("Stop Q");
						streamID = q1set.play();
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
				q1set.next();
				btnPlayQ.setText("Play Q");
				
			}
		});
		btnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				q1set.stop(streamID);
				q1set.previous();
				btnPlayQ.setText("Play Q");
			}
		});
		btnRecA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnRecA.getText().toString()) {
					case "Rec A":
						btnRecA.setText("Stop A");;
						answer1.startRecord();
						break;
					case "Stop A" :
						btnRecA.setText("Rec A");
						answer1.stopRecord();
						btnAnalyze.setEnabled(true);
						btnPlayA.setEnabled(true);
						break;						
				}
			}
		});
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
		btnAnalyze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				compare(q1set,answer1);
			}
		});
	}
	
	private void compare(Question q, Answer a){
		switch(NN){
			case 1:
				noteCompare(q,a);
				break;
			default :
				rhythmCompare();
				break;
		}
	}
	
	public void noteCompare(Question q, Answer a) {
		float[] resQues = q.getQuestionResult();
		float[] resAns = a.analyze(NN); 
		setQuestionResult(resQues);
		setAnswerResult(resAns);
	}
	public void rhythmCompare(){
		// Rhythm compare methods comes here
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
}
