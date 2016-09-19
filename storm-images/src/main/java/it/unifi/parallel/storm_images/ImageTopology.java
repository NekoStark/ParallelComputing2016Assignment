package it.unifi.parallel.storm_images;

import java.util.UUID;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import it.unifi.parallel.storm_images.bolt.ImageParser;
import it.unifi.parallel.storm_images.bolt.ImageRecord;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class ImageTopology {

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
		builder.setBolt("image-parser", new ImageParser()).shuffleGrouping("kafka-spout");
		builder.setBolt("image-record", new ImageRecord()).shuffleGrouping("image-parser");
		
//		BeamBolt<Map<String, Object>> beamBolt = new BeamBolt<Map<String, Object>>( new ImageBeamFactory() );
//		builder.setBolt("image-record", beamBolt).shuffleGrouping("image-parser");
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("image-topology", config, builder.createTopology());
		Utils.sleep(5000);
		
		cluster.killTopology("image-topology");
		cluster.shutdown();
		
//		config.setNumWorkers(20);
//		config.setMaxSpoutPending(5000);
//		StormSubmitter.submitTopology("image-topology", config, builder.createTopology());
	}
}
