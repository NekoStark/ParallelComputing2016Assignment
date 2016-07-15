package it.unifi.ing.pc.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import it.unifi.ing.pc.parser.Parser;
import it.unifi.ing.pc.service.Service;

public class Scraper {

	private static final String USER_AGENTS = "Mozilla/5.0 (X11; Linux i686) "
			+ "AppleWebKit/537.17 (KHTML, like Gecko) "
			+ "Chrome/24.0.1312.27 Safari/537.17";
	
	private Service service;
	private Parser parser;
	
	public Scraper(Service service, Parser parser) {
		this.service = service;
		this.parser = parser;
	}
	
	public Set<String> query(String term) {
		String stringUrl = service.getBaseUrl() + term;
		InputStream is = null;
		try {
			is = getStream( new URL( stringUrl ) );
			Document doc = Jsoup.parse(is, null, stringUrl);
			
			return parser.parse(doc);

		} catch(IOException e) {
			throw new RuntimeException(e);
			
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch(IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private InputStream getStream(URL url) {
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", USER_AGENTS);
			return con.getInputStream();
		
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
