package it.unifi.ing.pc.searcher.model;

public class Result {

	private String image;
	private String service;
	private String timeStamp;
	
	public Result(String image, String service, String timeStamp) {
		this.image = image;
		this.service = service;
		this.timeStamp = timeStamp;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getService() {
		return service;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
}
