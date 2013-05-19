package bupt.yangyang.tucao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class IndexActivity extends Activity {
	private ImageButton fanhui;
	private ImageButton add;
	private ToggleButton hn;
	
	private final String TAG = "TypeActivity";
	// 存放最热槽点
	public List<EventData> hotEvents;
	public List<EventData> nearEvents;
	public List<EventData> showList;
	private ListView mListView;
	// private IndexType IndexTypes;
	public List<EventData> events;
	private int[] listViewRes = null;
	private ListViewAdapter mListAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.index);
		
		fanhui = (ImageButton)findViewById(R.id.button2);
		//fanhui.setBackgroundDrawable(getResources().getDrawable(R.drawable.fanhui));
		add =(ImageButton)findViewById(R.id.button3);
		//add.setBackgroundDrawable(getResources().getDrawable(R.drawable.add));
		hn = (ToggleButton)findViewById(R.id.button1);
		hn.setBackgroundDrawable(getResources().getDrawable(R.drawable.hoter));
		setOncheckedChangeListener();
		initRes();
		findViews();
		setValues();
		setListeners();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initRes() {

		//
		// IndexTypes = new IndexType();
		// listViewRes = new int[IndexTypes.types.length];
		// for (int i = 0; i < IndexTypes.types.length; i++) {
		// listViewRes[i] = R.drawable.icon_360;
		// }
		getListFromBundle(this.getIntent().getExtras());
	}

	private void findViews() {
		mListView = (ListView) this.findViewById(R.id.listview);
	}

	private void setValues() {
		mListAdapter = new ListViewAdapter(this);
		mListView.setAdapter(mListAdapter);

	}

	private void getListFromBundle(Bundle listBundle) {
		// TODO Auto-generated method stub
		IntentData iData = new IntentData();
		iData = (IntentData) listBundle.getSerializable(IntentData.name);
		hotEvents = iData.getHotEvents();
		nearEvents = iData.getNearEvents();
		switch (listBundle.getInt("type")) {
		case 0:
			showList = hotEvents;
			break;
		case 1:
			showList = nearEvents;
			break;
		case 2:
			showList = new ArrayList<EventData>();
			break;
		}

	}
	
	private void setOncheckedChangeListener(){
		OnCheckedChangeListener listener1 = new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					hn.setBackgroundDrawable(getResources().getDrawable(R.drawable.hoter));
					//listType=0;//设置如果开启详情页时应显示类型0，最热
					//showHotEvents();
				}else{
					hn.setBackgroundDrawable(getResources().getDrawable(R.drawable.newer));
					//listType=1;//设置如果开启详情页时应显示类型1，最近
					//showNearEvents();
				}
			}
			
		};
		
		hn.setOnCheckedChangeListener(listener1);
	}
	
	private void setListeners() {

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View clickView,
					int position, long _id) {

				int id = (int) _id;
				EventData event = showList.get(id);
				Bundle bd = new Bundle();
				bd.putInt("id", id);
				bd.putSerializable("event", event);
				
				Intent intent = new Intent(IndexActivity.this,
						EventDetailActivity.class);
				intent.putExtras(bd);
				startActivity(intent);
			}

		});
		
		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						IndexActivity.this,
						NewEventActivity.class);
				startActivity(intent);
			}
		});
	}

	private final class ListViewHolder {
		// public ImageView imageViewIcon;
		public TextView publishTime;
		public TextView eventMessage;
		public TextView numberGood;
		public TextView numberBad;

	}

	private class ListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Context mContext;

		public ListViewAdapter(Context ctx) {
			this.mContext = ctx;
			this.mInflater = LayoutInflater.from(ctx);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return showList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			int id = (int) getItemId(position);
			ListViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.index_item_listview,
						null);
				viewHolder = new ListViewHolder();
				viewHolder.publishTime = (TextView) convertView
						.findViewById(R.id.text_publish_time);
				viewHolder.eventMessage = (TextView) convertView
						.findViewById(R.id.text_event_message);
				viewHolder.numberGood = (TextView) convertView
						.findViewById(R.id.text_good);
				viewHolder.numberBad = (TextView) convertView
						.findViewById(R.id.text_bad);
			} else {
				viewHolder = (ListViewHolder) convertView.getTag();
			}
			EventData event = showList.get(id);
			viewHolder.publishTime.setText(event.getPublishTime());
			viewHolder.eventMessage.setText(event.getEventMessage());
			viewHolder.numberGood.setText("顶：" + event.getNumberGood());
			viewHolder.numberBad.setText("踩：" + event.getNumberBad());

			// Log.v(TAG, "ID=" + id + "types=" + IndexTypes.types[id]);
			convertView.setTag(viewHolder);
			return convertView;
		}

	}
}