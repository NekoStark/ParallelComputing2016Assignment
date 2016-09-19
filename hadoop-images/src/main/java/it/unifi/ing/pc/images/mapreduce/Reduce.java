package it.unifi.ing.pc.images.mapreduce;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Reduce extends Reducer<Text, Text, Text, Text> {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		
		StringBuffer result = new StringBuffer();
		
		boolean first= true;
		for (Text val : values) {
			if(!first) {
				result.append(",");
			}
			result.append(val.toString());
			first = false;
		}
		
		try{
			context.write(new Text(ZonedDateTime.now().format(formatter) + "  "+ key.toString() +"  " + result.toString() + "\""+key.toString()+"\""), null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
