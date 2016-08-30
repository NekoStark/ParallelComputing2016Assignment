package it.unifi.parallel.storm_images.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import it.unifi.parallel.storm_images.model.ImageResuls;

public class ImageParser extends BaseBasicBolt {

	private static final long serialVersionUID = -8124116378426101454L;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("searchKey", "link"));

	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		ImageResuls image = new ImageResuls(input.getString(0));
		collector.emit( new Values(image.getSearchKey(), image.getLink()) );

	}

}

