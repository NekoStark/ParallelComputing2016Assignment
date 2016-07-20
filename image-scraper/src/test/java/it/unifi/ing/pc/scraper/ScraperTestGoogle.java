package it.unifi.ing.pc.scraper;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import it.unifi.ing.pc.parser.htmlunit.GoogleImageParser;
import it.unifi.ing.pc.service.Service;

public class ScraperTestGoogle {
	@Test
	public void test() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Set<String> result = new HtmlUnitScraper(Service.GOOGLE, new GoogleImageParser()).query("pippo");
		assertEquals(100, result.size());
	}
}
