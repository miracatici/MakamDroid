package org.example.trainear;


import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import dataAndroid.Answer;
import dataAndroid.Question;
import utilities.AudioUtilities;

public class MainActivity extends Activity {
	private Question questionSet;
	private Answer answer;
	private TextView dif1,dif2,dif3,dif4;
	private RadioButton quizA1,quizA2,quizA3,quizA4;
	private RadioGroup quizAnswer, quizAnswer2;
	public static TextView status;
	private Button btnSelQ, btnPlayQ,btnNext, btnRecA, btnPlayA,btnPrev;
	private ImageView resImg1, resImg2, resImg3, resImg4;
	private Spinner questionList;
	private String QT = "n";
	private int streamID=0; 
	private int NN = 1;
	private OnCheckedChangeListener listener1, listener2;
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
		quizA1 = (RadioButton) findViewById(R.id.quizA1);
		quizA2 = (RadioButton) findViewById(R.id.quizA2);
		quizA3 = (RadioButton) findViewById(R.id.quizA3);
		quizA4 = (RadioButton) findViewById(R.id.quizA4);
		quizAnswer = (RadioGroup) findViewById(R.id.quizAnswer);
		quizAnswer2 = (RadioGroup) findViewById(R.id.quizAnswer2);
		dif1 = (TextView) findViewById(R.id.dif1);
		dif2 = (TextView) findViewById(R.id.dif2);
		dif3 = (TextView) findViewById(R.id.dif3);
		dif4 = (TextView) findViewById(R.id.dif4);
		status = (TextView) findViewById(R.id.status);
		btnSelQ = (Button) findViewById(R.id.btnSelQ);
		btnPlayQ = (Button) findViewById(R.id.btnPlayQ);
		btnPlayQ.setEnabled(false);
		btnRecA = (Button) findViewById(R.id.btnRecA);
		btnNext = (Button) findViewById(R.id.btnNext); 
		btnNext.setEnabled(false);
		btnPlayA = (Button) findViewById(R.id.btnPlayA);
		btnPlayA.setEnabled(false);
		btnPrev = (Button) findViewById(R.id.btnPrev);
		btnPrev.setEnabled(false);
		questionList = (Spinner) findViewById(R.id.spinner1);
		
