package it.unifi.ing.pc.image.publisher.message.producer;

import java.time.Instant;
import java.util.UUID;

public class RandomMessageProducerJson extends RandomMessageProducer {

	public RandomMessageProducerJson(String[] services, String[] tags) {
		super(services, tags);
	}

	@Override
	public String buildRandomMessage() {
		StringBuffer sb = new StringBuffer();
		return sb.append("http://")
			.append(UUID.randomUUID().toString())
			.append(".com")
			.append("\t")
			.append( random(tags) )
			.append("\t")
			.append( random(services) )
			.append("\t")
			.append(Instant.now().toString())
			.toString();
	}

}
