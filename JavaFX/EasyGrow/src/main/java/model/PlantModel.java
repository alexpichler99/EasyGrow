package model;

import java.util.*;

/**
 * Created by Alex on 03/02/2016.
 */
public class PlantModel extends Observable {
    //constants
    public static int refreshTime = 1000; //ms

    public static final float maxMoisture = 100;
    public static final float minMoisture = 0;
    public static final float maxHumidity = 100;
    public static final float minHumidity = 0;
    public static final float minTemperature = -15;
    public static final float maxTemperature = 50;

    private Plant plant;

    boolean b = true;

    public PlantModel(float moistureOptimum, float humidityOptimum, float temperatureOptimum, String plantName, String ip, Observer obs) {
        addObserver(obs);
        plant = new Plant(ip, plantName, moistureOptimum, humidityOptimum, temperatureOptimum);
        Timer timer = new Timer(true);
        Date old = new Date();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RefreshType type = RefreshType.CURRENT;
                if (b)
                    type = RefreshType.HISTORY;
                if (!b)
                    type = RefreshType.CURRENT;
                b = !b;
                if (plant.refreshInformation(type)) {
                    setChanged();
                    notifyObservers(type);
                }
            }
        }, 100, refreshTime);
    }

    public Plant getPlant() {
        return plant;
    }
}
