package it.unifi.ing.pc.parser.jsoup;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleImageParser implements Parser {

	@Override
	public Set<String> parse(Document doc) {
		Elements elems = doc.select(".rg_di > .rg_meta");
		Set<String> result = new HashSet<>();
		for(Element elem : elems) {
			String imgUrl = StringUtils.getNestedString(elem.html().trim(), "\"ou\":\"", "\",\"ow\"");
			if(imgUrl != null && !imgUrl.trim().isEmpty()) {
				result.add( imgUrl.trim() );
			}
		}
		
		return result;
		
	}	
	
}
