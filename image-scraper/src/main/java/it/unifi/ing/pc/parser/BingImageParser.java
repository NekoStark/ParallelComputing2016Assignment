package it.unifi.ing.pc.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class BingImageParser implements Parser {

	@Override
	public Set<String> parse(HtmlPage page) {
		List<?> imgs = page.getByXPath("//div[@class='dg_u']/div/a");
		Set<String> result = new HashSet<>();
		for(Object img : imgs) {
			String imgUrl = StringUtils.getNestedString(((HtmlAnchor)img).getAttribute("m").toString().trim(), 
					"imgurl:\"", "\",tid");
			if(imgUrl != null && !imgUrl.trim().isEmpty()) {
				result.add( imgUrl.trim() );
			}
		}
		
		return result;
	}

}
