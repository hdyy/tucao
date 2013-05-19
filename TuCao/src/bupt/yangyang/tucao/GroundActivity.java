package bupt.yangyang.tucao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class GroundActivity extends Activity {
	private ImageView image;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ground);
		image = (ImageView) findViewById(R.id.groundView);
	}
}
