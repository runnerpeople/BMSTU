//package main.java;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by runnerpeople on 17.09.16.
 */
public class WordMapper extends Mapper<LongWritable, Text,Text,IntWritable> {
    @Override
    protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] words = str.split("[\\p{Punct}\\p{Space}—]");
        for (String word : words) {
            word = word.toLowerCase();
            context.write(new Text(word),new IntWritable(1));
        }

    }

}
