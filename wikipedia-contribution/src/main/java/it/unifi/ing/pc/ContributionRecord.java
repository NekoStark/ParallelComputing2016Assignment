package it.unifi.ing.pc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class ContributionRecord extends BaseBasicBolt {

	private static final long serialVersionUID = -1851336911778359170L;
	
	private static final Map<Integer, Set<String>> timestamps = new HashMap<Integer, Set<String>>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		addTimestamp(input.getInteger(2), input.getString(0)); 
	}

	private void addTimestamp(int contributorId, String timestamp) {
		Set<String> contributorTimestamps = timestamps.get(contributorId);
		if (contributorTimestamps == null) {
			contributorTimestamps = new HashSet<String>();
			timestamps.put(contributorId, contributorTimestamps);
		}
		contributorTimestamps.add(timestamp);
	}

}
