package model;

import java.util.Date;

/**
 * Created by alexa on 11.03.2016.
 */
public class Measurement {
    private Date date;
    private float value;
    public Measurement(float value, Date date)
    {
        this.date = date;
        this.value = value;
    }
    public Measurement(float value)
    {
        this.value = value;
        this.date = new Date();
    }
    public float getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
