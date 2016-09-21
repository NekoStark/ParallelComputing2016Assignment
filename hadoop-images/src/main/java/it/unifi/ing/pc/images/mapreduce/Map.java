package it.unifi.ing.pc.images.mapreduce;

import java.io.IOException;
import java.time.Instant;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<Object, Text, Text, ImageWritable> {

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split("\t");
		ImageWritable iw = new ImageWritable();
		iw.setImageUrl(new Text(tokens[0]));
		iw.setTimeStamp(new LongWritable( Instant.parse(tokens[3]).toEpochMilli() ));
		context.write(new Text(tokens[1]), iw);
	}
}
