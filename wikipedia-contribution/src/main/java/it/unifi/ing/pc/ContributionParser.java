package it.unifi.ing.pc;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ContributionParser extends BaseBasicBolt {

	private static final long serialVersionUID = -8124116378426101454L;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("timestamp", "id", "contributorId", "username"));

	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Contribution contribution = new Contribution(input.getString(0));
		collector.emit( new Values(
				contribution.timestamp, contribution.id, 
				contribution.contributorId, contribution.username));

	}

}
