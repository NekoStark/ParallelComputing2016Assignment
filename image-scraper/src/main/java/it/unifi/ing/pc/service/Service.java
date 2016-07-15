package it.unifi.ing.pc.service;

public enum Service {

	GOOGLE("https://www.google.it/search?hl=it&site=imghp&tbm=isch&q="), 
	BING("https://www.bing.com/images/search?q=");
	
	private String baseUrl;
	
	private Service(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
}
