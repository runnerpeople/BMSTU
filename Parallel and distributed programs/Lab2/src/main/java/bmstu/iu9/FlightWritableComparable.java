package bmstu.iu9;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by runnerpeople on 08.10.16.
 */
public class FlightWritableComparable implements WritableComparable {

    private boolean isCancelled;
    private float delayTime;
    private float airTime;
    private int idAirport;

    public FlightWritableComparable() {}

    public FlightWritableComparable(boolean isCancelled, float delayTime, float airTime, int idAirport) {
        this.isCancelled = isCancelled;
        this.delayTime = delayTime;
        this.airTime = airTime;
        this.idAirport = idAirport;
    }

    /* Setter */

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public void setAirTime(float airTime) {
        this.airTime = airTime;
    }

    public void setIdAirport(int idAirport) {
        this.idAirport = idAirport;
    }

    /* Getter */

    public boolean isCancelled() {
        return isCancelled;
    }

    public float getDelayTime() {
        return delayTime;
    }

    public float getAirTime() {
        return airTime;
    }

    public int getIdAirport() {
        return idAirport;
    }


    @Override
    public int compareTo(Object o) {
        FlightWritableComparable other = (FlightWritableComparable)o;
        if (this.isCancelled && !other.isCancelled)
            return 1;
        else if (!this.isCancelled && other.isCancelled)
            return -1;
        if (this.delayTime > other.delayTime)
            return 1;
        else if (this.delayTime < other.delayTime)
            return -1;
        if (this.idAirport > other.idAirport)
            return 1;
        else if (this.idAirport < other.idAirport)
            return -1;
        if (this.airTime > other.airTime)
            return 1;
        else if (this.airTime < other.airTime)
            return -1;
        return 0;
    }

    public int compareToDelayTime(Object o) {
        FlightWritableComparable other = (FlightWritableComparable)o;
        if (this.delayTime > other.delayTime)
            return 1;
        else if (this.delayTime < other.delayTime)
            return -1;
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(isCancelled);
        dataOutput.writeFloat(delayTime);
        dataOutput.writeFloat(airTime);
        dataOutput.writeInt(idAirport);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        isCancelled = dataInput.readBoolean();
        delayTime = dataInput.readFloat();
        airTime = dataInput.readFloat();
        idAirport = dataInput.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightWritableComparable that = (FlightWritableComparable) o;

        if (isCancelled != that.isCancelled) return false;
        if (Float.compare(that.delayTime, delayTime) != 0) return false;
        if (Float.compare(that.airTime, airTime) != 0) return false;
        return idAirport == that.idAirport;

    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();

        /* Generate by Intellij IDEA */
//        int result;
//        int result = (isCancelled ? 1 : 0);
//        result = 31 * result + (delayTime != +0.0f ? Float.floatToIntBits(delayTime) : 0);
//        result = 31 * result + (airTime != +0.0f ? Float.floatToIntBits(airTime) : 0);
//        result = 31 * result + idAirport;
//        return result;
    }

    @Override
    public String toString() {
        return "bmstu.iu9.FlightWritableComparable{" +
                "isCancelled=" + isCancelled +
                ", delayTime=" + delayTime +
                ", airTime=" + airTime +
                ", idAirport=" + idAirport +
                '}';
    }
}
