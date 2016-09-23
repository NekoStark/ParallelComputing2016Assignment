package it.unifi.ing.pc.images.mapreduce;

import java.io.IOException;
import java.time.Instant;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Reduce extends Reducer<Text, ImageWritable, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<ImageWritable> values, Context context)
			throws IOException, InterruptedException {
		
		StringBuffer result = new StringBuffer();
		boolean first= true;
		LongWritable date = new LongWritable(0l);
		
		for (ImageWritable val : values) {
			if(!first) {
				result.append(",");
			}
			date = max(date, val.getTimeStamp());
			result.append(val.getImageUrl().toString());
			first = false;
		}
		
		try{
			String dateText = Instant.ofEpochMilli(date.get()).toString();
			context.write(new Text(dateText + "  "+ key.toString() +"  " + result.toString()), null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private LongWritable max(LongWritable d1, LongWritable d2) {
		return (d1.compareTo(d2) > 0)? d1 : d2;
	}

}
