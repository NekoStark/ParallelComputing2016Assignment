package it.unifi.ing.pc.searcher;

public enum ImageFreshness {

	DAY("day", "d1"),
	WEEK("week", "w1"),
	MONTH("month", "m1");
	
	private String bingCode;
	private String googleCode;
	
	private ImageFreshness(String bingCode, String googleCode) {
		this.bingCode = bingCode;
		this.googleCode = googleCode;
	}
	
	
	public String getBingCode() {
		return bingCode;
	}
	public String getGoogleCode() {
		return googleCode;
	}
	
}
