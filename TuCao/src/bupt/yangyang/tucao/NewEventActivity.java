package bupt.yangyang.tucao;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class NewEventActivity extends Activity {
	private ImageButton btnCommit;
	private ImageButton fanhui;
	private Button btnAddImage;
	private ImageView imgView;
	
	private EditText editMessage;
	private UploadEventThread upLoadThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_event);
		initRes();
		findViews();
		setValues();
		setListeners();
		System.out.println("???");
	}

	private void initRes() {
		// TODO Auto-generated method stub

	}

	private void findViews() {
		// TODO Auto-generated method stub
		fanhui = (ImageButton)findViewById(R.id.button2);
		//fanhui.setBackgroundDrawable(getResources().getDrawable(R.drawable.fanhui));
		btnCommit =(ImageButton)findViewById(R.id.button3);
		//btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.queren));
		btnAddImage= (Button) findViewById(R.id.button1);
		editMessage = (EditText) findViewById(R.id.edit_text_detail);
		imgView=(ImageView)findViewById(R.id.imageView1);
	}

	private void setValues() {
		// TODO Auto-generated method stub

	}

	private void setListeners() {
		
		btnAddImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("btnAddImage clicked");
				Bitmap bmp;
				try {
					bmp = BitmapFactory
							.decodeStream(NewEventActivity.this
									.getAssets().open("sample1.jpg"));
					imgView.setImageBitmap(bmp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}});
		// TODO Auto-generated method stub
		btnCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//System.out.println(21231321232L);
				String message = editMessage.getText().toString();
				if (message.length() != 0) {
					// 直接开新线程发，不走service
					Bundle data = new Bundle();
					data.putString("EventMessage", message);
					upLoadThread = new UploadEventThread(NewEventActivity.this,
							data);
					upLoadThread.start();
					Toast.makeText(NewEventActivity.this, "恭喜你，吐槽成功", 0);
					finish();
				}else{
					Toast.makeText(NewEventActivity.this, "空的吐槽不是真正的吐槽！", 0);
				}
			}
		});
		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
