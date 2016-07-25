package it.unifi.ing.pc.searcher;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.unifi.ing.pc.utilities.PropertyLoader;
import okhttp3.Request;

public class GoogleImageSearcher extends Searcher {

	private static final int PAGE_SIZE = 10;
	
	private String buildRequestUrl(String term, int page) {
		StringBuffer sb = new StringBuffer();

		return sb.append(getProperty("service.url"))
				.append("?key=")
				.append(getProperty("service.key"))
				.append("&cx=")
				.append(getProperty("service.cx"))
				.append("&searchType=")
				.append(getProperty("service.type"))
				.append("&dateRestrict=")
				.append(getProperty("service.dateRestrict"))
				.append("&safe=")
				.append(getProperty("service.safe"))
				.append("&q=")
				.append(term)
				.append("&start=")
				.append(PAGE_SIZE*page + 1)
				.append("&sort=")
				.append(getProperty("service.sort"))
				.toString();
	}

	@Override
	Properties initProperties() {
		return PropertyLoader.asProperties("/google-api.properties");
	}

	@Override
	Request buildRequest(String term, int page) {
		return new Request.Builder()
				.url( buildRequestUrl(term, page) )
				.build();
	}

	@Override
	Set<String> parseResult(String responseBody) {
		Set<String> result = new HashSet<>();
		JsonArray photos = new JsonParser().parse(responseBody)
						.getAsJsonObject().getAsJsonArray("items");
		for (JsonElement photo : photos) {
			result.add( photo.getAsJsonObject().get("link").getAsString() );
		}
		
		return result;
	}
	
}
