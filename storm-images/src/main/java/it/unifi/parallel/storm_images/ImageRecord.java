package it.unifi.parallel.storm_images;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class ImageRecord extends BaseBasicBolt {

	private static final long serialVersionUID = 7114861203025617275L;
	private final static Map<String, String> images = new HashMap<String,String>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		if (images.containsKey(input.getValue(0).toString())){
			String value = images.get(input.getValue(0)) + input.getValue(1).toString() + "\t";
			images.put(input.getValue(0).toString(), value);
		} else{
			images.put(input.getValue(0).toString(), input.getValue(1).toString() + "\t");
		}
	}
	
	public static Map<String, String> getImages() {
		return images;
	}
	

}
