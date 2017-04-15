package bmstu.iu9;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by runnerpeople on 24.09.16.
 */
public class SortApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: bmstu.iu9.SortApp <input path> <output path>");
            System.exit(-1);
        }
        Job job = Job.getInstance();
        job.setJarByClass(SortApp.class);
        job.setJobName("Full sort");
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(FlightMapper.class);
        job.setReducerClass(FlightReducer.class);
        job.setPartitionerClass(FlightPartitioner.class);
        job.setGroupingComparatorClass(FlightComparator.class);

        job.setMapOutputKeyClass(FlightWritableComparable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(String.class);
        job.setOutputValueClass(Text.class);

        job.setNumReduceTasks(2);
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
