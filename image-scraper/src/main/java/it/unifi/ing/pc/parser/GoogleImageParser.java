package it.unifi.ing.pc.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class GoogleImageParser implements Parser {

	@Override
	public Set<String> parse(HtmlPage page) {
		List<?> imgs = page.getByXPath("//div[@class='rg_meta']");
		Set<String> result = new HashSet<>();
		for(Object img : imgs) {
			String imgUrl = StringUtils.getNestedString((
					(HtmlDivision)img).getTextContent().toString().trim(), "\"ou\":\"", "\",\"ow\"");
			if(imgUrl != null && !imgUrl.trim().isEmpty()) {
				result.add( imgUrl.trim() );
			}
		}
		
		return result;
		
	}	
	
}
