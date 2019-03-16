package iu9.bmstu.ru.lab7.model;

import java.util.Date;

public class Coin {

    private static String currency;

    private Date time;

    private double close;
    private double open;
    private double low;
    private double high;

    public Coin() {}

    public Coin(Date time, double close, double open, double low, double high) {
        this.time = time;
        this.close = close;
        this.open = open;
        this.low = low;
        this.high = high;
    }

    public static String getCurrency() {
        return currency;
    }

    public static void setCurrency(String currency) {
        Coin.currency = currency;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "time=" + time +
                ", close=" + close +
                ", open=" + open +
                ", low=" + low +
                ", high=" + high +
                '}';
    }
}
