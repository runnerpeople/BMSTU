package ru.bmstu.iu9;


import java.io.Serializable;

public class StatisticCount implements Serializable {

    private static final long serialVersionUID = 4582044296179750556L;

    private static final float F_ONE_HUNDRED = 100.0f;

    private int countFlights;
    private int countCancelledFlights;
    private int countDelayFlights;
    private float maxDelay;

    public StatisticCount(){};

    public StatisticCount(int countFlights, int countCancelledFlights, int countDelayFlights, float maxDelay) {
        this.countFlights = countFlights;
        this.countCancelledFlights = countCancelledFlights;
        this.countDelayFlights = countDelayFlights;
        this.maxDelay = maxDelay;
    }

    public int getCountFlights() {
        return countFlights;
    }

    public int getCountCancelledFlights() {
        return countCancelledFlights;
    }

    public int getCountDelayFlights() {
        return countDelayFlights;
    }

    public float getMaxDelay() {
        return maxDelay;
    }

    public void setCountFlights(int countFlights) {
        this.countFlights = countFlights;
    }

    public void setCountCancelledFlights(int countCancelledFlights) {
        this.countCancelledFlights = countCancelledFlights;
    }

    public void setCountDelayFlights(int countDelayFlights) {
        this.countDelayFlights = countDelayFlights;
    }

    public void setMaxDelay(float maxDelay) {
        this.maxDelay = maxDelay;
    }

    public static StatisticCount addValue(StatisticCount a, boolean isCancelled,boolean isDelay,float delayTime) {
        return new StatisticCount(a.getCountFlights()+1,
                                  isCancelled ? a.getCountCancelledFlights()+1 : a.getCountCancelledFlights(),
                                  isDelay ? a.getCountDelayFlights()+1 : a.getCountDelayFlights(),
                                  delayTime > a.getMaxDelay()? delayTime : a.getMaxDelay());
    }

    public static StatisticCount add(StatisticCount a,StatisticCount b) {
        return new StatisticCount(a.getCountFlights() + b.getCountFlights(),
                                  a.getCountCancelledFlights() + b.getCountCancelledFlights(),
                                  a.getCountDelayFlights() + b.getCountDelayFlights(),
                                  a.getMaxDelay() + b.getMaxDelay());
    }

    public static String outputString(StatisticCount a) {
            float delayPercent = (float)a.countDelayFlights / (float)a.countFlights * F_ONE_HUNDRED ;
            float cancelPercent = (float)a.countCancelledFlights / (float)a.countFlights * F_ONE_HUNDRED;
            String output = "Count all flights: " + a.countFlights + ", Statistics: ";
            output += "Cancelled flights[" + a.countCancelledFlights + "/" + a.countFlights + "]: " + cancelPercent + "%; ";
            output += "Delayed flights[" + a.countDelayFlights + "/" + a.countFlights + "]: " + delayPercent + "%; ";
            output += "Max delay time flight: " + a.maxDelay;
            return output;
    }

    @Override
    public String toString() {
        return "StatisticCount{" +
                "countFlights=" + countFlights +
                ", countCancelledFlights=" + countCancelledFlights +
                ", countDelayFlights=" + countDelayFlights +
                ", maxDelay=" + maxDelay +
                '}';
    }
}
