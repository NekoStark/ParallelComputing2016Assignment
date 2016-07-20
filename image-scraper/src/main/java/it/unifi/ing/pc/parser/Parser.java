package it.unifi.ing.pc.parser;

import java.util.Set;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface Parser {

	public Set<String> parse(HtmlPage page);
	
}
