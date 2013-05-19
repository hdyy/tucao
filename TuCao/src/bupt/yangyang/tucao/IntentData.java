package bupt.yangyang.tucao;

import java.io.Serializable;
import java.util.List;

public class IntentData implements Serializable{
	// ´æ·Å×îÈÈ²Ûµã
		public static final String name="IDATA";
		public List<EventData> hotEvents;
		public List<EventData> nearEvents;
		public List<EventData> newEvents;
		public List<EventData> rangeEvents;
		
		public void setHotEvents(List<EventData> list){
			hotEvents=list;
		}
		public List<EventData> getHotEvents( ){
			return hotEvents;
		}
		public void setNearEvents(List<EventData> list){
			nearEvents=list;
		}
		public List<EventData> getNearEvents( ){
			return nearEvents;
		}
		public void setNewEvents(List<EventData> newEvents) {
			// TODO Auto-generated method stub
			this.newEvents=newEvents;
		}
		public void setRangeEvents(List<EventData> rangeEvents) {
			// TODO Auto-generated method stub
			this.rangeEvents=rangeEvents;
		}
}
