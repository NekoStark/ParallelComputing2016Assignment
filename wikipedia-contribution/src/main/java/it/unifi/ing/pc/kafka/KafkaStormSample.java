package it.unifi.ing.pc.kafka;

import java.util.UUID;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class KafkaStormSample {

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.setDebug(true);
		config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		String zkConnString = "localhost:2181";
		String topic = "test2";
		BrokerHosts hosts = new ZkHosts(zkConnString);

		SpoutConfig kafkaSpoutConfig = new SpoutConfig(hosts, topic, "/" + topic, UUID.randomUUID().toString());
		kafkaSpoutConfig.bufferSizeBytes = 1024 * 1024 * 4;
		kafkaSpoutConfig.fetchSizeBytes = 1024 * 1024 * 4;
		kafkaSpoutConfig.forceFromStart = true;
		kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka-spout", new KafkaSpout(kafkaSpoutConfig));
		builder.setBolt("word-spitter", new SplitBolt2()).shuffleGrouping("kafka-spout");
		builder.setBolt("word-counter", new ImageRecord()).shuffleGrouping("word-spitter");

//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology("KafkaStormSample", config, builder.createTopology());
		
		Config conf = new Config();
		conf.setNumWorkers(20);
		conf.setMaxSpoutPending(5000);
		StormSubmitter.submitTopology("mytopology", conf, builder.createTopology());
	}

}
