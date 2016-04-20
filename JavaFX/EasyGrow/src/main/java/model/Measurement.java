package model;

import java.util.Date;

/**
 * Created by alexa on 11.03.2016.
 */
public class Measurement {
    private Date date;
    private float value;

    public boolean isValid() {
        return valid;
    }

    private boolean valid;

    public Measurement(float value, Date date)  {
        this.date = date;
        this.value = value;
        this.valid = true;
    }
    public Measurement(float value) {
        this.value = value;
        this.date = new Date();
        this.valid = true;
    }

    public Measurement(float value, Date date, boolean valid) {
        this.value = value;
        this.date = date;
        this.valid = valid;
    }
    public float getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
