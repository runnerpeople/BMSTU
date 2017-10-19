package iu9.bmstu.ru.lab7.model;

import java.util.Date;
import java.util.Locale;
import java.util.Currency;

public class Coin {

    private static Currency value_symbol;
    private static Locale locale;

    private Date date;
    private Double value;

    public Coin() {}

    public Coin(Date date, double value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static Currency getValue_symbol() {
        return value_symbol;
    }

    public static void setValue_symbol(Currency value_symbol) {
        Coin.value_symbol = value_symbol;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        Coin.locale = locale;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "date=" + date +
                ", value=" + value +
                '}';
    }
}
