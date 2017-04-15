package bmstu.iu9;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by runnerpeople on 15.10.16.
 */
public class AirportMapper extends Mapper<LongWritable,Text,JoinWritableComparable,Text> {

    private static final int AIRPORT_ID = 0;
    private static final int AIRPORT_NAME = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split("\",");
        if (key.get()>0) {
            Text airportName = new Text(columns[AIRPORT_NAME].replaceAll("\"",""));
            int airportID = Integer.parseInt(columns[AIRPORT_ID].replaceAll("\"",""));
            JoinWritableComparable newKey = new JoinWritableComparable(airportID,0);
            context.write(newKey,airportName);
        }
    }
}
