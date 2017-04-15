package bmstu.iu9;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by runnerpeople on 15.10.16.
 */

public class JoinReducer extends Reducer<JoinWritableComparable,Text,Text,Text> {

    private static final float F_ZERO = 0.0f;

    @Override
    protected void reduce(JoinWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iter = values.iterator();
        Text airportName = new Text("Airport name: " + iter.next().toString());
        if (iter.hasNext()) {
            int i = 0;
            float current;
            float min = F_ZERO;
            float max = F_ZERO;
            float sum = F_ZERO;
            while (iter.hasNext()) {
                current = Float.parseFloat(iter.next().toString());
                if (current < min || i == 0)
                    min = current;
                else if (current > max)
                    max = current;
                sum += current;
                i++;
            }
            sum /= i;
            Text statistics = new Text("Stats|Min: " + Float.toString(min) + " , Average: " + Float.toString(sum) + " , Max: " + Float.toString(max) + " |");
            context.write(airportName,statistics);
        }
    }
}
