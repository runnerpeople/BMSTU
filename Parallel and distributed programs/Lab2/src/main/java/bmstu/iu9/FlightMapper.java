package bmstu.iu9;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by runnerpeople on 08.10.16.
 */
public class FlightMapper extends Mapper<LongWritable, Text, FlightWritableComparable, Text> {

    /* Constants */

    private static final int AIRPORT_ID = 14;
    private static final int DELAY_TIME = 18;
    private static final int IS_CANCELLED = 19;
    private static final int AIR_TIME = 21;

    private static final float F_ZERO = 0.0f;

    private static final String NULL_STRING = "";
    private static final String S_ONE = "1.00";



    private static float getFloat(String column) {
        if (!column.equals(NULL_STRING))
            return Float.parseFloat(column);
        else
            return F_ZERO;
    }

    @Override
    protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get()>0) {
            int airportID = Integer.parseInt(columns[AIRPORT_ID]);
            float delayTime = getFloat(columns[DELAY_TIME]);
            float airTime = getFloat(columns[AIR_TIME]);
            /* cancelled */
            if (columns[IS_CANCELLED].equals(S_ONE)) {
                context.write(
                        new FlightWritableComparable(true,F_ZERO,F_ZERO,airportID),
                        value
                );
            }
            /* delayed */
            else if (delayTime > F_ZERO) {
                context.write(
                        new FlightWritableComparable(false,delayTime,airTime,airportID),
                        value
                );
            }
        }
    }

}
