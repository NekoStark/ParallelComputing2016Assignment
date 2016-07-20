package it.unifi.ing.pc.scraper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

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
	private static final String USER_AGENTS = "Mozilla/5.0 (X11; Linux i686) "
			+ "AppleWebKit/537.17 (KHTML, like Gecko) "
			+ "Chrome/24.0.1312.27 Safari/537.17";
	
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
	
	public static void saveImage(String imageUrl) throws IOException {
		String destinationFile = "imgs" + imageUrl.substring(imageUrl.lastIndexOf("/"));
		URL url = new URL(imageUrl);	
		try{
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", USER_AGENTS);
			con.setConnectTimeout(2000);
			InputStream is = con.getInputStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
			os.close();
		} catch(FileNotFoundException|SocketTimeoutException|SSLHandshakeException e){
			System.out.println("Immagine non trovata");
		}
		
	}
	
}
