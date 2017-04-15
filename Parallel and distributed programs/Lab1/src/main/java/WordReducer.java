//package main.java;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by runnerpeople on 17.09.16.
 */
public class WordReducer extends Reducer<Text,IntWritable,Text,LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        context.write(key,new LongWritable(count));
    }
}
