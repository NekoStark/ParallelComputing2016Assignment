package it.unifi.parallel.storm_images.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import it.unifi.parallel.storm_images.model.ImageResult;

public class ImageParser implements IRichBolt {

	private static final long serialVersionUID = -8097431159695785710L;
	private OutputCollector collector;

	@Override
	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("searchKey", "link"));

	}

	@Override
	public void execute(Tuple input) {
		ImageResult image = new ImageResult(input.getString(0));
		collector.emit( new Values(image.getSearchKey(), image.getLink()) );
		collector.ack(input);
	}

	@Override
	public void cleanup() {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}