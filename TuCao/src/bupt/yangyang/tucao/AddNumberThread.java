package bupt.yangyang.tucao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.Bundle;

import android.util.Log;

//import android.widget.Toast;

/*
 * DAUThread is Data Acquisition Unit Thread
 * it's used for data acquisition and storage in SQLite
 */
public class AddNumberThread extends Thread {
	Bundle inputData;
	private long id;
	private boolean isGood;
	private EventDetailActivity context;
	private boolean runFlag = true;
	private String TAG = "AddNumberThread";
	private static boolean mFlag = true;

	public AddNumberThread(EventDetailActivity Activity, Bundle data) {
		this.context = Activity;
		inputData = data;
		// System.out.println("upload construct" +
		// data.getString("EventMessage"));
	}

	@Override
	public void run() {

		// while (runFlag) {
		// if (mFlag) {
		id = inputData.getLong("id");
		isGood=inputData.getBoolean("isGood");
		Log.v(TAG, "id=" + id + ",isGood=" + isGood);
		 dohttp();
	}

	public void dohttp() {
		HttpClient httpclient = new DefaultHttpClient();
		String url;
		// ÄãµÄURL
		if (isGood) {

			url = "http://192.168.22.11:8000/add_up/";
		} else {
			url = "http://192.168.22.11:8000/add_down/";
		}
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("id", event.getId() + ""));
			params.add(new BasicNameValuePair("id", id + ""));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("add up or down error");
			} else {
				System.out.println("add up or down Success");
			}

		} catch (ClientProtocolException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

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