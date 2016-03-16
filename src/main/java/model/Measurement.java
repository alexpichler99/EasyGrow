package model;

import java.util.Date;

/**
 * Created by alexa on 11.03.2016.
 */
public class Measurement {
    private Date date;
    private double value;
    public Measurement(double value,Date date)
    {
        this.date=date;
        this.value = value;
    }
    public Measurement(float value)
    {
        this.value=value;
        this.date=new Date();
    }
    public double getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
