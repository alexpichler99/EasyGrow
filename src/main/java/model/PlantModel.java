package model;

import java.util.*;

/**
 * Created by Alex on 03/02/2016.
 */
public class PlantModel extends Observable {
    //constants
    public static int refreshTime = 1000; //ms
    public static long spanTime = 300000; //ms
    public static final float minTemperature = -15;
    public static final float maxTemperature = 50;

    private Plant plant;

    public  PlantModel(Observer obs) {
        addObserver(obs);
        plant = new Plant(95, 95, 30, "192.168.1.5", "");
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                plant.refreshInformation();
                setChanged();
                notifyObservers();
            }
        }, 100, refreshTime);
    }
    public Plant getPlant() {
        return plant;
    }
}
