package controller;

import java.io.*;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import model.*;



public class Controller implements Observer {
    //constants
    private static final String mainPropertiesFile = "mainProperties.prop";

    private PlantModel model;

    @FXML
    private Tab tabMoisture;

    @FXML
    private Label tabMoistureText;

    @FXML
    private FlowPane flowPane;

    @FXML
    private ImageView imageViewMoisture;

    @FXML
    private Canvas canvasCurrentMoisture;

    @FXML
    private Canvas canvasMoistureHistory;

    @FXML
    private Canvas canvasSunlightHistory;

    @FXML
    private Canvas canvasCurrentSunlight;

    @FXML
    private Canvas canvasCurrentTemperature;

    @FXML
    private Canvas canvasTemperatureHistory;

    @FXML
    private Canvas canvasCurrentHumidity;

    @FXML
    private Canvas canvasHumidityHistory;

    @FXML
    private Tab tabSettings;

    @FXML
    private Tab tabSunlight;

    @FXML
    private Label tabSettingsText;

    @FXML
    private Tab tabTemperature;

    @FXML
    private Label tabTemperatureText;

    @FXML
    private Tab tabHumidity;

    @FXML
    private Label tabHumidityText;

    @FXML
    private Label tabSunlightText;

    @FXML
    private TextField tfSetIP;

    @FXML
    private Button btnSetIP;

    @FXML
    private Label labelSetLanguage;

    @FXML
    private ComboBox<String> comboSetLanguage;

    @FXML
    private Label labelSetMoistureOptimum;

    @FXML
    private ComboBox<Integer> comboSetMoistureOptimum;

    @FXML
    private ComboBox<Integer> comboSetHumidityOptimum;

    @FXML
    private Label labelSetArduinoIP;

    @FXML
    private Label labelCurrentMoisturePercent;

    @FXML
    private Label labelCurrentMoisture;

    @FXML
    private Label labelCurrentTemperaturePercent;

    @FXML
    private Label labelCurrentTemperature;

    @FXML
    private Label labelCurrentHumidityPercent;

    @FXML
    private Label labelCurrentHumidity;

    @FXML
    private Label labelCurrentSunlight;

    @FXML
    private ImageView imageViewTemperature;

    @FXML
    private ImageView imageViewHumidity;

    @FXML
    private ImageView imageViewSunlight;

    @FXML
    private ImageView imageViewSettings;

    private static long historyBeginningDrawTimeMoisture = 0;
    private static long historyEndingDrawTimeMoisture = 30000;
    private static Color temperatureColor = Color.web("#d35400");
    private static Color humidityColor = Color.web("#1abc9c");
    private static Color moistureColor = Color.web("#2980b9");

    File resourcesPath;
    File imagesPath;
    private void loadMainProperties() {
        try {
            FileInputStream propFile = new FileInputStream(mainPropertiesFile);
            Properties prop = new Properties();
            prop.load(propFile);
            String ip = prop.getProperty("ip");
            if (ip != null)
                model.getPlant().setIp(ip);
        }
        catch (Exception e){ }
    }
    private void storeMainProperties() {
        try {
            FileOutputStream propFile = new FileOutputStream(mainPropertiesFile);
            Properties prop = new Properties();
            prop.setProperty("ip", model.getPlant().getIp());
            prop.store(propFile, "");
        }
        catch (Exception e) { }
    }

