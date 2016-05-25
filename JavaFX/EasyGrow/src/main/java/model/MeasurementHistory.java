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
    private float tolerance;
    List<Measurement> measurementList;

    public MeasurementHistory(float maximum, float minimum, float optimum, float tolerance) {
        this.maximum = maximum;
        this.minumum = minimum;
        this.optimum = optimum;
        this.tolerance = tolerance;
        this.measurementList = new LinkedList<>();
    }

    public MeasurementHistory(LinkedList<Measurement> linkedList, float maximum, float minumum, float optimum) {
        this.measurementList = linkedList;
        this.maximum = maximum;
        this.minumum = minumum;
        this.optimum = optimum;
    }

    public void addMeasurement(Measurement measurement) {
        if (measurement.getValue() <= maximum && measurement.getValue() >= minumum)
            measurementList.add(measurement);
        else
            measurementList.add(new Measurement(minumum - 1, measurement.getDate(), false));
        while (new Date().getTime() - measurementList.get(0).getDate().getTime() > PlantModel.spanTime && measurementList.size() != 1) {
            if (new Date().getTime() - measurementList.get(1).getDate().getTime() > PlantModel.spanTime)
                measurementList.remove(0);
            else
                break;
        }
    }

    public List<Measurement> getMeasurements() {
        return measurementList;
    }

    public Measurement getLastMeasurement() {
        if (measurementList.size() != 0)
            return measurementList.get(0);
        return null;
    }

    public Measurement getFirstMeasurement() {
        if (measurementList.size() != 0)
            return measurementList.get(measurementList.size() - 1);
        return null;
    }

    public Measurement getFirstMeasurementInTime(long delayed) {
        if (measurementList.size() > 1) {
            long time = getFirstMeasurement().getDate().getTime() - delayed;
            for (Measurement m : measurementList) {
                if (m.getDate().getTime() > time) {
                    int index = measurementList.indexOf(m) - 1;
                    if (index < 0)
                        return null;
                    return measurementList.get(index);
                }

            }

        }
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

    public WarningType getWarning() {
        Measurement mes = getFirstMeasurement();
        if (mes != null) {
            if (mes.getValue() <= optimum + tolerance && mes.getValue() >= optimum - tolerance)
                return WarningType.Optimum;
            else if (mes.getValue() <= optimum + tolerance * 2 && mes.getValue() >= optimum - tolerance * 2)
                return WarningType.Normal;
            else
                return WarningType.Critical;
        }
        return WarningType.Unknown;
    }
}
