package it.unifi.ing.pc.scraper;

import java.util.Set;

public interface Scraper {
	
	public Set<String> query(String term);
	
}
