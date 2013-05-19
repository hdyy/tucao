package bupt.yangyang.tucao;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView image;
	public final static  String ip="192.168.22.11";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		image = (ImageView) findViewById(R.id.imageView1);
		
//		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//		   @Override
//		   public void run() {
//			   MainActivity.this.startActivity(new Intent(MainActivity.this,
//						ItemizedOverlayDemo.class)); 
//			   //实现淡入浅出的效果  
//               //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);  
//                
//               //由左向右滑入的效果  
//               //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);  
//                 
//               //实现zoommin 和 zoomout,即类似iphone的进入和退出时的效果  
//			   overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//			   finish();
//		   }
//		};
//		
		image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(new Intent(MainActivity.this,
						ItemizedOverlayDemo.class));				
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});
		
		//timer.schedule(task, 1000);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
