package org.example.trainear.main;

import java.io.File;

import org.example.trainear.R;
import org.example.trainear.menu.FileChooser;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SongEar extends Activity {
	private TextView songName;
	final int FILE_CHOOSER=1;
	private int NN = 2;
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
		songName = (TextView) findViewById(R.id.songName);
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
	        songName.setText(fileSelected);
	        Toast.makeText(this, "File selected "+fileSelected, Toast.LENGTH_SHORT).show();
	        FileChooser.currentDir = new File(data.getStringExtra("fileSelected"));
	    }                   
	}
}
