package at.htl.easygrow.model;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class PlantModel extends Observable {
    private static PlantModel instance;

    private Plant plant;

    private Timer currentTimer;
    private Timer allTimer;

    private Object lock;

    private PlantModel() {
        plant = new Plant("", 50);

        currentTimer = new Timer(true);
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                plant.refreshCurrentMeasurement();
                setChanged();
                notifyObservers();
            }
        }, 0, 10000); //10sec
        allTimer = new Timer(true);
        allTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                plant.refreshMeasurements();
                setChanged();
                notifyObservers();
            }
        }, 0, 60000); //60sec
    }

    public static PlantModel getInstance() {
        if (instance == null)
            instance = new PlantModel();
        return instance;
    }

    public Plant getPlant() {
        return plant;
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }
}
