package ru.bmstu.iu9;

import java.io.Serializable;

public class FlightSerializable implements Serializable {

    private static final long serialVersionUID = -6714985524961312675L;

    private int originAirportID;
    private int destAirportID;
    private float delayTime;
    private float cancelled;

    public FlightSerializable() {}

    public FlightSerializable(int originAirportID,int destAirportID,float delayTime,float cancelled) {
        this.originAirportID = originAirportID;
        this.destAirportID = destAirportID;
        this.delayTime = delayTime;
        this.cancelled = cancelled;
    }

    public int getOriginAirportID() {
        return originAirportID;
    }

    public int getDestAirportID() {
        return destAirportID;
    }

    public float getDelayTime() {
        return delayTime;
    }

    public float getCancelled() {
        return cancelled;
    }

    public void setOriginAirportID(int originAirportID) {
        this.originAirportID = originAirportID;
    }

    public void setDestAirportID(int destAirportID) {
        this.destAirportID = destAirportID;
    }

    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public void setCancelled(float cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String toString() {
        return "FlightSerializable{" +
                "originAirportID=" + originAirportID +
                ", destAirportID=" + destAirportID +
                ", delayTime=" + delayTime +
                ", cancelled=" + cancelled +
                '}';
    }
}
