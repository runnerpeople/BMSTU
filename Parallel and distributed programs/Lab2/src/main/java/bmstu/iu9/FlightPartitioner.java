package bmstu.iu9;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by runnerpeople on 08.10.16.
 */
public class FlightPartitioner extends Partitioner<FlightWritableComparable,Text> {
    @Override
    public int getPartition(FlightWritableComparable key, Text value, int numReduceTasks) {
        return (Float.hashCode(key.getDelayTime()) & Integer.MAX_VALUE) % numReduceTasks;
    }
}
