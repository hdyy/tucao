package bupt.yangyang.tucao;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
public class UploadEventThread extends Thread {
	Bundle bd, inputData;

	private EventData event;
	private int mcount = 0;
	private NewEventActivity context;
	private boolean runFlag = true;
	private String TAG = "UploadEventThread";
	private static boolean mFlag = true;

	public UploadEventThread(NewEventActivity newEventActivity, Bundle data) {
		this.context = newEventActivity;
		inputData = data;
		// System.out.println("upload construct" +
		// data.getString("EventMessage"));
	}

	@Override
	public void run() {

		bd = new Bundle();
		event = new EventData();
		event.setId(12345678);
		event.setEventMessage(inputData.getString("EventMessage"));
		event.setLat(39959013);
		event.setLon(116351791);
		event.setNumberGood(0);
		event.setNumberBad(0);
		event.setPublishTime(getPublishTime());
		// while (runFlag) {
		// if (mFlag) {
		Log.v(TAG,
				"Message sendding,message=" + event.getEventMessage() + ",lat="
						+ event.getLat() + ",lon=" + event.getLon() + ",good="
						+ event.getNumberGood() + ",bad="
						+ event.getNumberBad() + ",time="
						+ event.getPublishTime());
		dohttp();
	}

	private String getPublishTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String publishTime = sDateFormat.format(new java.util.Date());
		return publishTime;
	}

	public void dohttp() {
		HttpClient httpclient = new DefaultHttpClient();

		// ÄãµÄURL
		HttpPost httppost = new HttpPost(
				"http://192.168.22.11:8000/send_sarcasm/");
		try {
			// List<NameValuePair> params = new ArrayList<NameValuePair>();
			// // params.add(new BasicNameValuePair("id", event.getId() + ""));
			// params.add(new BasicNameValuePair("longtitude", Double
			// .parseDouble(event.getLon() / 1000000.0 + "") + ""));
			// params.add(new BasicNameValuePair("latitude", Double
			// .parseDouble(event.getLat() / 1000000.0 + "") + ""));
			// params.add(new BasicNameValuePair("msg",
			// event.getEventMessage()));
			// params.add(new BasicNameValuePair("publish_time", event
			// .getPublishTime()));
			// params.add(new BasicNameValuePair("up", event.getNumberGood() +
			// ""));
			// params.add(new BasicNameValuePair("down", event.getNumberBad() +
			// ""));
			// httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			String url = "http://" + MainActivity.ip + ":8000/upload_image/";
			String path = "/mnt/sdcard/DCIM/hello.jpg";
			MultipartEntity mpEntity = new MultipartEntity();
			File file = new File(path);
			if (file.exists()) {
				System.out.println("exist!!!");
			}
			mpEntity.addPart("image", new FileBody(file, "image/jpeg"));
			mpEntity.addPart(
					"longtitude",
					new StringBody(Double.parseDouble(event.getLon()
							/ 1000000.0 + "")
							+ ""));

			mpEntity.addPart(
					"latitude",
					new StringBody(Double.parseDouble(event.getLat()
							/ 1000000.0 + "")
							+ ""));
			mpEntity.addPart("msg", new StringBody(event.getEventMessage(),
					Charset.forName("UTF-8")));
			mpEntity.addPart("publish_time",
					new StringBody(event.getPublishTime()));

			httppost.setEntity(mpEntity);
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("new event upload error");
			} else {
				System.out.println("new event upload Success");
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