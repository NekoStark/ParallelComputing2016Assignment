package it.unifi.ing.pc.images.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		
		StringBuffer result = new StringBuffer();
		boolean first= true;
		for (Text val : values) {
			if(!first) {
				result.append("\t");
			}
			result.append(val.toString());
			first = false;
		}
		
		context.write(key, new Text(result.toString()));
	}

}
