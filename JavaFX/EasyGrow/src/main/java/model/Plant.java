package model;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;
import java.util.Random;

/**
 * Created by Alex on 03/02/2016.
 */
public class Plant {
    private String ip;
    private String name;

    private MeasurementHistory moistureHistory;
    private MeasurementHistory temperatureHistory;
    private MeasurementHistory humidityHistory;

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


    public MeasurementHistory getTemperatureHistory()
    {
        return temperatureHistory;
    }
    public MeasurementHistory getHumidityHistory()
    {
        return humidityHistory;
    }
    public MeasurementHistory getMoistureHistory()
    {
        return moistureHistory;
    }


    //add tolerance to constructor
    public Plant(float moistureOptimum, float humidityOptimum, float temperatureOptimum, String ip, String name) {
        this.ip = ip;
        this.name = name;
        moistureHistory = new MeasurementHistory(100, 0, moistureOptimum, 10);
        humidityHistory = new MeasurementHistory(100, 0, humidityOptimum, 10);
        temperatureHistory=new MeasurementHistory(PlantModel.maxTemperature, PlantModel.minTemperature, temperatureOptimum, 5);
    }


    public boolean refreshInformation() {
        Date date = new Date();
        try {
            float moist = Float.NaN, temp = Float.NaN, hum = Float.NaN;
            boolean mode = true; //used for testing
            if(mode) {
                String sentence;
                Socket clientSocket;
                clientSocket = new Socket();
                clientSocket.connect(new InetSocketAddress(ip, 80), 1000);
                if (!clientSocket.isConnected()) {
                    clientSocket.close();
                    return false;
                }
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes("GET / HTTP/1.1");
                sentence = inFromServer.readLine();
                String split[] = sentence.split(";");
                if (split.length != 3)
                    return false;
                try {
                    moist = Float.parseFloat(split[0]);
                } catch (NumberFormatException ex) { }
                try {
                    temp = Float.parseFloat(split[1]);
                } catch (NumberFormatException ex) { }
                try {
                    hum = Float.parseFloat(split[2]);
                } catch (NumberFormatException ex) { }
            }
            else {
                Random random = new Random();
                int i = random.nextInt();
                i = (i % 60);
                if (i < 0)
                    i *= -1;
                moist=i;
                temp=i;
                hum=i;
            }
            moistureHistory.addMeasurement(new Measurement(moist));
            temperatureHistory.addMeasurement(new Measurement(temp));
            humidityHistory.addMeasurement(new Measurement(hum));
        }
        catch (Exception e) {
            return false;
        }
        System.out.println("time: " + (new Date().getTime() - date.getTime()));
        return true;
    }
}
