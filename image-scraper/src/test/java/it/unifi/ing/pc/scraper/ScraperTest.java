package it.unifi.ing.pc.scraper;

import java.util.Set;

import org.junit.Test;

import it.unifi.ing.pc.parser.GoogleImageParser;
import it.unifi.ing.pc.service.Service;

public class ScraperTest {

	@Test
	public void test() {
		Set<String> result = new HtmlUnitScraper(Service.GOOGLE, new GoogleImageParser()).query("pippo");
		System.out.println(result.size());
//		for(String s : result) {
//			System.out.println(s);
//		}
		
//		result = new Scraper(Service.BING, new BingImageParser()).query("pippo");
//		for(String s : result) {
//			System.out.println(s);
//		}
	}
	
}
