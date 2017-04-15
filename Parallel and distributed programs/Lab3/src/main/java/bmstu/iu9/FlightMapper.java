package bmstu.iu9;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by runnerpeople on 15.10.16.
 */
public class FlightMapper extends Mapper<LongWritable,Text,JoinWritableComparable,Text> {
    private static final int AIRPORT_ID = 14;
    private static final int DELAY_TIME = 18;

    private static final float F_ZERO = 0.0f;

    private static final String NULL_STRING = "";

    private static float getFloat(String column) {
        if (!column.equals(NULL_STRING))
            return Float.parseFloat(column);
        else
            return F_ZERO;
    }


    @Override
    protected void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get()>0) {
            float delayTime=getFloat(columns[DELAY_TIME]);
            /* delayed */
            if (delayTime > F_ZERO) {
                int airportID = Integer.parseInt(columns[AIRPORT_ID]);
                JoinWritableComparable newKey = new JoinWritableComparable(airportID,1);
                context.write(newKey,new Text(columns[DELAY_TIME]));
            }
        }
    }
}
