package it.unifi.parallel.storm_images;

public class ImageResuls {

	protected String link;
	protected String searchKey;
	protected String service;
	protected String timestamp;
	
	public ImageResuls(String input) {
		String[] split = input.split("\t");
		this.link = split[0];
		this.searchKey = split[1];
		this.service = split[2];
		this.timestamp = split[3];
	}
	
}
