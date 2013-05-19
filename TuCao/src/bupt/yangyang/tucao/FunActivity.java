package bupt.yangyang.tucao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FunActivity extends Activity {
	private ImageButton image =null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fun);
		image = (ImageButton) findViewById(R.id.ImageButton12);
		setListeners();
	}



	private void setListeners() {
		// TODO Auto-generated method stub
		image.setOnClickListener(mListener);
	
	}
	
	private MyButtonListener mListener=new MyButtonListener();
	
	private class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FunActivity.this.startActivity(new Intent(FunActivity.this,
					GroundActivity.class));				
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);	
		}

	}
}