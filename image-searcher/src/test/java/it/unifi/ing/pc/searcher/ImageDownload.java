package it.unifi.ing.pc.searcher;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

//XXX
public class ImageDownload {

	private static final String[] searchTerms = {"topolino"};
	private static final String fileLocation = "/Users/stark/Desktop/ciao/image.out";
	
	@Test
	public void test() throws Exception {
		for(String term : searchTerms) {
			appendResults( new GoogleImageSearcher().search(term, 30), term );
			appendResults( new BingImageSearcher().search(term, 3), term );
			appendResults( new FlickrSearcher().search(term, 3), term );
		}
	}
		
	
	private void appendResults(Set<Result> results, String term) {
		try {
			List<String> rezString = new LinkedList<>();
			
			for(Result r : results) {
				rezString.add( r.getImage()+"\t"+term+"\t"+r.getService()+"\t"+r.getTimeStamp() );
			}
			
			Files.write(Paths.get(fileLocation), rezString, UTF_8, APPEND, CREATE);
			
		} catch(IOException e) {
			
		}
	}
	
}
