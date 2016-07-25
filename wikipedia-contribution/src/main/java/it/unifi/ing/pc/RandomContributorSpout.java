package it.unifi.ing.pc;

import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class RandomContributorSpout extends BaseRichSpout {

	private static final long serialVersionUID = 8208027510626157679L;
	
	private static final Random rand = new Random();
	private static final DateTimeFormatter isoFormat = ISODateTimeFormat.dateTimeNoMillis();

	private SpoutOutputCollector collector;
	private int contributionId = 10000;

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
		Utils.sleep(rand.nextInt(100));
		++contributionId;
		String line = isoFormat.print(DateTime.now()) + " " + contributionId + " " + rand.nextInt(10000) + " "
				+ "dummyusername";
		collector.emit(new Values(line));
	}

}
