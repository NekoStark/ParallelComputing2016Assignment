package it.unifi.ing.pc.searcher;

import java.util.Set;

import org.junit.Test;

public class FlickrSearcherTest {

	@Test
	public void test() {
		Set<String> result = new FlickrSearcher().search("pippo");
		System.out.println(result.size());

	}
	
}
