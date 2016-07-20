package it.unifi.ing.pc.scraper;

import java.io.IOException;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.unifi.ing.pc.parser.htmlunit.Parser;
import it.unifi.ing.pc.service.Service;

public class HtmlUnitScraper implements Scraper {

	private Service service;
	private Parser parser;
	
	public HtmlUnitScraper(Service service, Parser parser) {
		this.service = service;
		this.parser = parser;
	}
	
	@Override
	public Set<String> query(String term) {
		String stringUrl = service.getBaseUrl() + term;
		
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
				
	        HtmlPage page = webClient.getPage(stringUrl);
        	ScriptResult s = page.executeJavaScript("window.scrollBy(0,500)");
        	webClient.waitForBackgroundJavaScript(10000);
        	page = (HtmlPage) s.getNewPage();
	       		
			return parser.parse(page);
			
	    } catch (FailingHttpStatusCodeException | IOException e) {
			throw new RuntimeException(e);
			
		}
	}
	
}
