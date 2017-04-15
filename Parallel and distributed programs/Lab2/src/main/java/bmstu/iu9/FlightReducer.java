package bmstu.iu9;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by runnerpeople on 08.10.16.
 */
public class FlightReducer extends Reducer<FlightWritableComparable,Text,String,Text> {
    @Override
    protected void reduce(FlightWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text text: values) {
            context.write("Key(flight): ["  + key.toString() + "]",new Text("Value(full_info): [" + text.toString() + "]"));
        }
    }
}
