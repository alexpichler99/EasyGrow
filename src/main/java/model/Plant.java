package model;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;
import java.util.List;
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


    public Plant(float moistureOptimum, float humidityOptimum,float temperatureOptimum, String ip, String name) {
        this.ip = ip;
        this.name = name;
        moistureHistory=new MeasurementHistory(100,0,moistureOptimum);
        humidityHistory=new MeasurementHistory(100,0,humidityOptimum);
        temperatureHistory=new MeasurementHistory(PlantModel.maxTemperature,PlantModel.minTemperature,temperatureOptimum);
    }

    /***
     * Fix parseFloat, try - catch for every parseFloat_MAYRHOFERSUXCOX_(EASTEREGG)
     */
    public void refreshInformation() {
        try {
            /*
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
            float moisture = -1, temperature = -1, humidity = -1;
            try {
                moisture = Float.parseFloat(split[0]);
                temperature = Float.parseFloat(split[1]);
                humidity = Float.parseFloat(split[2]);
            }
            catch (Exception ex) {

            }
            */
            //moistureHistory.addMeasurement(new Measurement(moisture));
            //temperatureHistory.addMeasurement(new Measurement(temperature));
            //humidityHistory.addMeasurement(new Measurement(humidity));
            //clientSocket.close();
            Random random = new Random();
            int i = random.nextInt();
            i = (i%60);
            if(i<0)
                i*=-1;
            i-=10;
            moistureHistory.addMeasurement(new Measurement(i));
            temperatureHistory.addMeasurement(new Measurement(-10));
            humidityHistory.addMeasurement(new Measurement(i));
        }
        catch (Exception e) {
            System.out.println("fatal_error");
        }
    }
}
