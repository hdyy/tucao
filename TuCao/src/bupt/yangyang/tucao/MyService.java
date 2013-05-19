package bupt.yangyang.tucao;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class MyService extends Service {
	private String TAG = "DAUService";
	private NetWorker nw;
	private Bundle bd;
	private boolean bindFlag = false;
//	private UploadEventThread runNet;
	
	public Handler servicehd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.v(TAG, "DAU handler Message received");
			switch (msg.what) {
			case 0:
				bd = msg.getData();
				break;
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate()");		
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG, "onStartCommand,return START_NOT_STICKY");
		
//		runNet = new UploadEventThread(this);
//		runNet.start();
		
		return START_NOT_STICKY;
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "onBind()");
		bindFlag = true;
		return new ServiceBinder();
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy()");
		bindFlag = true;
		super.onDestroy();
	}

	public class ServiceBinder extends Binder {

		public Bundle getBundle() {

			return bd;
		}

	}
	public interface upDate {
		void upDateView(TextView tv, int Data);
	}

}
