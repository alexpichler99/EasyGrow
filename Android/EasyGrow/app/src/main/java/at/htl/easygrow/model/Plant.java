package at.htl.easygrow.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class Plant {
    private final int maxMeasurements = 120;

    public static final float maxMoisture = 100;
    public static final float minMoisture = 0;
    public static final float maxHumidity = 100;
    public static final float minHumidity = 0;
    public static final float minTemperature = -15;
    public static final float maxTemperature = 50;
    private float warningTolerance = 10; //%


    private String ip;
    private List<Measurement> measurements = new LinkedList<>();
    private Measurement current;
    private int moistureOptimum;
    private int temperatureOptimum;
    private int humidityOptimum;

    public Plant(String ip, int moistureOptimum) {
        this.ip = ip;
        this.moistureOptimum = moistureOptimum;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //findArduino();
            }
        }).start();
    }


    public void refreshCurrentMeasurement1() {
        if (current == null) {
            current = new Measurement(1, 10, 10, 10);
        }
        if (current.getMoisture() >= 100)
            current.setMoisture(0);
        current = new Measurement(current.getTime() + 1, current.getMoisture() + 1,
                current.getHumidity() + 1, current.getTemperature() + 1);
    }

    public void refreshMeasurements1() {
        if (current != null) {
            measurements.add(current);
        }
        if (measurements.size() >= maxMeasurements)
            measurements.remove(0);
    }

    public synchronized String getIp() {
        return ip;
    }

    public synchronized void setIp(String ip) {
        this.ip = ip;
    }

    public int getMoistureOptimum() {
        return moistureOptimum;
    }

    public void setMoistureOptimum(int moistureOptimum) {
        this.moistureOptimum = moistureOptimum;
    }

    public int getTemperatureOptimum() {
        return temperatureOptimum;
    }

    public void setTemperatureOptimum(int temperatureOptimum) {
        this.temperatureOptimum = temperatureOptimum;
    }

    public int getTemperatureOptimumPerCent() {
        int max = Math.abs((int)(maxTemperature - minTemperature));
        int curr = temperatureOptimum + (int)Math.abs(minTemperature);
        return (int)(((float)curr / (float)max) * 100f);
    }

    public void setTemperatureOptimumPerCent(int perCent) {
        int max = Math.abs((int)(maxTemperature - minTemperature));
        int curr = (int)((float)max * ((float)perCent / (float)100));
        curr -= Math.abs(minTemperature);
        temperatureOptimum = curr;
    }

    public int getHumidityOptimum() {
        return humidityOptimum;
    }

    public void setHumidityOptimum(int humidityOptimum) {
        this.humidityOptimum = humidityOptimum;
    }

    public List<Measurement> getMeasurements() {
        ArrayList<Measurement> m = new ArrayList<Measurement>();
        m.addAll(measurements);
        return m;
    }

    public Measurement getCurrentMeasurement() {
        return current;
    }

    public WarningType getMoistureWarning() {
        return getWarning(minMoisture, maxMoisture,
                getCurrentMeasurement() == null ? Float.NaN :
                        getCurrentMeasurement().getMoisture(),
                moistureOptimum, warningTolerance);
    }

    public WarningType getHumidityWarning() {
        return getWarning(minHumidity, maxHumidity,
                getCurrentMeasurement() == null ? Float.NaN :
                    getCurrentMeasurement().getHumidity()
                , humidityOptimum, warningTolerance);
    }

    //percent is wrong
    public WarningType getTemperatureWarning() {
        return getWarning(minTemperature, maxTemperature,
                getCurrentMeasurement() == null ? Float.NaN :
                        getCurrentMeasurement().getTemperature(),
                temperatureOptimum, warningTolerance);
    }


    private WarningType getWarning(float min, float max, float val, float opt, float tol) {
        if (val <= max && val >= min && !Float.isNaN(val)) {
            if (val <= opt + tol && val >= opt - tol)
                return WarningType.OPTIMUM;
            else if (val <= opt + tol * 2 && val >= opt - tol * 2)
                return WarningType.NORMAL;
            else if (val > opt)
                return WarningType.CRITICAL_ABOVE;
            else
                return WarningType.CRITICAL_BELOW;
        }
        return WarningType.UNKNOWN;
    }

    //region Refresh

    /**
     * Refreshes the current values
     */
    public void refreshCurrentMeasurement() {
        System.out.println(getMoistureOptimum());
        float moist = Float.NaN, temp = Float.NaN, hum = Float.NaN;
        Socket clientSocket;
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(ip, 80), 1000);
        } catch (IOException e1) {
            current = new Measurement(0, moist, temp, hum);
            return;
        }
        if (!clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                current = new Measurement(0, moist, temp, hum);
                return;
            }
            current = new Measurement(0, moist, temp, hum);
            return;
        }
        DataOutputStream outToServer = null;
        BufferedReader inFromServer = null;
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
            current = new Measurement(0, moist, temp, hum);
            return;
        }
        try {
            outToServer.writeBytes("get2\n");
        } catch (IOException e1) {
            current = new Measurement(0, moist, temp, hum);
            return;
        }
        String sentence = null;
        try {
            sentence = inFromServer.readLine();
        } catch (IOException e1) {
            current = new Measurement(0, moist, temp, hum);
            return;
        }
        if (sentence == null) {
            current = new Measurement(0, moist, temp, hum);
            return;
        }

        String split[] = sentence.split(";");
        if (split.length != 3) {
            current = new Measurement(0, moist, temp, hum);
            return;
        }
        try {
            moist = Float.parseFloat(split[0]);
        } catch (NumberFormatException ex) { }
        try { temp = Float.parseFloat(split[1]);
        } catch (NumberFormatException ex) { }
        try {
            hum = Float.parseFloat(split[2]);
        } catch (NumberFormatException ex) { }

        if (moist < 0 || moist > 100)
            moist = Float.NaN;
        if (hum < 0 || hum > 100)
            hum = Float.NaN;

        current = new Measurement(0, moist, temp, hum);

        try {
            inFromServer.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e1) {
        }
    }

    public void refreshMeasurements() {
        float moist = Float.NaN, temp = Float.NaN, hum = Float.NaN;
        long time = -1;
        Socket clientSocket;
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(ip, 80), 10000);
        } catch (IOException e1) {
            return;
        }
        if (!clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
            return;
        }
        DataOutputStream outToServer = null;
        BufferedReader inFromServer = null;
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        try {
            outToServer.writeBytes("get1\n");
        } catch (IOException e1) {
            return;
        }
        measurements.clear();
        while (true) {
            String sentence = null;
            boolean formatError = false;
            try {
                sentence = inFromServer.readLine();
            } catch (IOException e1) {
                return;
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
        //measurements.sort(null);
        //return;
    }

    //endregion


    //region Find Arduino
    public boolean testConnection() {
        return checkIp(getIp());
    }

    private boolean checkIp(String ip) {
        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 80), 2000);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader inputStream = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));
            outputStream.writeBytes("get3");
            String str = "";
            int timeout = 0;
            while (!inputStream.ready() && timeout < 5) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeout++;
            }
            if (!inputStream.ready())
                return false;
            str = inputStream.readLine();
            if (str.equals("hello"))
                return true;
        } catch (SocketTimeoutException ex) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public boolean findArduino() {
        Enumeration<NetworkInterface> nets = null;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address) {
                    byte[] bIp = inetAddress.getAddress();
                    String strIp = (bIp[0] & 0xFF) + "." + (bIp[1] & 0xFF) + "." + (bIp[2] & 0xFF) + "."; //& 0xFF to make the byte unsigned
                    //Loop through all ips
                    for (int i = 0; i <= 255; i++)
                        if (checkIp(strIp + i)) {
                            setIp(strIp + i);
                            return true;
                        }
                }
            }
        }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }
    //endregion
}
