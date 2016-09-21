package it.unifi.ing.pc.searcher;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.unifi.ing.pc.searcher.model.Result;
import it.unifi.ing.pc.searcher.utils.PropertyLoader;
import okhttp3.Request;

public class GoogleImageSearcher extends Searcher {

	private static final String SERVICE_NAME = "GOOGLE"; 
	private static final int PAGE_SIZE = 10;
	
	private String freshness;
	
	public GoogleImageSearcher() {
		super();
	}
	public GoogleImageSearcher(ImageFreshness freshness) {
		super();
		this.freshness = freshness.getGoogleCode();
	}
	
	private String buildRequestUrl(String term, int page) {
		StringBuffer sb = new StringBuffer();

		sb.append(getProperty("service.url"))
			.append("?key=")
			.append(getProperty("service.key"))
			.append("&cx=")
			.append(getProperty("service.cx"))
			.append("&searchType=")
			.append(getProperty("service.type"))
			.append("&safe=")
			.append(getProperty("service.safe"))
			.append("&q=")
			.append(term)
			.append("&start=")
			.append(PAGE_SIZE*page + 1)
			.append("&sort=")
			.append(getProperty("service.sort"));
	
		if(StringUtils.isNotEmpty(freshness)) {
			sb.append("&dateRestrict=")
		.		append(freshness);
		}
		
		return sb.toString();
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
	Set<Result> parseResult(String responseBody) {
		Set<Result> result = new HashSet<>();
		JsonArray photos = new JsonParser().parse(responseBody)
						.getAsJsonObject().getAsJsonArray("items");
		for (JsonElement photo : photos) {
			result.add( new Result(
					photo.getAsJsonObject().get("link").getAsString(),
					SERVICE_NAME, LocalDateTime.now().toString()) );
		}
		
		return result;
	}
	
}
