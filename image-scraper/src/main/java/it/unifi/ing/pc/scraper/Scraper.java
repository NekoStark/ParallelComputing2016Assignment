package it.unifi.ing.pc.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.unifi.ing.pc.parser.Parser;
import it.unifi.ing.pc.service.Service;

public class Scraper {
	
	private Service service;
	private Parser parser;
	
	public Scraper(Service service, Parser parser) {
		this.service = service;
		this.parser = parser;
	}
	
	public Set<String> query(String term) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String stringUrl = service.getBaseUrl() + term;
		
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
				
	        HtmlPage page1 = webClient.getPage(stringUrl);
	        ScriptResult s = page1.executeJavaScript("window.scrollBy(0,500)");
	        webClient.waitForBackgroundJavaScript(10000);
	
	        HtmlPage page = (HtmlPage)s.getNewPage();
	       		
			return parser.parse(page);
	    }
	}
	
}
