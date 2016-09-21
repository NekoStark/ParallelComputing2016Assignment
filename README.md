Assignment for Parallel Computing class, 2016, UniFi.
Projects in this repository are:

* image-searcher: API for searching images from Google Images, Bing and Flickr.
* image-publisher: Simulates a real time data flow, with data downloaded using image-searcher classes or generated randomly
* hadoop-images: Hadoop map-reduce implementation, generates batch view with image results
* storm-images: Storm implementation, reads from an Apache Kafka topic and published results on Druid or to output files.
