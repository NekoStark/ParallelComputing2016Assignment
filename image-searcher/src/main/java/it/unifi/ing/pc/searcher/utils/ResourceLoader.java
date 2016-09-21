package it.unifi.ing.pc.searcher.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceLoader {

	public static URL asURL(String filePath) {
		return ResourceLoader.class.getResource(filePath);
	}
	
	public static URI asURI(String filePath) {
		try {
			return asURL(filePath).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File asFile(String filePath) {
		return new File( asURI(filePath) );
	}
	
}
