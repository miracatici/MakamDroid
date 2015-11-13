package fileChooser;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class FileMethod extends Activity {
	private final int FILE_CHOOSER=1;

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
