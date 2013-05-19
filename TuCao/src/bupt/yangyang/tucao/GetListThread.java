package bupt.yangyang.tucao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Message;

//import android.widget.Toast;

/*
 * DAUThread is Data Acquisition Unit Thread
 * it's used for data acquisition and storage in SQLite
 */
public class GetListThread extends Thread {
	int type;
	Bundle data;
	private ItemizedOverlayDemo context;
	private boolean runFlag = true;
	private String TAG = "GetListThread";
	private static boolean mFlag = true;

	public List<EventData> targetEvents;// 此线程的目标列表

	public GetListThread(ItemizedOverlayDemo context, Bundle data) {
		this.context = context;
		this.data = data;
		type = data.getInt("type");
		System.out.println("upload construct" +
		data.getString("EventMessage"));
	}

	private Bundle getListBundle() {
		// TODO Auto-generated method stub
		Bundle listBundle = new Bundle();
		IntentData iData = new IntentData();
		iData.setHotEvents(targetEvents);
		listBundle.putSerializable(IntentData.name, iData);
		return listBundle;
	}

	@Override
	public void run() {

		// while (runFlag) {
		// if (mFlag) {
		// id = inputData.getLong("id");
		// isGood=inputData.getBoolean("isGood");
		getTargetEvents();
		Bundle listBundle = getListBundle();
		Message msg = Message.obtain();
		msg.what = type;
		System.out.println("type="+type+",in getlist : "
				+ ((IntentData) listBundle.getSerializable(IntentData.name))
						.getHotEvents());
		msg.setData(listBundle);
		context.listHD.sendMessage(msg);
	}

	public void getTargetEvents() {
		System.out.println("doing http");
		String url = null;
		//设置param
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("id", event.getId() + ""));
		params.add(new BasicNameValuePair("longtitude", Double.parseDouble(data
				.getInt("lon") / 1000000.0 + "")
				+ ""));
		params.add(new BasicNameValuePair("latitude", Double.parseDouble(data
				.getInt("lat") / 1000000.0 + "")
				+ ""));
		params.add(new BasicNameValuePair("longtitude1", Double.parseDouble(data
				.getInt("lon1") / 1000000.0 + "")
				+ ""));
		params.add(new BasicNameValuePair("latitude1", Double.parseDouble(data
				.getInt("lat1") / 1000000.0 + "")
				+ ""));
		params.add(new BasicNameValuePair("longtitude2", Double.parseDouble(data
				.getInt("lon2") / 1000000.0 + "")
				+ ""));
		params.add(new BasicNameValuePair("latitude2", Double.parseDouble(data
				.getInt("lat2") / 1000000.0 + "")
				+ ""));
		
		switch (type) {
		// 得到数据库里最热的十条
		case 0:
			url = "http://192.168.22.11:8000/get_hotest_sarcasms/";
			break;
		// 得到某个地点最热的10条
		case 1:
			url = "http://192.168.22.11:8000/get_hotest_sarcasms_by_location/";

			break;
		// 得到某个地点最新的10条
		case 2:
			url = "http://192.168.22.11:8000/get_newest_sarcasms_by_location/";
			break;
		case 3:
//			url = "http://192.168.22.11:8000/get_newest_sarcasms_by_location/";
			url = "http://192.168.22.11:8000/get_hotest_sarcasms_by_range/";
		}

		HttpResponse response;
		String result;
		
		try {
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			response = client.execute(post);// 执行post方法
			result = EntityUtils.toString(response.getEntity());
			System.out.println("type="+type+",result="+result);
			InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
			MyPullParser parser = new MyPullParser();
			targetEvents = parser.pullXMLResolve(is);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void stopThread() {
		runFlag = false;
	}

	public static void setFlag(boolean flag) {
		mFlag = flag;
	}

	public static boolean getFlag() {
		boolean flag = mFlag;
		return flag;
	}
}