package it.unifi.ing.pc.image.publisher.message.properties;

import java.util.Properties;

public class KafkaProducerProperties {

	public static final Properties PROPERTIES = init();
	
	private static Properties init() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 1);
		props.put("batch.size", 16384);
		props.put("linger.ms", 0);
		props.put("block.on.buffer.full", true);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return props;
	}
	
	
}
