package bmstu.iu9;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by runnerpeople on 15.10.16.
 */
public class JoinPartitioner extends Partitioner<JoinWritableComparable,Text> {
    @Override
    public int getPartition(JoinWritableComparable key, Text value, int numReduceTasks) {
        return key.getAirportID() % numReduceTasks;
    }
}
