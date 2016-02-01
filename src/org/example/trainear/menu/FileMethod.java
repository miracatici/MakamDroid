package org.example.trainear.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class FileMethod extends Activity {
	private final int FILE_CHOOSER=1;

	public void browseFiles(View view) {
		Intent intent = new Intent(this, FileChooser.class);
		startActivityForResult(intent, FILE_CHOOSER);
	}
}
