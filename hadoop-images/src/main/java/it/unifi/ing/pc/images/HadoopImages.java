package it.unifi.ing.pc.images;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import it.unifi.ing.pc.images.mapreduce.ImageWritable;
import it.unifi.ing.pc.images.mapreduce.Map;
import it.unifi.ing.pc.images.mapreduce.Reduce;

public class HadoopImages {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
	    Job job = Job.getInstance(conf, "image batch");
	    
	    job.setJarByClass(HadoopImages.class);
	    
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(ImageWritable.class);
	    
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	    
	    if(args.length > 0 && "demo".equals( args[0] )) {
	    	FileInputFormat.addInputPath(job, new Path( "/Users/stark/Desktop/hadoop/input/rawdata") );
	    	FileOutputFormat.setOutputPath(job, new Path("/Users/stark/Desktop/hadoop/output" ));
	    } else {
	    	FileInputFormat.addInputPath(job, new Path( "/input/rawdata") );
	    	FileOutputFormat.setOutputPath(job, new Path("/output" ));
	    }
	    job.waitForCompletion(true);
	    
	}

}
