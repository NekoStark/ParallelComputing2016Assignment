package it.unifi.ing.pc.searcher;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.unifi.ing.pc.utilities.PropertyLoader;
import okhttp3.Request;

public class FlickrSearcher extends Searcher {

	private static final String SERVICE_NAME = "FLICKR"; 
	
	private String buildRequestUrl(String term, int page) {
		StringBuffer sb = new StringBuffer();
		
		//FIXME timestamp
//		String date = LocalDateTime.now()
//				.format( DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm") );
		
		return sb.append(getProperty("service.url"))
			.append("?method=")
			.append(getProperty("service.method"))
			.append("&api_key=")
			.append(getProperty("service.apikey"))
			.append("&api_sig=")
			.append(getProperty("service.apisig"))
			.append("&text=")
			.append(term)
			.append("&page=")
			.append(page)
			.append("&extras=")
			.append(getProperty("service.extras"))
//			.append("&min_upload_date=")
//			.append(date)
			.append("&format=json&nojsoncallback=1")
			.toString();
	}
	
	@Override
	Properties initProperties() {
		return PropertyLoader.asProperties("/flickr-api.properties");
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
						.getAsJsonObject().getAsJsonObject("photos")
						.getAsJsonArray("photo");
		
		for (JsonElement photo : photos) {
			String imageUrl = buildImageUrl( string(photo, "farm"), string(photo, "server"),
					string(photo, "id"), string(photo, "secret") );
			
			result.add( new Result(imageUrl, SERVICE_NAME, string(photo, "date_upload")) );
		}
		
		return result;
	}
	
	private String string(JsonElement elem, String property) {
		return elem.getAsJsonObject().get(property).getAsString();
	}
	
	private String buildImageUrl(String farmId, String serverId, 
			String imageId, String secret) {
		StringBuffer sb = new StringBuffer();
		return sb.append("https://farm")
				.append(farmId)
				.append(".staticflickr.com/")
				.append(serverId)
				.append("/")
				.append(imageId)
				.append("_")
				.append(secret)
				.append(".jpg").toString();
	}

}
