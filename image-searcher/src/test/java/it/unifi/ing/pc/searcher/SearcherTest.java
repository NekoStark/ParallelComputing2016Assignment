package it.unifi.ing.pc.searcher;

import java.util.Set;

import org.junit.Test;

public class SearcherTest {

	@Test
	public void test() {
		Set<String> result = new Searcher().search("pippo");
		System.out.println(result.size());

	}
	
}
