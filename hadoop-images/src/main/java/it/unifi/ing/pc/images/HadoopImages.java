package it.unifi.ing.pc.images;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import it.unifi.ing.pc.images.elephantdb.DatabaseOperation;
import it.unifi.ing.pc.images.elephantdb.EndpointFactory;
import it.unifi.ing.pc.images.mapreduce.Map;
import it.unifi.ing.pc.images.mapreduce.Reduce;

public class HadoopImages {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
	    Job job = Job.getInstance(conf, "image search");
	    
	    job.setJarByClass(HadoopImages.class);
	    
	    job.setMapperClass(Map.class);
	    job.setCombinerClass(Reduce.class);
	    job.setReducerClass(Reduce.class);
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    FileInputFormat.addInputPath(job, new Path( "/input/rawdata") );
	    FileOutputFormat.setOutputPath(job, new Path("/output/" ));
	    
	    DatabaseOperation.writeToDb( EndpointFactory.getHfs() );
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
