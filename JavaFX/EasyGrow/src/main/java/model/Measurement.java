package model;

/**
 * Created by alex on 12.06.16.
 */
public class Measurement {
    private long time;
    private float moisture;
    private float humidity;
    private float temperature;

    public void setTime (long date) {
        this.time = date;
    }

    public void setMoisture(float moisture) {
        this.moisture = moisture;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public long getTime() {

        return time;
    }

    public float getMoisture() {
        return moisture;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public Measurement(long time, float moisture, float temperature, float humidity) {

        this.time = time;
        this.moisture = moisture;
        this.humidity = humidity;
        this.temperature = temperature;
    }
}
