package it.unifi.ing.pc.searcher;

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

public class BingImageSearcher extends Searcher {

	private static final String SERVICE_NAME = "BING";
	private static final int PAGE_SIZE = 100;
	
	private String freshness;
	
	public BingImageSearcher() {
		super();
	}
	public BingImageSearcher(ImageFreshness freshness) {
		super();
		this.freshness = freshness.getBingCode();
	}
	
	private String buildRequestUrl(String term, int page) {
		StringBuffer sb = new StringBuffer();
		sb.append(getProperty("service.url"))
				.append("?safeSearch=")
				.append(getProperty("service.safe"))
				.append("&q=")
				.append(term)
				.append("&count=")
				.append(PAGE_SIZE)
				.append("&offset=")
				.append(page*PAGE_SIZE);
		
		if(StringUtils.isNotEmpty(freshness)) {
			sb.append("&freshness=")
			.append(freshness);
		}
		
		return sb.toString();
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
	Set<Result> parseResult(String responseBody) {
		Set<Result> result = new HashSet<>();
		JsonArray photos = new JsonParser().parse(responseBody)
						.getAsJsonObject().getAsJsonArray("value");
		for (JsonElement photo : photos) {
			result.add( 
				new Result( photo.getAsJsonObject().get("contentUrl").getAsString(),
						SERVICE_NAME, photo.getAsJsonObject().get("datePublished").getAsString() ) );
		}
		
		return result;
	}
	
}
