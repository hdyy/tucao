package bupt.yangyang.tucao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 在一个圆周上添加自定义overlay.
 */
public class ItemizedOverlayDemo extends Activity {
	private ToggleButton tog1;
	private Button loc = null;
	private Button fun = null;

	private GeoPoint popGeoPt;
	final static String TAG = "MainActivty";
	static MapView mMapView = null;
	private MapController mMapController = null;
	public MKMapViewListener mMapListener = null;
	Button hotEventButton = null;
	Button nearEventButton = null;

	EditText indexText = null;
	OverlayTest ov = null;
	private int listType;
	private int type;
	private ItemizedOverlayDemo activity;
	/**
	 * 圆心经纬度坐标
	 */
	int cLat = 39960086;
	int cLon = 116352446;
	// 存放overlayitem
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 存放overlay图片
	public List<Drawable> res = new ArrayList<Drawable>();

	// 存放最热槽点
	public List<EventData> hotEvents;
	public List<EventData> nearEvents;
	public List<EventData> newEvents;
	public List<EventData> rangeEvents;

	private PopupOverlay pop = null;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;
	LocationData locData = null;
	MyLocationOverlay myLocationOverlay = null;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(ItemizedOverlayDemo.this, "msg:" + msg.what,
					Toast.LENGTH_SHORT).show();
		};
	};

	Handler listHD = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.out.println("listHD : msg.what=" + msg.what);
			switch (msg.what) {
			// 得到数据库里最热的十条
			case 0:
				hotEvents = getListFromBundle(msg.getData());
				break;
			// 得到某个地点最热的10条
			case 1:
				nearEvents = getListFromBundle(msg.getData());
				break;
			// 得到某个地点最新的10条
			case 2:
				newEvents = getListFromBundle(msg.getData());
				break;
			case 3:
				rangeEvents = getListFromBundle(msg.getData());
				break;
			}

		}
	};

	private List<EventData> getListFromBundle(Bundle listBundle) {
		// TODO Auto-generated method stub
//		System.out.println("in itemized : "
//				+ ((IntentData) listBundle.getSerializable(IntentData.name))
//						.getHotEvents());
		IntentData iData = new IntentData();
		iData = (IntentData) listBundle.getSerializable(IntentData.name);
		System.out.println("getListFromBundle = " + iData.getHotEvents());
		return iData.getHotEvents();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity = this;
		initMap();

		Drawable marker = ItemizedOverlayDemo.this.getResources().getDrawable(
				R.drawable.icon_marka);
		mMapView.getOverlays().clear();
		ov = new OverlayTest(marker, this, mMapView);
		mMapView.getOverlays().add(ov);

		setOncheckedChangeListener();
		setListener();

		setValues();
		pop = new PopupOverlay(ItemizedOverlayDemo.mMapView,
				new PopupClickListener() {

					@Override
					public void onClickedPopup(int index) {
						switch (index) {
						case 0:
							Toast.makeText(ItemizedOverlayDemo.this, "吐槽此地",
									Toast.LENGTH_SHORT).show();
							Bundle bd = new Bundle();
							bd.putInt("Lat", activity.popGeoPt.getLatitudeE6());
							bd.putInt("Lon", activity.popGeoPt.getLongitudeE6());
							Intent intent = new Intent(
									ItemizedOverlayDemo.this,
									NewEventActivity.class);
							intent.putExtras(bd);
							startActivity(intent);
							break;
						case 1:
							
							Intent indexIntent = new Intent(
									ItemizedOverlayDemo.this,
									IndexActivity.class);
							Bundle indexBundle = getListBundle();
							indexBundle.putInt("type", listType);// 用于确认传入的是hot还是near
							indexIntent.putExtras(indexBundle);

							ItemizedOverlayDemo.this.startActivity(indexIntent);
							break;
						case 2:
							Toast.makeText(ItemizedOverlayDemo.this,
									"item 2 clicked", Toast.LENGTH_SHORT)
									.show();
							break;
						}
					}
				});
		// 自2.1.1 开始，使用 add/remove 管理overlay , 无需调用以下接口.
		// populate();
	}

	private Bundle getListBundle() {
		// TODO Auto-generated method stub
		Bundle listBundle = new Bundle();
		IntentData iData = new IntentData();
		iData.setHotEvents(hotEvents);
		iData.setNearEvents(nearEvents);
		iData.setNewEvents(newEvents);
		iData.setRangeEvents(rangeEvents);
		listBundle.putSerializable(iData.name, iData);
		return listBundle;
	}

	private void initMap() {
		// TODO Auto-generated method stub
		DemoApplication app = (DemoApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(DemoApplication.strKey,
					new DemoApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_itemizedoverlay);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		initMapView();

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		mMapView.getController().setZoom(13);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);

		tog1 = (ToggleButton)findViewById(R.id.button1);
		tog1.setBackgroundDrawable(getResources().getDrawable(R.drawable.hotest));
		loc = (Button) findViewById(R.id.button2);
		loc.setBackgroundDrawable(getResources().getDrawable(R.drawable.location));
		fun = (Button)findViewById(R.id.button3);
		fun.setBackgroundDrawable(getResources().getDrawable(R.drawable.fun));

	}

	private void setOncheckedChangeListener() {
		OnCheckedChangeListener listener1 = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				getRangeEvents();
				// TODO Auto-generated method stub
				if (isChecked) {
					type = 0;// 设置如果开启详情页时应显示类型0，最热
					tog1.setBackgroundDrawable(getResources().getDrawable(R.drawable.newest));
					showHotEvents();
				} else {
					type = 1;// 设置如果开启详情页时应显示类型1，最近
					tog1.setBackgroundDrawable(getResources().getDrawable(R.drawable.hotest));
					showNearEvents();
				}
			}

		};
		OnCheckedChangeListener listener2 = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					tog1.setBackgroundDrawable(getResources().getDrawable(R.drawable.newest));
					type = 2;// 设置如果开启详情页时应显示类型0，最热
					showNewEvents();
				} else {
					type = 3;// 设置如果开启详情页时应显示类型1，最近
					tog1.setBackgroundDrawable(getResources().getDrawable(R.drawable.hotest));
					showRangeEvents();
				}
			}

		};

		tog1.setOnCheckedChangeListener(listener1);
		//tog2.setOnCheckedChangeListener(listener2);

		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub

				if (mapPoiInfo != null) {
					String title = mapPoiInfo.strText;
					Toast.makeText(ItemizedOverlayDemo.this, title,
							Toast.LENGTH_SHORT).show();
					Bitmap[] bmps = new Bitmap[2];
					try {
						bmps[1] = BitmapFactory
								.decodeStream(ItemizedOverlayDemo.this
										.getAssets().open("marker_detail.png"));
						bmps[0] = BitmapFactory
								.decodeStream(ItemizedOverlayDemo.this
										.getAssets().open("marker_bad.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					pop.showPopup(bmps, mapPoiInfo.geoPt, 50);
					activity.popGeoPt = mapPoiInfo.geoPt;
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub

			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager,
				mMapListener);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		OnClickListener BtnListener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button2:
					type = 2;// 设置如果开启详情页时应显示类型2，空的
					getRangeEvents();
					break;
				case R.id.button3:
					System.out.println("list!");
					ItemizedOverlayDemo.this.startActivity(new Intent(ItemizedOverlayDemo.this,
							FunActivity.class));
//					getHotEvents();
//					getNearEvents();
//					getNewEvents();
//					getRangeEvents();
					break;
				//case R.id.button5:
					// testUpdateClick();
					// gp = new GeoPoint((int) (location.getLatitude() *
					// 1e6),(int) (location.getLongitude() * 1e6));
					// mMapView.getController().animateTo(gp);
					// mMapController = mMapView.getController();
					

				//	break;
				/*
				 * case R.id.button1: listType = 0;// 设置如果开启详情页时应显示类型0，最热
				 * showHotEvents(); break; case R.id.button2: listType = 1;//
				 * 设置如果开启详情页时应显示类型1，最近 showNearEvents(); break; case
				 * R.id.button3: listType = 2;// 设置如果开启详情页时应显示类型2，空的
				 * testRemoveAllItemClick(); break; case R.id.button4:
				 * System.out.println("Locationing!");
				 * 
				 * break;
				 */
				}
			}
		};
		fun.setOnClickListener(BtnListener);
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager,
				mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
	}

	private void setValues() {
		// TODO Auto-generated method stub
		res.add(getResources().getDrawable(R.drawable.icon_marka));
		res.add(getResources().getDrawable(R.drawable.icon_markb));
		res.add(getResources().getDrawable(R.drawable.icon_markc));
		res.add(getResources().getDrawable(R.drawable.icon_markd));
		res.add(getResources().getDrawable(R.drawable.icon_markf));
		res.add(getResources().getDrawable(R.drawable.icon_markg));
		res.add(getResources().getDrawable(R.drawable.icon_markh));
		res.add(getResources().getDrawable(R.drawable.icon_marki));

		// // overlay 数量
		// int iSize = 9;
		// Random rnd = new Random();
		// // 准备overlay 数据
		// for (int i = 0; i < iSize; i++) {
		// int lat = cLat + rnd.nextInt(4000) - 2000;
		// int lon = cLon + rnd.nextInt(4000) - 2000;
		// OverlayItem item = new OverlayItem(new GeoPoint(lat, lon), "item"
		// + i, "item" + i);
		// item.setMarker(res.get(i % (res.size())));
		// mGeoList.add(item);
		// }
//		getHotEvents();
//		getNearEvents();
//		getNewEvents();
//		getRangeEvents();
	}

	private void getHotEvents() {
		System.out.println("getHotEvents");
		// hotEvents = getEventListFromXML("hot.xml");
		Bundle data = new Bundle();
		data.putInt("type", 0);
		GetListThread thread = new GetListThread(this, data);
		thread.start();
	}

	private void getNearEvents() {
		System.out.println("getNearEvents");
//		nearEvents = getEventListFromXML("near.xml");
		Bundle data = new Bundle();
		data.putInt("type", 1);
		data.putInt("lat",mMapView.getMapCenter().getLatitudeE6());
		data.putInt("lon",mMapView.getMapCenter().getLongitudeE6());
		GetListThread thread = new GetListThread(this, data);
		thread.start();
	}
	private void getNewEvents() {
		System.out.println("getNewEvents");
//		nearEvents = getEventListFromXML("near.xml");
		Bundle data = new Bundle();
		data.putInt("type", 2);
		data.putInt("lat",mMapView.getMapCenter().getLatitudeE6());
		data.putInt("lon",mMapView.getMapCenter().getLongitudeE6());
		GetListThread thread = new GetListThread(this, data);
		thread.start();
	}
	private void getRangeEvents() {
		System.out.println("getRangeEvents");
//		nearEvents = getEventListFromXML("near.xml");
		Bundle data = new Bundle();
		data.putInt("type", 3);
		data.putInt("lat",mMapView.getMapCenter().getLatitudeE6());
		data.putInt("lon",mMapView.getMapCenter().getLongitudeE6());
		int a,b;
		a=mMapView.getMapCenter().getLatitudeE6();
		b=mMapView.getLatitudeSpan()/2;
		System.out.println("a="+a+",b="+b);
		data.putInt("lat1",a-b);
		data.putInt("lon1",mMapView.getMapCenter().getLongitudeE6()-mMapView.getLongitudeSpan()/2);
		data.putInt("lat2",mMapView.getMapCenter().getLatitudeE6()+mMapView.getLatitudeSpan()/2);
		data.putInt("lon2",mMapView.getMapCenter().getLongitudeE6()+mMapView.getLongitudeSpan()/2);
		GetListThread thread = new GetListThread(this, data);
		thread.start();
	}

	public List<EventData> getEventListFromXML(String xmlName) {
		// TODO Auto-generated method stub
		try {
			InputStream is = this.getAssets().open(xmlName);
			MyPullParser parser = new MyPullParser();
			return parser.pullXMLResolve(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		getHotEvents();
		getNearEvents();
		getNewEvents();
//		getRangeEvents();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// mMapView.destroy();
		// super.onDestroy();

		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		DemoApplication app = (DemoApplication) this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		super.onDestroy();
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
		// mMapController.setMapClickEnable(true);
		// mMapView.setSatellite(false);
	}

	public void testRemoveAllItemClick() {
		// 清除所有添加的Overlay
		ov.removeAll();
		mMapView.refresh();

	}
	public void showNewEvents() {
		if (ov != null) {
			ov.removeAll();
		}
		fillGeoList(newEvents);
		ov.addItem(mGeoList);
		mMapView.refresh();
	}
	public void showRangeEvents() {
		if (ov != null) {
			ov.removeAll();
		}
		fillGeoList(rangeEvents);
		ov.addItem(mGeoList);
		mMapView.refresh();
	}
	public void showNearEvents() {
		if (ov != null) {
			ov.removeAll();
		}
		fillGeoList(nearEvents);
		ov.addItem(mGeoList);
		mMapView.refresh();
	}

	public void showHotEvents() {
		if (ov != null) {
			ov.removeAll();
		}
		fillGeoList(hotEvents);
		ov.addItem(mGeoList);
		mMapView.refresh();

	}

	private void fillGeoList(List<EventData> events) {
		// TODO Auto-generated method stub
		if (mGeoList == null) {
			mGeoList = new ArrayList<OverlayItem>();
		} else {
			mGeoList.clear();
		}
		for (EventData ed : events) {
			OverlayItem item = new OverlayItem(new GeoPoint(ed.getLat(),
					ed.getLon()), ed.getEventMessage(), ed.getEventMessage());
			item.setMarker(res.get(0));
			mGeoList.add(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
		private Context mContext = null;

		Toast mToast = null;

		public OverlayTest(Drawable marker, Context context, MapView mapView) {
			super(marker, mapView);
			this.mContext = context;
			// pop = new PopupOverlay(ItemizedOverlayDemo.mMapView,
			// new PopupClickListener() {
			//
			// @Override
			// public void onClickedPopup(int index) {
			// switch (index) {
			// case 0:
			// Toast.makeText(mContext, "顶了一下", Toast.LENGTH_SHORT)
			// .show();
			// break;
			// case 1:
			//
			// break;
			// case 2:
			// Toast.makeText(mContext, "踩了一下", Toast.LENGTH_SHORT)
			// .show();
			// break;
			// }
			// }
			// });
			// 自2.1.1 开始，使用 add/remove 管理overlay , 无需调用以下接口.
			// populate();

		}

		// 点击标记后弹出
		@Override
		protected boolean onTap(int index) {
			System.out.println("item onTap: " + index);

			Bitmap[] bmps = new Bitmap[2];

			try {
				// bmps[0] =
				// BitmapFactory.decodeStream(mContext.getAssets().open(
				// "marker_good.png"));
				// bmps[1] =
				// BitmapFactory.decodeStream(mContext.getAssets().open(
				// "marker_detail.png"));
				// bmps[2] =
				// BitmapFactory.decodeStream(mContext.getAssets().open(
				// "marker_bad.png"));
				bmps[1] = BitmapFactory.decodeStream(mContext.getAssets().open(
						"marker_detail.png"));
				bmps[0] = BitmapFactory.decodeStream(mContext.getAssets().open(
						"marker_bad.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			pop.showPopup(bmps, getItem(index).getPoint(), 50);
			activity.popGeoPt = getItem(index).getPoint();// 保存pop的坐标，供pop被点击的时候使用
			Toast.makeText(mContext, getItem(index).getTitle(),
					Toast.LENGTH_SHORT).show();

			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			if (pop != null) {
				pop.hidePop();
			}
			super.onTap(pt, mapView);
			return false;
		}

		// 自2.1.1 开始，使用 add/remove 管理overlay , 无需重写以下接口
		/*
		 * @Override protected OverlayItem createItem(int i) { return
		 * mGeoList.get(i); }
		 * 
		 * @Override public int size() { return mGeoList.size(); }
		 */
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			myLocationOverlay.setData(locData);
			mMapView.refresh();
			mMapController
					.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6)), mHandler
							.obtainMessage(1));
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}
}
