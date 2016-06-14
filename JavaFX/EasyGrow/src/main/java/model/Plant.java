package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alex on 03/02/2016.
 */
public class Plant {
    private String ip;
    private String name;
    private List<Measurement> measurements = new LinkedList<>();
    private float moistureOptimum;
    private float warningTolerance = 10; //%


    public WarningType getMoistureWarning() {
        return getWarning(PlantModel.minMoisture, PlantModel.maxMoisture, currentMoisture, moistureOptimum, warningTolerance);
    }

    private WarningType getWarning(float min, float max, float val, float opt, float tol) {
        if (val <= max && val >= min && !Float.isNaN(val)) {
            if (val <= opt + tol && val >= opt - tol)
                return WarningType.Optimum;
            else if (val <= opt + tol * 2 && val >= opt - tol * 2)
                return WarningType.Normal;
            else
                return WarningType.Critical;
        }
        return WarningType.Unknown;
    }

    public WarningType getTemperatureWarning() {
        return getWarning(PlantModel.minTemperature, PlantModel.maxTemperature, currentTemperature, temperatureOptimum, warningTolerance);
    }

    public WarningType getHumidityWarning() {
        return getWarning(PlantModel.minHumidity, PlantModel.maxHumidity, currentHumidity, humidityOptimum, warningTolerance);
    }

    public float getMoistureOptimum() {
        return moistureOptimum;
    }

    public void setMoistureOptimum(float moistureOptimum) {
        this.moistureOptimum = moistureOptimum;
    }

    public float getHumidityOptimum() {
        return humidityOptimum;
    }

    public void setHumidityOptimum(float humidityOptimum) {
        this.humidityOptimum = humidityOptimum;
    }

    public float getTemperatureOptimum() {
        return temperatureOptimum;
    }

    public void setTemperatureOptimum(float temperatureOptimum) {
        this.temperatureOptimum = temperatureOptimum;
    }

    private float humidityOptimum;
    private float temperatureOptimum;

    private float currentMoisture;
    private float currentTemperature;

    public float getCurrentHumidity() {
        return currentHumidity;
    }

    private float currentHumidity;

    public float getCurrentMoisture() {
        return currentMoisture;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    List<XYChart.Data<Long, Double>> l1 = new LinkedList< >();

    public Plant(String ip, String name) {
        this.name = name;
        this.ip = ip;
    }

    public Plant(String ip, String name, float moistureOptimum, float humidityOptimum, float temperatureOptimum) {
        this.ip = ip;
        this.name = name;
        this.moistureOptimum = moistureOptimum;
        this.humidityOptimum = humidityOptimum;
        this.temperatureOptimum = temperatureOptimum;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public List<XYChart.Data<Long, Double>> getMoistures() {
        List<XYChart.Data<Long, Double>> l = new LinkedList<>();
        for (Measurement m : measurements) {
            l.add(new XYChart.Data<Long, Double>(m.getTime(), (double) m.getMoisture()));
        }
        return l;
    }

    public List<XYChart.Data<Long, Double>> getTemperatures() {
        List<XYChart.Data<Long, Double>> l = new LinkedList<>();
        for (Measurement m : measurements) {
            l.add(new XYChart.Data<Long, Double>(m.getTime(), (double) m.getTemperature()));
        }
        return l;
    }

    public List<XYChart.Data<Long, Double>> getHumidities() {
        List<XYChart.Data<Long, Double>> l = new LinkedList<>();
        for (Measurement m : measurements) {
            l.add(new XYChart.Data<Long, Double>(m.getTime(), (double) m.getHumidity()));
        }
        return l;
    }

    //region refreshing
    public boolean refreshHistory() {
        float moist = Float.NaN, temp = Float.NaN, hum = Float.NaN;
        long time = -1;
        Socket clientSocket;
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress("192.168.1.2", 80), 1000);
        } catch (IOException e1) {
            return false;
        }
        if (!clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
            return false;
        }
        DataOutputStream outToServer = null;
        BufferedReader inFromServer = null;
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        try {
            outToServer.writeBytes("get1\n");
        } catch (IOException e1) {
            return false;
        }
        measurements.clear();
        while (true) {
            String sentence = null;
            boolean formatError = false;
            try {
                sentence = inFromServer.readLine();
            } catch (IOException e1) {
                return false;
            }
            if (sentence == null)
                break;
            String split[] = sentence.split(";");
            if (split.length != 4)
                continue;
            try {
                time = Long.parseLong(split[0]);
            } catch (NumberFormatException ex) {
                formatError = true;
            }
            try {
                moist = Float.parseFloat(split[1]);
            } catch (NumberFormatException ex) {
                formatError = true;
            }
            try {
                temp = Float.parseFloat(split[2]);
            } catch (NumberFormatException ex) {
                formatError = true;
            }
            try {
                hum = Float.parseFloat(split[3]);
            } catch (NumberFormatException ex) {
                formatError = true;
            }
            if (!formatError)
                measurements.add(new Measurement(time, moist, temp, hum));
        }
        try {
            inFromServer.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e1) {
        }
        return true;
    }

    public boolean refreshCurrentValues() {
        float moist = Float.NaN, temp = Float.NaN, hum = Float.NaN;
        Socket clientSocket;
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress("192.168.1.2", 80), 1000);
        } catch (IOException e1) {
            return false;
        }
        if (!clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
            return false;
        }
        DataOutputStream outToServer = null;
        BufferedReader inFromServer = null;
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        try {
            outToServer.writeBytes("get2\n");
        } catch (IOException e1) {
            return false;
        }
        String sentence = null;
        try {
            sentence = inFromServer.readLine();
        } catch (IOException e1) {
            return false;
        }
        if (sentence == null)
            return false;

        String split[] = sentence.split(";");
        if (split.length != 3)
            return false;
        try {
            moist = Float.parseFloat(split[0]);
        } catch (NumberFormatException ex) { }
        try { temp = Float.parseFloat(split[1]);
        } catch (NumberFormatException ex) { }
        try {
            hum = Float.parseFloat(split[2]);
        } catch (NumberFormatException ex) { }

        currentMoisture = moist;
        currentHumidity = hum;
        currentTemperature = temp;

        try {
            inFromServer.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e1) {
        }
        return true;
    }


    public boolean refreshInformation(RefreshType type) {
        Date date = new Date();
        /*for (int i = l1.size() - 1; i >= 0; i--)
        {
            l1.get(i).setXValue(l1.get(i).getXValue() - 1);
            if(l1.get(i).getXValue() < 0)
                l1.remove(i);
        }
        //l1.remove(0);
        l1.add(new XYChart.Data<Long, Double>((long)24, (double)100));*/
        if (type == RefreshType.HISTORY) {
            System.out.println("time: " + (new Date().getTime() - date.getTime()));
            return refreshHistory();
        }
        if (type == RefreshType.CURRENT) {
            System.out.println("time: " + (new Date().getTime() - date.getTime()));
            return refreshCurrentValues();
        }
        return true;
    }
    //endregion
}
