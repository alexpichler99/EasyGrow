package model;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
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


    public Plant(float moistureOptimum, float humidityOptimum, float temperatureOptimum, String ip, String name) {
        this.ip = ip;
        this.name = name;
        moistureHistory=new MeasurementHistory(100,0,moistureOptimum);
        humidityHistory=new MeasurementHistory(100,0,humidityOptimum);
        temperatureHistory=new MeasurementHistory(PlantModel.maxTemperature,PlantModel.minTemperature,temperatureOptimum);
    }

    public void refreshInformation() {
        try {
            float moist = Float.NaN, temp = Float.NaN, hum = Float.NaN;
            boolean mode = false; //used for testing
            if(mode) {
                String sentence;
                Socket clientSocket;
                clientSocket = new Socket(ip, 80);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes("GET / HTTP/1.1");
                sentence = inFromServer.readLine();
                String split[] = sentence.split(";");
                if (split.length != 3)
                    return;
                try {
                    moist = Float.parseFloat(split[0]);
                } catch (Exception ex) { }
                try {
                    temp = Float.parseFloat(split[1]);
                } catch (Exception ex) { }
                try {
                    hum = Float.parseFloat(split[2]);
                } catch (Exception ex) { }
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
            humidityHistory.addMeasurement(new Measurement(-1));
        }
        catch (Exception e) { }
    }
}
