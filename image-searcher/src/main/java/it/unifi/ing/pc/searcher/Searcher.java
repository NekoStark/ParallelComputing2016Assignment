package it.unifi.ing.pc.searcher;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Searcher {

	private Properties properties;
	
	public final Set<Result> search(String term) {
		return search(term, 1);
	}
	
	public final Set<Result> search(String term, int pages) {
		OkHttpClient client = new OkHttpClient();
		Set<Result> results = new HashSet<>();
		
		for (int i = 0; i < pages; i++) {
			Request request = buildRequest(term, i);

			try (Response response = client.newCall(request).execute()) {
				results.addAll( parseResult( response.body().string() ) );
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return results;
	}
	

	//
	// PRIVATE METHODS
	//
	
	final String getProperty(String key) {
		if(properties == null) {
			properties = initProperties();
		}
		return properties.get(key).toString();
	}

	//
	// ABSTRACT METHODS
	//
	
	abstract Properties initProperties();
	abstract Request buildRequest(String term, int page);
	abstract Set<Result> parseResult(String responseBody);
	
}
