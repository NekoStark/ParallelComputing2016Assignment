package it.unifi.ing.pc.searcher.utils;

import static it.unifi.ing.pc.searcher.utils.ResourceLoader.asURL;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

	public static Properties asProperties(String fileName) {
		Properties p = new Properties();
		try (InputStream in = asURL(fileName).openStream()) {
			p.load(in);
			return p;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}