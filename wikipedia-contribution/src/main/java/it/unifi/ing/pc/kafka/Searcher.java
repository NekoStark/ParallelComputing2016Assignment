package it.unifi.ing.pc.kafka;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Set;

import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import it.unifi.ing.pc.searcher.BingImageSearcher;
import it.unifi.ing.pc.searcher.FlickrSearcher;
import it.unifi.ing.pc.searcher.GoogleImageSearcher;
import it.unifi.ing.pc.searcher.ImageFreshness;
import it.unifi.ing.pc.searcher.Result;
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;

public class Searcher {

	public static void main(String[] args) {
		
		// args[0]: key search
		Set<Result> googleImages = new GoogleImageSearcher(ImageFreshness.DAY).search(args[0], 30);
		Set<Result> bingImages = new BingImageSearcher(ImageFreshness.DAY).search(args[0], 3);
		Set<Result> flickrImages = new FlickrSearcher(LocalDateTime.now()).search(args[0], 3);
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:4242");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			
		ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
		AdminUtils.createTopic(zkClient, "my-topic", 10, 1, new Properties());
		
		Producer<String, Set<Result>> producer = new KafkaProducer<>(props);
		producer.send(new ProducerRecord<String,Set<Result>>("my-topic", args[0], googleImages));
		producer.send(new ProducerRecord<String,Set<Result>>("my-topic", args[0], bingImages));	
		producer.send(new ProducerRecord<String,Set<Result>>("my-topic", args[0], flickrImages));	
		
		producer.close();
	}
}
	