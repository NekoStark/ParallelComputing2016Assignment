package it.unifi.ing.pc.image.publisher.message.producer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class RandomMessageProducerTsv extends RandomMessageProducer {

	public RandomMessageProducerTsv(String[] services, String[] tags) {
		super(services, tags);
	}

	@Override
	public String buildRandomMessage() {
		StringBuffer sb = new StringBuffer();
		return sb.append("{\"url\":\"http://")
			.append(UUID.randomUUID().toString())
			.append(".com\"")
			.append(",\"tag\": \"")
			.append( random(tags) )
			.append("\",\"service\":\"")
			.append( random(services) )
			.append("\",\"timestamp\" : \"")
			.append(LocalDateTime.now( ZoneId.of("UTC") ))
			.append("\"}")
			.toString();
	}

}
