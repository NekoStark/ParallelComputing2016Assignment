package it.unifi.ing.pc.kafka;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class ImageRecord extends BaseBasicBolt {

	private static final long serialVersionUID = 7114861203025617275L;
	private static final String BASE_PATH = "/Users/stark/Desktop/ciao/";
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		appendResults(input.getValue(0).toString(), input.getValue(1).toString());
	}
	
	private void appendResults(String key, String value) {
		try {
			Files.write(Paths.get(BASE_PATH + key), Collections.singleton( value ), UTF_8, APPEND, CREATE);
			
		} catch(IOException e) {
			
		}
	}
	

}