		resImg1 = (ImageView) findViewById(R.id.resultImg1);
		resImg2 = (ImageView) findViewById(R.id.resultImg2);
		resImg3 = (ImageView) findViewById(R.id.resultImg3);
		resImg4 = (ImageView) findViewById(R.id.resultImg4);
		listener1 = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            if (checkedId != -1) {
	                quizAnswer2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer2.clearCheck(); // clear the second RadioGroup!
	                quizAnswer2.setOnCheckedChangeListener(listener2); //reset the listener
	                switch (checkedId) {
						case R.id.quizA1 :
							Toast.makeText(MainActivity.this, quizA1.getText(), Toast.LENGTH_SHORT).show();
							break;
						case R.id.quizA2 :
							Toast.makeText(MainActivity.this, quizA2.getText(), Toast.LENGTH_SHORT).show();
							break;	
	                }		
	            }
	        }
	    };
	    listener2 = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            if (checkedId != -1) {
	                quizAnswer.setOnCheckedChangeListener(null);
	                quizAnswer.clearCheck();
	                quizAnswer.setOnCheckedChangeListener(listener1);
	                switch (checkedId) {			
						case R.id.quizA3 :
							Toast.makeText(MainActivity.this, quizA3.getText(), Toast.LENGTH_SHORT).show();
							break;				
						case R.id.quizA4 :
							Toast.makeText(MainActivity.this, quizA4.getText(), Toast.LENGTH_SHORT).show();
							break;				
	                } 
	            }
	        }
	    };
		quizAnswer.setOnCheckedChangeListener(listener1);
		quizAnswer2.setOnCheckedChangeListener(listener2);
		
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
						quizAnswer.setClickable(false);
						quizAnswer2.setClickable(false);						
						break;
					case "2 Note":
						NN = 2;
						QT = "n";
						break;
					case "3 Note":
						NN = 3;
						QT = "n";
						break;
					case "4 Note" :
						NN = 4;
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
		answer = new Answer();
		btnSelQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{ 
					new Thread(new Runnable(){
						@Override
						public void run() {
							questionSet = new Question(MainActivity.this,QT,NN);							
							answer = new Answer();						
						}	
					}).start();
					setQuestionResult(0,0,0,0);
					setAnswerResult(0,0,0,0);
					setResultImage(0,0,0,0);
					setDifferenceResult(0,0,0,0);
					setOption();
					clearChekcs();
					btnSelQ.setEnabled(false);
					btnPlayQ.setEnabled(true);
					btnPlayA.setEnabled(false);
					btnNext.setEnabled(true);
					btnPrev.setEnabled(true);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "Error occured Main, no files loaded", Toast.LENGTH_SHORT).show();
				}						
			}	
		});
		btnPlayQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayQ.getText().toString()) {
					case "Play Q":
						btnPlayQ.setText("Stop Q");
						streamID = questionSet.play();
						break;
					case "Stop Q" :
						btnPlayQ.setText("Play Q");
						questionSet.stop(streamID);
						break;
				}
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				questionSet.stop(streamID);
				questionSet.next();
				btnPlayQ.setText("Play Q");
				setQuestionResult(0,0,0,0);
				setAnswerResult(0,0,0,0);
				setResultImage(0,0,0,0);
				setDifferenceResult(0,0,0,0);
				setOption();
				clearChekcs();
			}
		});
		btnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				questionSet.stop(streamID);
				questionSet.previous();
				btnPlayQ.setText("Play Q");
				setQuestionResult(0,0,0,0);
				setAnswerResult(0,0,0,0);
				setResultImage(0,0,0,0);
				setDifferenceResult(0,0,0,0);
				setOption();
				clearChekcs();
			}
		});
		btnRecA.setOnTouchListener(new View.OnTouchListener() {
	        @SuppressLint("ClickableViewAccessibility")
			@Override
	        public boolean onTouch(View v, MotionEvent event) {
	            switch(event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                	answer.startRecord();
	                	btnRecA.setPressed(true);
						btnPlayA.setEnabled(false);
	                    return true;
	                case MotionEvent.ACTION_UP:
	                	btnRecA.setPressed(false);
	                	answer.stopRecord();
						btnPlayA.setEnabled(true);
						try {
							compare(questionSet,answer);							
						} catch (Exception e){
							e.printStackTrace();
							Toast.makeText(MainActivity.this, "Press long and record, then release", Toast.LENGTH_LONG).show();
						}
	                    return true;
	            }
	            return false;
	        }
	    });
		btnPlayA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayA.getText().toString()) {
				case "Play A":
					btnPlayA.setText("Stop A");
					answer.startPlay();
					break;
				case "Stop A" :
					btnPlayA.setText("Play A");
					answer.stopPlay();
					break;
				}						
			}
		});
	}
	private void compare(Question q, Answer a){
		switch(NN){
			case 0 : rhythmCompare(q,a);
				break;
			default : noteCompare(q,a);
				break;
		}
	}
	public void noteCompare(Question q, Answer a) {
		float[] resQues = q.getQuestionResult();
		float[] resAns = a.analyze(NN);
		int[] answers = new int[NN];
		int[] difference = new int[NN];
		for (int i = 0; i < answers.length; i++) {
			int qCent = AudioUtilities.hertzToCent(resQues[i]);
			int aCent = AudioUtilities.hertzToCent(resAns[i]);
			int[] temp = compareCent(qCent, aCent);
			answers[i] = temp[0];
			difference[i] = temp[1];
		}
		setDifferenceResult(difference);
		setResultImage(answers);
		setQuestionResult(resQues);
		setAnswerResult(resAns);
	}
	public void rhythmCompare(Question q, Answer a){
		// Rhythm compare methods comes here
	}
	private void setQuestionResult(float... results){

	}
	private void setAnswerResult(float... results){

	}
	private void setDifferenceResult(int... results){
		switch(results.length){
			case 1:
				dif1.setText(String.valueOf((results[0])));
				break;
			case 2:
				dif1.setText(String.valueOf((results[0])));
				dif2.setText(String.valueOf((results[1])));
				break;	
			case 3:
				dif1.setText(String.valueOf((results[0])));
				dif2.setText(String.valueOf((results[1])));
				dif3.setText(String.valueOf((results[2])));
				break;
			case 4:
				dif1.setText(String.valueOf((results[0])));
				dif2.setText(String.valueOf((results[1])));
				dif3.setText(String.valueOf((results[2])));
				dif4.setText(String.valueOf((results[3])));
				break;	
		}
	}
	private void setResultImage(int... results){
		switch(results.length){
			case 1:
				changeImage(resImg1,results[0]);
				break;
			case 2:
				changeImage(resImg1,results[0]);
				changeImage(resImg2,results[1]);
				break;	
			case 3:
				changeImage(resImg1,results[0]);
				changeImage(resImg2,results[1]);
				changeImage(resImg3,results[2]);
				break;
			case 4:
				changeImage(resImg1,results[0]);
				changeImage(resImg2,results[1]);
				changeImage(resImg3,results[2]);
				changeImage(resImg4,results[3]);
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
	private void setOption(){
		switch(NN){
			case 0:
				
				break;
			case 1:
				quizA1.setClickable(false);
				quizA2.setClickable(false);
				quizA3.setClickable(false);
				quizA4.setClickable(false);
				break;
			case 2:
				break;
			case 3:
				;
			case 4:
				;
		}
	}
	private void clearChekcs(){
		quizAnswer.setOnCheckedChangeListener(null);
		quizAnswer2.setOnCheckedChangeListener(null);
		quizAnswer.clearCheck(); quizAnswer2.clearCheck(); 
		quizAnswer.setOnCheckedChangeListener(listener1);
		quizAnswer2.setOnCheckedChangeListener(listener2);
	}
	private int[] compareCent(float testCent, float recCent){
		if(Math.abs(testCent - recCent)<50.5f){
			return new int[]{1,Math.round((Math.abs(testCent - recCent)))};
		} else if (Math.abs(testCent - (recCent-1200))<50.5f){
			return new int[]{1,Math.round((Math.abs(testCent - (recCent-1200))))};
		} else if (Math.abs(testCent - (recCent+1200))<50.5f){
			return new int[]{1,Math.round((Math.abs(testCent - (recCent+1200))))};
		} else {
			return new int[]{2,Math.round((Math.abs(testCent - recCent)))};
		}
	}
}
