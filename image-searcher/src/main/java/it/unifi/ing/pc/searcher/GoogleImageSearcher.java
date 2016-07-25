package it.unifi.ing.pc.searcher;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.unifi.ing.pc.utilities.PropertyLoader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleImageSearcher {

	Properties p = PropertyLoader.asProperties("/google-api.properties");
	
	public Set<String> search(String term) {
		Set<String> result = new HashSet<>();
		OkHttpClient client = new OkHttpClient();
		
		Request request = new Request.Builder()
				.url( buildRequestUrl() )
				.build();

		try (Response response = client.newCall(request).execute()) {
			JsonArray photos = new JsonParser().parse(response.body().string()).getAsJsonObject()
					.getAsJsonArray("items");
			for (JsonElement photo : photos) {
				result.add( photo.getAsJsonObject().get("link").getAsString() );
			}
			
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String buildRequestUrl() {
		StringBuffer sb = new StringBuffer();
		sb.append(p.getProperty("service.url"));
		sb.append("?method=");
		sb.append(p.getProperty("service.method"));
		sb.append("&api_key=");
		sb.append(p.getProperty("service.apikey"));
		sb.append("&api_sig=");
		sb.append(p.getProperty("service.apisig"));
		sb.append("&format=json&nojsoncallback=1");
		sb.append("&text=");
		sb.append("pippo");
		
		return sb.toString();
	}
	
}
