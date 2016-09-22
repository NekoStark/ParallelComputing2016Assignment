package it.unifi.parallel.storm_images.bolt;

import java.util.LinkedHashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import it.unifi.parallel.storm_images.model.ImageResult;

public class ImageParser extends BaseBasicBolt {

	private static final long serialVersionUID = 3912296783013470050L;

	@Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
		ImageResult image = new ImageResult(tuple.getString(0));
		
		Map<String, Object> event = new LinkedHashMap<>();
        event.put("timestamp", image.getTimestamp());
        event.put("service", image.getService());
        event.put("searchKey", image.getSearchKey());
        event.put("link", image.getLink());
        event.put("click", 1);
        collector.emit(new Values(event));
    }
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare( new Fields("event") );

	}

}