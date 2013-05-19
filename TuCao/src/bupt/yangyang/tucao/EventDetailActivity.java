package bupt.yangyang.tucao;

import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EventDetailActivity extends Activity {

	private int id;
	private EventData event;
	private Button buttonF;
	private TextView publishTime, eventMessage, numberGood, numberBad;
	private Button buttonDing, buttonCai;
	
	AddNumberThread addThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.event_detail);
		initRes();
		findViews();
		setValues();
		setListeners();
	}

	private void initRes() {
		// TODO Auto-generated method stub
		Bundle bd = getIntent().getExtras();
		id = bd.getInt("id");
		event = (EventData) bd.getSerializable("event");
	}

	private void findViews() {
		// TODO Auto-generated method stub
		buttonF = (Button) findViewById(R.id.button3);
		buttonF.setBackgroundDrawable(getResources().getDrawable(R.drawable.fanhui));
		publishTime = (TextView) findViewById(R.id.text_time);
		eventMessage = (TextView) findViewById(R.id.text_detail);
		numberGood = (TextView) findViewById(R.id.text_ding);
		numberBad = (TextView) findViewById(R.id.text_cai);
		buttonDing = (Button) findViewById(R.id.button1);
		buttonDing.setBackgroundDrawable(getResources().getDrawable(R.drawable.ding));
		buttonCai = (Button) findViewById(R.id.button2);
		buttonCai.setBackgroundDrawable(getResources().getDrawable(R.drawable.cai));
	}

	private void setValues() {
		// TODO Auto-generated method stub
		publishTime.setText(event.getPublishTime());
		eventMessage.setText(event.getEventMessage());
		numberGood.setText("¶¥£º"+event.getNumberGood());
		numberBad.setText("²È£º"+event.getNumberBad());
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		buttonDing.setOnClickListener(mListener);
		buttonCai.setOnClickListener(mListener);
	}
	private MyButtonListener mListener=new MyButtonListener();
	private class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Bundle bd=new Bundle();
			bd.putLong("id",event.getId());
			switch (v.getId()) {
			case R.id.button1:
				bd.putBoolean("isGood",true);
				addThread=new AddNumberThread(EventDetailActivity.this, bd);
				addThread.start();
				break;
			case R.id.button2:
				bd.putBoolean("isGood",false);
				addThread=new AddNumberThread(EventDetailActivity.this, bd);
				addThread.start();
				break;
			}
		}

	}
}
