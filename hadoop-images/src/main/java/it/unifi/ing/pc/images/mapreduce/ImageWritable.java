package it.unifi.ing.pc.images.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class ImageWritable implements Writable {

	private Text imageUrl;
	private LongWritable timeStamp;
	
	@Override
	public void write(DataOutput out) throws IOException {
		imageUrl.write(out);
		timeStamp.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		imageUrl.readFields(in);
		timeStamp.readFields(in);
	}
	
	public Text getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(Text imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public LongWritable getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LongWritable timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public int hashCode() {
		return imageUrl.hashCode();
	}

}
