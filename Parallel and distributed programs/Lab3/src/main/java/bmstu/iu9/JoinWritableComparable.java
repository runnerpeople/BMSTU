package bmstu.iu9;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by runnerpeople on 22.10.16.
 */
public class JoinWritableComparable implements WritableComparable {

    private int airportID;
    private int flagTable;

    public JoinWritableComparable() {}

    public JoinWritableComparable(int airportID, int flagTable) {
        this.airportID = airportID;
        this.flagTable = flagTable;
    }

    /* Setter */

    public void setAirportID(int airportID) {
        this.airportID = airportID;
    }

    public void setFlagTable(int flagTable) {
        this.flagTable = flagTable;
    }

    /* Getter */

    public int getAirportID() {
        return airportID;
    }

    public int getFlagTable() {
        return flagTable;
    }


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(airportID);
        dataOutput.writeInt(flagTable);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        airportID = dataInput.readInt();
        flagTable = dataInput.readInt();
    }

    @Override
    public int compareTo(Object o) {
        JoinWritableComparable that = (JoinWritableComparable)o;
        if (this.airportID > that.airportID)
            return 1;
        else if (this.airportID < that.airportID)
            return -1;
        if (this.flagTable > that.flagTable)
            return 1;
        else if (this.flagTable < that.flagTable)
            return -1;
        return 0;
    }

    public int compareToAirportID(Object o) {
        JoinWritableComparable that = (JoinWritableComparable) o;
        if (this.airportID > that.airportID)
            return 1;
        else if (this.airportID < that.airportID)
            return -1;
        return 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinWritableComparable that = (JoinWritableComparable) o;

        if (airportID != that.airportID) return false;
        return flagTable == that.flagTable;

    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();

        /* Generate by Intellij IDEA */
//        int result = airportID;
//        result = 31 * result + flagTable;
//        return result;
    }

    @Override
    public String toString() {
        return "JoinWritableComparable{" +
                "airportID=" + airportID +
                ", flagTable=" + flagTable +
                '}';
    }
}
