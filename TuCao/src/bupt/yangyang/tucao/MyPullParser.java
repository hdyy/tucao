package bupt.yangyang.tucao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class MyPullParser {
	public List<EventData> pullXMLResolve(InputStream is) {
		List<EventData> eventDatas = null;
		try {
			// // 方式一：使用Android提供的实用工具类android.util.Xml
			XmlPullParser xpp = Xml.newPullParser();
			xpp.setInput(is, "utf-8");

			EventData event = null;
			StringBuffer xmlHeader = null; // 保存xml头部
			String ele = null; // Element flag

			int eventType = xpp.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					eventDatas = new ArrayList<EventData>();
					xmlHeader = new StringBuffer();
					break;

				case XmlPullParser.START_TAG:
					if ("sarcasm_object".equals(xpp.getName())) {
						xmlHeader.append("sarcasm_object").append("\t\t");

					} else if ("sarcasm".equals(xpp.getName())) {
						event = new EventData();
					} else if ("longtitude".equals(xpp.getName())) {
						ele = "longtitude";
					} else if ("latitude".equals(xpp.getName())) {
						ele = "latitude";
					} else if ("msg".equals(xpp.getName())) {
						ele = "msg";
					} else if ("publish_time".equals(xpp.getName())) {
						ele = "publish_time";
					} else if ("up".equals(xpp.getName())) {
						ele = "up";
					} else if ("down".equals(xpp.getName())) {
						ele = "down";
					} else {
						ele = null;
					}
					break;

				case XmlPullParser.TEXT:
					if (null != ele) {
						if ("longtitude".equals(ele)) {
							event.setLon((int)(1000000*Double.parseDouble(xpp.getText())));
						} else if ("latitude".equals(ele)) {
							event.setLat((int)(1000000*Double.parseDouble(xpp.getText())));
						} else if ("msg".equals(ele)) {
							event.setEventMessage(xpp.getText());
						} else if ("publish_time".equals(ele)) {
							event.setPublishTime(xpp.getText());
						} else if ("up".equals(ele)) {
							event.setNumberGood(Integer.parseInt(xpp.getText()));
						} else if ("down".equals(ele)) {
							event.setNumberBad(Integer.parseInt(xpp.getText()));
						}
					}
					break;

				case XmlPullParser.END_TAG:
					if ("sarcasm".equals(xpp.getName())) {
						eventDatas.add(event);
						event = null;
					}
					ele = null;
					break;
				}
				eventType = xpp.next(); // 下一个事件类型
			}
			System.out.println(eventDatas);

		} catch (XmlPullParserException e) { // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventDatas;
	}

}