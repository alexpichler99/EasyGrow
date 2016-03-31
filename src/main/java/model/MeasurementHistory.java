package model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alexa on 11.03.2016.
 */
public class MeasurementHistory {
    private float maximum;
    private float minumum;
    private float optimum;
    List<Measurement> measurementList;
    public MeasurementHistory(float maximum, float minimum, float optimum) {
        this.maximum = maximum;
        this.minumum = minimum;
        this.optimum = optimum;
        this.measurementList = new LinkedList<>();
    }
    public MeasurementHistory(LinkedList<Measurement> linkedList, float maximum, float minumum, float optimum) {
        this.measurementList = linkedList;
        this.maximum = maximum;
        this.minumum = minumum;
        this.optimum = optimum;
    }
    public void addMeasurement(Measurement measurement) {
        if(measurement.getValue() <= maximum && measurement.getValue() >= minumum) {
            measurementList.add(measurement);
            while (new Date().getTime() - measurementList.get(0).getDate().getTime() > PlantModel.spanTime && measurementList.size() != 1) {
                if (new Date().getTime() - measurementList.get(1).getDate().getTime() > PlantModel.spanTime)
                    measurementList.remove(0);
                else
                    break;
            }
        }
        else
            System.out.println("wrong value");
    }
    public List<Measurement> getMeasurements() {
        return measurementList;
    }
    public Measurement getLastMeasurement() {
        if(measurementList.size() != 0)
            return measurementList.get(0);
        return null;
    }
    public Measurement getFirstMeasurement() {
        if(measurementList.size() != 0)
            return measurementList.get(measurementList.size() - 1);
        return null;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinumum() {
        return minumum;
    }

    public float getOptimum() {
        return optimum;
    }

    public void setOptimum(float optimum) {
        this.optimum = optimum;
    }
}
