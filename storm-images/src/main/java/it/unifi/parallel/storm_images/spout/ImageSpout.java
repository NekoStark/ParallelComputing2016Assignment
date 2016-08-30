package it.unifi.parallel.storm_images.spout;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import it.unifi.ing.pc.searcher.*;

public class ImageSpout extends BaseRichSpout {

	private static final long serialVersionUID = 3421415774404184763L;
	private static final String fileLocation = "/home/tommi/Scrivania/Pippo/image.out";
//	private static final String[] searchTerms = {"topolino"};
	private int listIndex = 0;
	
	private SpoutOutputCollector collector;
	
	@Override
	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	@Override
	public void nextTuple() {
		if (listIndex < getLines().size()){
			String line = getLines().get(listIndex);
			++listIndex;
			collector.emit(new Values(line));
		}
	}
	
	public List<String> getLines(){
		
//		for(String term : searchTerms) {
//			appendResults( new GoogleImageSearcher().search(term, 30), term );
//			appendResults( new BingImageSearcher().search(term, 3), term );
//			appendResults( new FlickrSearcher().search(term, 3), term );
//		}
		
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileLocation));
			while ((sCurrentLine = br.readLine()) != null) {
				lines.add(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return lines;
	}
	
	// Aggiunge le nuove immagini al raw file
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
