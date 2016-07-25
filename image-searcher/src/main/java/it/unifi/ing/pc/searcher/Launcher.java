package it.unifi.ing.pc.searcher;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import it.unifi.ing.pc.parser.htmlunit.BingImageParser;
import it.unifi.ing.pc.parser.htmlunit.GoogleImageParser;
import it.unifi.ing.pc.scraper.HtmlUnitScraper;
import it.unifi.ing.pc.service.Service;

public class Launcher {

	public static void main(String[] args) {
		Path path = Paths.get( new File("/Users/stark/Desktop/prova.out").toURI() );
		
		for(String term : args) {
			writeToFile(path, term, searchGoogle(term));
			writeToFile(path, term, searchBing(term));
//			writeToFile(path, term, searchFlickr(term));
			
		}
		
	}
	
	private static Set<String> searchGoogle(String term) {
		Set<String> result = new HashSet<>();
		new HtmlUnitScraper(Service.GOOGLE, new GoogleImageParser())
			.query(term)
			.forEach(s -> result.add( s+"\t"+term+"\tGOOGLE" ));

		return result;
	}
	
	private static Set<String> searchBing(String term) {
		Set<String> result = new HashSet<>();
		new HtmlUnitScraper(Service.BING, new BingImageParser())
			.query(term)
			.forEach(s -> result.add( s+"\t"+term+"\tBING" ));

		return result;
	}
	
	private static Set<String> searchFlickr(String term) {
		Set<String> result = new HashSet<>();
		new FlickrSearcher()
			.search(term)
			.forEach(s -> result.add( s+"\t"+term+"\tFLICKR" ));

		return result;
	}

	private static void writeToFile(Path path, String term, Set<String> lines) {
		try {
			Files.write(path, lines, APPEND, CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
