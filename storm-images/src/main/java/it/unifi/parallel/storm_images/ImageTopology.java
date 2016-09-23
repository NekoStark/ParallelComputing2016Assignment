package it.unifi.parallel.storm_images;

import java.util.Map;
import java.util.UUID;

import com.metamx.tranquility.storm.BeamBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import it.unifi.parallel.storm_images.bolt.ImageBeamFactory;
import it.unifi.parallel.storm_images.bolt.ImageParser;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class ImageTopology {

	private static final String ZK_CONNECT = "localhost:2181";
	private static final String KAFKA_TOPIC = "images";
	
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.setDebug(true);
		config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		
		BrokerHosts hosts = new ZkHosts(ZK_CONNECT);

		SpoutConfig kafkaSpoutConfig = new SpoutConfig(hosts, KAFKA_TOPIC, "/" + KAFKA_TOPIC, UUID.randomUUID().toString());
		kafkaSpoutConfig.bufferSizeBytes = 1024 * 1024 * 4;
		kafkaSpoutConfig.fetchSizeBytes = 1024 * 1024 * 4;
		kafkaSpoutConfig.forceFromStart = true;
		kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

		//BUILD TOPOLOGY
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka-spout", new KafkaSpout(kafkaSpoutConfig));
		builder.setBolt("image-parser", new ImageParser()).shuffleGrouping("kafka-spout");
		BeamBolt<Map<String, Object>> beamBolt = new BeamBolt<>( new ImageBeamFactory() );
		builder.setBolt("image-druid", beamBolt).shuffleGrouping("image-parser");
		
		//LOCAL MODE
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("image-topology", config, builder.createTopology());
//		Utils.sleep(10000);
		
//		cluster.killTopology("image-topology");
//		cluster.shutdown();
		
		//MANAGED MODE
//		config.setNumWorkers(20);
//		config.setMaxSpoutPending(5000);
//		StormSubmitter.submitTopology("image-topology", config, builder.createTopology());
	}
	
}