    @FXML
    void initialize() {
        resourcesPath = new File(new File("").getAbsolutePath()+File.separator+"src"+File.separator+ "main" +File.separator+"resources");
        imagesPath = new File(resourcesPath, "images");
        assert tabMoistureText != null : "fx:id=\"tabMoistureText\" was not injected: check your FXML file 'sample.fxml'.";
        assert flowPane != null : "fx:id=\"flowPane\" was not injected: check your FXML file 'sample.fxml'.";
        assert canvasCurrentMoisture != null : "fx:id=\"canvasCurrentMoisture\" was not injected: check your FXML file 'sample.fxml'.";
        assert canvasMoistureHistory != null : "fx:id=\"canvasMoistureHistory\" was not injected: check your FXML file 'sample.fxml'.";
        assert tabSettings != null : "fx:id=\"tabSettings\" was not injected: check your FXML file 'sample.fxml'.";
        assert tabSettingsText != null : "fx:id=\"tabSettingsText\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfSetIP != null : "fx:id=\"tfSetIP\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnSetIP != null : "fx:id=\"btnSetIP\" was not injected: check your FXML file 'sample.fxml'.";
        assert comboSetLanguage != null : "fx:id=\"comboSetLanguage\" was not injected: check your FXML file 'sample.fxml'.";
        assert labelSetLanguage != null : "fx:id=\"labelSetLanguage\" was not injected: check your FXML file 'sample.fxml'.";

        model = new PlantModel(this);

        for(int i = 0; i <= 10; i++) {
            comboSetMoistureOptimum.getItems().add(i * 10);
            comboSetHumidityOptimum.getItems().add(i * 10);
        }
        imageViewMoisture.setImage(new Image("file:" + new File(imagesPath,"moistureIcon.png").getAbsolutePath()));
        imageViewTemperature.setImage(new Image("file:" + new File(imagesPath,"temperatureIcon.png").getAbsolutePath()));
        imageViewHumidity.setImage(new Image("file:" + new File(imagesPath,"humidityIcon.png").getAbsolutePath()));
        imageViewSunlight.setImage(new Image("file:" + new File(imagesPath,"sunlightIcon.png").getAbsolutePath()));
        imageViewSettings.setImage(new Image("file:" + new File(imagesPath,"settingsIcon.png").getAbsolutePath()));

        loadMainProperties();
        tfSetIP.setText(model.getPlant().getIp());
    }
    @FXML
    void onBtnSetIPAction(ActionEvent event) {
        model.getPlant().setIp(tfSetIP.getText());
        storeMainProperties();
    }
    @FXML
    void onComboSetMoistureOptimumAction(ActionEvent event) {
        model.getPlant().getMoistureHistory().setOptimum(comboSetMoistureOptimum.getValue());
    }
    @FXML
    void onComboSetHumidityOptimumAction(ActionEvent event) {
        model.getPlant().getHumidityHistory().setOptimum(comboSetHumidityOptimum.getValue());
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            if (model.getPlant().getMoistureHistory().getFirstMeasurement() != null
                    && !Float.isNaN(model.getPlant().getMoistureHistory().getFirstMeasurement().getValue()))
                labelCurrentMoisturePercent.setText(model.getPlant().getMoistureHistory().getFirstMeasurement().getValue() + "%");
            else
                labelCurrentMoisturePercent.setText("Not available");
            if (model.getPlant().getTemperatureHistory().getFirstMeasurement() != null
                    && !Float.isNaN(model.getPlant().getTemperatureHistory().getFirstMeasurement().getValue()))
                labelCurrentTemperaturePercent.setText(model.getPlant().getTemperatureHistory().getFirstMeasurement().getValue() + "°C");
            else
                labelCurrentTemperaturePercent.setText("Not available");
            if (model.getPlant().getHumidityHistory().getFirstMeasurement() != null
                    && !Float.isNaN(model.getPlant().getHumidityHistory().getFirstMeasurement().getValue()))
                labelCurrentHumidityPercent.setText(model.getPlant().getHumidityHistory().getFirstMeasurement().getValue() + "%");
            else
                labelCurrentMoisturePercent.setText("Not available");
            redraw();
        });
    }
    private void redraw() {
        //moisture
        redrawCurrentMeasurement(model.getPlant().getMoistureHistory(),canvasCurrentMoisture,moistureColor,5,0);
        redrawHistroy(model.getPlant().getMoistureHistory(),canvasMoistureHistory,moistureColor);

        //humidity
        redrawCurrentMeasurement(model.getPlant().getHumidityHistory(),canvasCurrentHumidity,humidityColor,5,0);
        redrawHistroy(model.getPlant().getHumidityHistory(),canvasHumidityHistory,humidityColor);

        //temperature
        redrawCurrentMeasurement(model.getPlant().getTemperatureHistory(),canvasCurrentTemperature,temperatureColor,5,1);
        redrawHistroy(model.getPlant().getTemperatureHistory(),canvasTemperatureHistory,temperatureColor);
    }
    private void redrawHistroy(MeasurementHistory history, Canvas canvas, Color color) {
        List<Measurement> list = history.getMeasurements();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        float minimum = history.getMinumum();
        float maximum = history.getMaximum();
        float optimum = history.getOptimum();
        Measurement first = history.getFirstMeasurement();
        Measurement last = history.getLastMeasurement();
        if (first == null || last == null)
            return;
        long time = first.getDate().getTime();
        double elementSpan= (historyEndingDrawTimeMoisture-historyBeginningDrawTimeMoisture) / width;
        double elementPower= height / (maximum-minimum);
        long mDate;
        double mValue;
        long lastDate = last.getDate().getTime();
        double lastValue = last.getValue();

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, width, height);
        graphicsContext.setFill(Color.web("#ecf0f1"));
        graphicsContext.fillRect(0, 0, width, height);
        graphicsContext.setFill(Color.LIGHTBLUE);
        if(minimum < 0)
            graphicsContext.fillRect(0, height + (minimum * elementPower), width, height);
        graphicsContext.setLineWidth(2);
        graphicsContext.setStroke(color);
        Color fillColor = new Color(color.getRed(), color.getGreen(),color.getBlue(), 0.5);
        graphicsContext.setFill(fillColor);
        if(last != null) {
            for (Measurement m : list) {
                mDate = m.getDate().getTime();
                mValue = m.getValue();
                double[] xPoints = {
                        width - 1 - ((lastDate - time) * -1) / elementSpan, width - ((mDate - time) * -1) / elementSpan,
                        width - ((mDate - time) * -1) / elementSpan, width - 1 - ((lastDate - time) * -1) / elementSpan
                };
                double[] yPoints = {
                        height - (lastValue - minimum) * elementPower,
                        height - (mValue - minimum) * elementPower, height, height
                };
                graphicsContext.fillPolygon(xPoints, yPoints, 4);
                graphicsContext.strokeLine(width - ((lastDate - time) * -1) / elementSpan, height - elementPower * (lastValue-minimum),
                        width - ((mDate - time) * -1) / elementSpan, height - (mValue - minimum) * elementPower);

                lastDate = mDate;
                lastValue = mValue;
            }
        }
        graphicsContext.setStroke(new Color(0, 0, 0, 1));
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeRect(0, 0, width, height);
        graphicsContext.strokeLine(0, height - (optimum - minimum) * elementPower, width, height - (optimum - minimum) * elementPower);
        graphicsContext.setLineWidth(1);
        int textOffset = -5;
        if((optimum * elementPower * 100) / height > 90)
            textOffset = 15;
        graphicsContext.strokeText("Optimum", width / 200, height - (optimum - minimum) * elementPower + textOffset);
        for(int i = 0; i < 6; i++)
            graphicsContext.strokeLine(i* (width / 7) + width / 7, 0, i * (width / 7) + width / 7, height);
    }
    private void redrawCurrentMeasurement(MeasurementHistory history, Canvas canvas, Color color, int lineWidth, int mode) {
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        Measurement measurement = history.getFirstMeasurement();
        if (measurement == null)
            return;
        float minimum = history.getMinumum();
        float maximum = history.getMaximum();
        float value = measurement.getValue();
        GraphicsContext graphicsContext=canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, width, height);
        graphicsContext.setFill(color);
        graphicsContext.setLineWidth(lineWidth);
        switch (mode) {
            case 0: //moisture

                graphicsContext.fillArc(lineWidth * 2, height - (width - lineWidth * 4) - lineWidth * 2, width - lineWidth * 4, width - lineWidth * 4,
                        360, 360, ArcType.OPEN);

                graphicsContext.fillRect(lineWidth * 2, 0, width - lineWidth * 4, height - width / 2);//fehler

                double percentage = (measurement.getValue() - history.getMinumum()) / (history.getMaximum() - history.getMinumum());
                double power = height - ((height - lineWidth * 2) * percentage);
                graphicsContext.clearRect(0, 0, width, (height - lineWidth * 2) - ((height - lineWidth * 2) *
                        ((measurement.getValue() - history.getMinumum()) / (history.getMaximum() - history.getMinumum()))));

                graphicsContext.strokeArc(lineWidth, height - (width - lineWidth * 2) - lineWidth, width - lineWidth * 2,
                        width - lineWidth * 2, 180, 180, ArcType.OPEN);

                graphicsContext.strokeLine(lineWidth, 0, lineWidth, height - width / 2 + lineWidth);
                graphicsContext.strokeLine(width - lineWidth, 0, width - lineWidth, height - width / 2 + lineWidth);
                break;
            case 1: //temperature
                double circlewidth = width-4*lineWidth;
                double stickwidth = (width-4*lineWidth)*0.66;
                graphicsContext.fillArc(lineWidth * 2, height - circlewidth - lineWidth * 2, circlewidth, circlewidth,
                        360, 360, ArcType.OPEN);
                graphicsContext.fillArc(lineWidth*2+(circlewidth-stickwidth)/2,lineWidth*2,stickwidth,stickwidth,360,360,ArcType.OPEN);
                graphicsContext.fillRect(lineWidth*2+(circlewidth-stickwidth)/2,lineWidth*2+stickwidth/2,stickwidth,height-(lineWidth*2+stickwidth/2)-lineWidth*2-circlewidth/2);

                double per =1-(value-minimum)/(maximum-minimum);
                graphicsContext.clearRect(0,0,width,lineWidth*2+(height-lineWidth*4)*per);

                graphicsContext.strokeArc((circlewidth-stickwidth)/2,lineWidth/2,stickwidth+lineWidth*4,stickwidth+lineWidth*4,0,180,ArcType.OPEN);
                graphicsContext.strokeLine((circlewidth-stickwidth)/2,stickwidth*0.5+lineWidth*2+lineWidth/2,(circlewidth-stickwidth)/2,height-lineWidth*4-circlewidth+lineWidth/2);
                double Angle = Math.atan((stickwidth/2+lineWidth*2)/(circlewidth/2))/Math.PI*180;
                graphicsContext.strokeArc(0,height-4*lineWidth+lineWidth/2-circlewidth,circlewidth+lineWidth*4,circlewidth+lineWidth*4,90+Angle,360-Angle*2,ArcType.OPEN);

                break;
        }

    }
}

