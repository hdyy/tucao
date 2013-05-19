package bupt.yangyang.tucao;

import java.io.Serializable;

public class EventData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3508558016467125886L;
	private long id=1;
	private String eventMessage;
	private int Lat;
	private int Lon;
	private int numberGood;
	private int numberBad;
	private String publishTime;

	public void setId(long id){
		this.id=id;
	}
	public long getId(){
		return id;
	}
	public void setPublishTime(String time){
		publishTime=time;
	}
	public String getPublishTime(){
		return publishTime;
	}

	public void setEventMessage(String msg) {
		eventMessage = msg;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setPosition(int Lat, int Lon) {
		this.Lat = Lat;
		this.Lon = Lon;
	}

	public int[] getPosition() {
		int[] position = { Lat, Lon };
		return position;
	}

	public void setLat(int lat) {
		this.Lat = lat;
	}

	public int getLat() {
		return Lat;
	}

	public void setLon(int lon) {
		this.Lon = lon;
	}

	public int getLon() {
		return Lon;
	}

	public void setNumberGood(int number) {
		numberGood = number;
	}

	public int getNumberGood() {
		return numberGood;
	}

	public void setNumberBad(int number) {
		numberBad = number;
	}

	public int getNumberBad() {
		return numberBad;
	}

	public void setComment(int good, int bad) {
		numberGood = good;
		numberBad = bad;
	}
}
