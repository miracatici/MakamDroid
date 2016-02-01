package org.example.trainear.main;

import org.example.trainear.R;
import org.example.trainear.backEnd.Answer;
import org.example.trainear.synth.SynthPlayer;
import org.example.trainear.utilities.AudioUtilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MicroEar extends Activity {
	
	private float[] quesCentData = new float[]{4800,5000,5200,5400,5600,5800,6000};
	private OnCheckedChangeListener listener1M,listener2M, listener3M;
	private Button btnPlayM,btnNextM, btnRecAM, btnPlayAM;
	private RadioButton quizA1M,quizA2M,quizA3M,quizA4M,quizA5M,quizA6M;
	private RadioGroup quizAnswer1M, quizAnswer2M,quizAnswer3M;
	private TextView theoAnsM;
	public static TextView status;
	private Spinner questionList;
	private Answer answer;
	private SynthPlayer question;
	private Switch switch1M;
	private int NN;
	private ImageView resImg1M, resImg2M, resImg3M, resImg4M,resImg5M;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_micro_ear);
		setProp();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.micro_ear, menu);
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
	private void setProp(){
		btnNextM = (Button) findViewById(R.id.btnNextM);
		btnPlayM = (Button) findViewById(R.id.btnPlayM);
		status = (TextView) findViewById(R.id.statusM);
		theoAnsM = (TextView) findViewById(R.id.theoAnsM);
		switch1M = (Switch) findViewById(R.id.switch1M);
		resImg1M = (ImageView) findViewById(R.id.resultImg1M);
		resImg2M = (ImageView) findViewById(R.id.resultImg2M);
		resImg3M = (ImageView) findViewById(R.id.resultImg3M);
		resImg4M = (ImageView) findViewById(R.id.resultImg4M);
		resImg5M = (ImageView) findViewById(R.id.resultImg5M);
		answer = new Answer();
		question = new SynthPlayer(status,1f);
		btnRecAM = (Button) findViewById(R.id.btnRecAM);
		btnPlayAM = (Button) findViewById(R.id.btnPlayAM);
		quizA1M = (RadioButton) findViewById(R.id.quizA1M);
		quizA1M.setClickable(false);	
		quizA2M = (RadioButton) findViewById(R.id.quizA2M);
		quizA2M.setClickable(false);	
		quizA3M = (RadioButton) findViewById(R.id.quizA3M);
		quizA3M.setClickable(false);	
		quizA4M = (RadioButton) findViewById(R.id.quizA4M);
		quizA4M.setClickable(false);	
		quizA5M = (RadioButton) findViewById(R.id.quizA5M);
		quizA5M.setClickable(false);	
		quizA6M = (RadioButton) findViewById(R.id.quizA6M);
		quizA6M.setClickable(false);	
		quizAnswer1M = (RadioGroup) findViewById(R.id.quizAnswer1M);
		quizAnswer2M = (RadioGroup) findViewById(R.id.quizAnswer2M);
		quizAnswer3M = (RadioGroup) findViewById(R.id.quizAnswer3M);
		questionList = (Spinner) findViewById(R.id.questionListM);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.quesListM, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		questionList.setAdapter(adapter);
		questionList.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				NN = (int) arg3 + 2;
				question.setPosition(0);
				question.setNoteNumber(NN);
				question.setInterval(quesCentData);
				question.setData("seq");
				btnPlayM.setText("Play");
				setResultImage(0,0,0,0);
				setDifferenceResult(0,0,0,0);
				clearChekcs();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		listener1M = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
            	if (checkedId != -1) {
	                quizAnswer2M.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer2M.clearCheck(); // clear the second RadioGroup!
	                quizAnswer2M.setOnCheckedChangeListener(listener2M); //reset the listener
	                quizAnswer3M.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer3M.clearCheck(); // clear the third RadioGroup!
	                quizAnswer3M.setOnCheckedChangeListener(listener3M); //reset the listener
	                switch (checkedId) {
						case R.id.quizA1M :
							theoryCompare((String)quizA1M.getText());
							break;
						case R.id.quizA2M :
							theoryCompare((String)quizA2M.getText());
							break;	
	                }		
	            }
	        }
	    };
	    listener2M = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
            	if (checkedId != -1) {
	                quizAnswer1M.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer1M.clearCheck(); // clear the first RadioGroup!
	                quizAnswer1M.setOnCheckedChangeListener(listener1M); //reset the listener
	                quizAnswer3M.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer3M.clearCheck(); // clear the third RadioGroup!
	                quizAnswer3M.setOnCheckedChangeListener(listener3M); //reset the listener
	                switch (checkedId) {
						case R.id.quizA3M :
							theoryCompare((String)quizA3M.getText());
							break;
						case R.id.quizA4M :
							theoryCompare((String)quizA4M.getText());
							break;	
	                }		
	            }   
	        }
	    };
	    listener3M = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
            	if (checkedId != -1) {
	                quizAnswer1M.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer1M.clearCheck(); // clear the first RadioGroup!
	                quizAnswer1M.setOnCheckedChangeListener(listener1M); //reset the listener
	                quizAnswer2M.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                quizAnswer2M.clearCheck(); // clear the second RadioGroup!
	                quizAnswer2M.setOnCheckedChangeListener(listener2M); //reset the listener
	                switch (checkedId) {
						case R.id.quizA5M :
							theoryCompare((String)quizA5M.getText());
							break;
						case R.id.quizA6M :
							theoryCompare((String)quizA6M.getText());
							break;	
	                }		
	            }   
	        }
	    };
	    btnPlayM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayM.getText().toString()) {
				case "Play":
					btnPlayM.setText("Stop");
					question.playSynth("seq");
					break;
				case "Stop" :
					btnPlayM.setText("Play");
					question.stopSynth();
					break;
				}				
			}
		});
	    btnNextM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				question.stopSynth();
				question.next();
				btnPlayM.setText("Play");
				setResultImage(0,0,0,0);
				setDifferenceResult(0,0,0,0);
				clearChekcs();
				theoAnsM.setTextColor(Color.WHITE);
				theoAnsM.setText("Answer");
			}
		});
	    btnRecAM.setOnTouchListener(new View.OnTouchListener() {
	        @SuppressLint("ClickableViewAccessibility")
			@Override
	        public boolean onTouch(View v, MotionEvent event) {
	            switch(event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                	answer.startRecord();
	                	btnRecAM.setPressed(true);
						btnPlayAM.setEnabled(false);
	                    return true;
	                case MotionEvent.ACTION_UP:
	                	answer.stopRecord();
	                	btnRecAM.setPressed(false);
						btnPlayAM.setEnabled(true);
						try {
							noteCompare(question,answer);							
						} catch (Exception e){
							e.printStackTrace();
							Toast.makeText(MicroEar.this, "Press long and record, then release", Toast.LENGTH_SHORT).show();
						}
	                    return true;
	            }
	            return false;
	        }
	    });
	    btnPlayAM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(btnPlayAM.getText().toString()) {
				case "Play":
					btnPlayAM.setText("Stop");
					answer.startPlay();
					break;
				case "Stop" :
					btnPlayAM.setText("Play");
					answer.stopPlay();
					break;
				}						
			}
		});
	    switch1M.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					clearChekcs();
					theoAnsM.setTextColor(Color.WHITE);
					theoAnsM.setText("Answer");
				}
				quizA1M.setClickable(isChecked);	
				quizA2M.setClickable(isChecked);	
				quizA3M.setClickable(isChecked);	
				quizA4M.setClickable(isChecked);
				quizA5M.setClickable(isChecked);	
				quizA6M.setClickable(isChecked);	
			}
		});
	    quizAnswer1M.setOnCheckedChangeListener(listener1M);
	    quizAnswer2M.setOnCheckedChangeListener(listener2M);
	    quizAnswer3M.setOnCheckedChangeListener(listener3M);
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
		setDifferenceResult(difference);
		setResultImage(answers);
	}
	private void theoryCompare(String a){
		String resQues = "Bakkiyye";
		if(a.equals(resQues)){
			theoAnsM.setTextColor(Color.GREEN);
			theoAnsM.setText("Correct");
		} else {
			theoAnsM.setTextColor(Color.RED);
			theoAnsM.setText("Wrong");
		}
	}
	private void setDifferenceResult(int... results){
//		switch(results.length){
//			case 1:
//				dif1.setText(String.valueOf((results[0])));
//				break;
//			case 2:
//				dif1.setText(String.valueOf((results[0])));
//				dif2.setText(String.valueOf((results[1])));
//				break;	
//			case 3:
//				dif1.setText(String.valueOf((results[0])));
//				dif2.setText(String.valueOf((results[1])));
//				dif3.setText(String.valueOf((results[2])));
//				break;
//			case 4:
//				dif1.setText(String.valueOf((results[0])));
//				dif2.setText(String.valueOf((results[1])));
//				dif3.setText(String.valueOf((results[2])));
//				dif4.setText(String.valueOf((results[3])));
//				break;	
//		}
	}
	private void setResultImage(int... results){
		switch(results.length){
			case 1:
				changeImage(resImg1M,results[0]);
				break;
			case 2:
				changeImage(resImg1M,results[0]);
				changeImage(resImg2M,results[1]);
				break;	
			case 3:
				changeImage(resImg1M,results[0]);
				changeImage(resImg2M,results[1]);
				changeImage(resImg3M,results[2]);
				break;
			case 4:
				changeImage(resImg1M,results[0]);
				changeImage(resImg2M,results[1]);
				changeImage(resImg3M,results[2]);
				changeImage(resImg4M,results[3]);
				break;
			case 5:
				changeImage(resImg1M,results[0]);
				changeImage(resImg2M,results[1]);
				changeImage(resImg3M,results[2]);
				changeImage(resImg4M,results[3]);
				changeImage(resImg5M,results[4]);
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
	private void clearChekcs(){
		quizAnswer1M.setOnCheckedChangeListener(null);
		quizAnswer2M.setOnCheckedChangeListener(null);
		quizAnswer3M.setOnCheckedChangeListener(null);
		quizAnswer1M.clearCheck(); quizAnswer2M.clearCheck(); quizAnswer3M.clearCheck(); 
		quizAnswer1M.setOnCheckedChangeListener(listener1M);
		quizAnswer2M.setOnCheckedChangeListener(listener2M);
		quizAnswer3M.setOnCheckedChangeListener(listener3M);
	}
}
