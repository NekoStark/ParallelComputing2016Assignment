package it.unifi.ing.pc.parser.jsoup;

import java.util.Set;

import org.jsoup.nodes.Document;

public interface Parser {

	public Set<String> parse(Document doc);
	
}
