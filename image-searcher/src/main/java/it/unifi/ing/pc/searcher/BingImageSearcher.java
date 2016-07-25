package it.unifi.ing.pc.searcher;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.unifi.ing.pc.utilities.PropertyLoader;
import okhttp3.Request;

public class BingImageSearcher extends Searcher {

	private static final int PAGE_SIZE = 100;
	
	//TODO filtrare per giorno
	private String buildRequestUrl(String term, int page) {
		StringBuffer sb = new StringBuffer();
		
		return sb.append(getProperty("service.url"))
				.append("?safeSearch=")
				.append(getProperty("service.safe"))
				.append("&q=")
				.append(term)
				.append("&count=")
				.append(PAGE_SIZE)
				.append("&offset=")
				.append(page*PAGE_SIZE)
				.toString();
	}

	@Override
	Properties initProperties() {
		return PropertyLoader.asProperties("/bing-api.properties");
	}

	@Override
	Request buildRequest(String term, int page) {
		return new Request.Builder()
				.url( buildRequestUrl(term, page) )
				.header("Ocp-Apim-Subscription-Key", getProperty("service.key"))
				.build();
	}

	@Override
	Set<String> parseResult(String responseBody) {
		Set<String> result = new HashSet<>();
		JsonArray photos = new JsonParser().parse(responseBody)
						.getAsJsonObject().getAsJsonArray("value");
		for (JsonElement photo : photos) {
			result.add( photo.getAsJsonObject().get("contentUrl").getAsString() );
		}
		
		return result;
	}
	
}
