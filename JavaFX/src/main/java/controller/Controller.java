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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import model.*;



public class Controller implements Observer {
    private PlantModel model;

    //region FXMLControls
    @FXML
    private Circle circleOTemperatureWarning;

    @FXML
    private Circle circleOHumidityWarning;

    @FXML
    private Circle circleOMoistureWarning;

    @FXML
    private Circle circleTemperatureWarning;

    @FXML
    private Circle circleHumidityWarning;

    @FXML
    private Circle circleMoistureWarning;

    @FXML
    private Label labelSetTemperatureOptimum;

    @FXML
    private ComboBox<Integer> comboSetTemperatureOptimum;
    @FXML
    private Label labelOCurrentHumidity;

    @FXML
    private Label labelOCurrentHumidityPercent;

    @FXML
    private Label labelOCurrentTemperature;

    @FXML
    private Label labelOCurrentTemperatureCelsius;

    @FXML
    private Tab tabOverview;

    @FXML
    private Label labelOverviewText;

    @FXML
    private Label labelOCurrentMoisture;

    @FXML
    private Label labelOCurrentMoisturePercent;

    @FXML
    private Tab tabMoisture;

    @FXML
    private Label tabMoistureText;

    @FXML
    private FlowPane flowPane;

    @FXML
    private ImageView imageViewMoisture;

    @FXML
    private ResizeableCanvas canvasCurrentMoisture;

    @FXML
    private ResizeableCanvas canvasMoistureHistory;

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
    private Label labelSetHumidityOptimum;

    @FXML
    private Label labelCurrentMoisturePercent;

    @FXML
    private Label labelCurrentMoisture;

    @FXML
    private Label labelCurrentTemperatureCelsius;

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

    @FXML
    private Label labelLanguage;

    @FXML
    private Button btnGerman;

    @FXML
    private Button btnEnglish;
    //endregion
    @FXML
    private HBox hboxMoisture;

    @FXML
    private HBox hboxMoistureHistory;

    @FXML
    private HBox hboxTemperature;

    @FXML
    private HBox hboxTemperatureHistory;

    @FXML
    private HBox hboxHumidity;

    @FXML
    private HBox hboxHumidityHistory;

    @FXML
    private ImageView imgPichler;

    @FXML
    private ImageView imgKrauck;

    @FXML
    private ImageView imgPanz;

    @FXML
    private ImageView imgRiedl;

    @FXML
    private Button btnAbout;

    @FXML
    private ImageView imgJava;

    @FXML
    private ImageView imgArduino;

    @FXML
    void handleBtnAbout(ActionEvent event) {
        //TODO
        Alert a = new Alert(Alert.AlertType.WARNING, "http://dschihad.eu");
        a.show();
    }

    //region Constants
    private static final String mainPropertiesFile = "mainProperties.prop";
    private static final String languagePropertyFile = "LanguageProperty";
    private final long historyBeginningDrawTimeMoisture = 0;
    private final long historyEndingDrawTimeMoisture = 30000;
    private final Color temperatureColor = Color.web("#d35400");
    private final Color humidityColor = Color.web("#1abc9c");
    private final Color moistureColor = Color.web("#2980b9");
    private final Color normalWarning = Color.ORANGE; //rename
    private final Color criticalWarning = Color.RED;
    private final Color optimumWarning = Color.LIME;
    private final String defaultArduinoIp = "localhost";
    private final float defaultMoistureOptimum = 50;
    private final float defaultHumidityOptimum = 50;
    private final float defaultTemperatureOptimum = 30;
    //endregion

    private File resourcesPath;
    private File imagesPath;

    private String arduinoIp = defaultArduinoIp;
    private float moistureOptimum = defaultMoistureOptimum;
    private float humidityOptimum = defaultHumidityOptimum;
    private float temperatureOptimum = defaultTemperatureOptimum;

    private void loadMainProperties() {
        try {
            FileInputStream propFile = new FileInputStream(mainPropertiesFile);
            Properties prop = new Properties();
            prop.load(propFile);
            arduinoIp = prop.getProperty("ip");

            //if no ip is saved, use default ip
            if (arduinoIp == null)
                arduinoIp = defaultArduinoIp;

            //Moisture optimum
            String tmp = prop.getProperty("mOptimum");
            try {
                moistureOptimum = Float.parseFloat(tmp);
            } catch (NumberFormatException ex) {
                moistureOptimum = defaultMoistureOptimum;
            }

            //Humidity optimum
            tmp = prop.getProperty("hOptimum");
            try {
                humidityOptimum = Float.parseFloat(tmp);
            } catch (NumberFormatException ex) {
                humidityOptimum = defaultHumidityOptimum;
            }

            //Temperature optimum
            tmp = prop.getProperty("tOptimum");
            try {
                temperatureOptimum = Float.parseFloat(tmp);
            } catch (NumberFormatException ex) {
                temperatureOptimum = defaultTemperatureOptimum;
            }

            comboSetHumidityOptimum.setValue((int)humidityOptimum);
            comboSetMoistureOptimum.setValue((int)moistureOptimum);
            comboSetTemperatureOptimum.setValue((int)temperatureOptimum);
            tfSetIP.setText(arduinoIp);

            if (model != null && model.getPlant() != null) {
                model.getPlant().setIp(arduinoIp);
                model.getPlant().getMoistureHistory().setOptimum(moistureOptimum);
                model.getPlant().getMoistureHistory().setOptimum(humidityOptimum);
                model.getPlant().getTemperatureHistory().setOptimum(temperatureOptimum);
            }
        }
        catch (Exception e){ }
    }
    private void storeMainProperties() {
        try {
            if (model == null || model.getPlant() == null)
                return;
            FileOutputStream propFile = new FileOutputStream(mainPropertiesFile);
            Properties prop = new Properties();
            prop.setProperty("ip", model.getPlant().getIp());
            prop.setProperty("mOptimum", String.valueOf(model.getPlant().getMoistureHistory().getOptimum()));
            prop.setProperty("hOptimum", String.valueOf(model.getPlant().getHumidityHistory().getOptimum()));
            prop.setProperty("tOptimum", String.valueOf(model.getPlant().getTemperatureHistory().getOptimum()));
            prop.store(propFile, "");
        } catch (FileNotFoundException e) {
            System.out.println(mainPropertiesFile + " not found!");
        } catch (IOException e) {
            System.out.println("Error storing properties.\n" + e.getMessage());
        }

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

        loadMainProperties();
        model = new PlantModel(moistureOptimum, humidityOptimum, temperatureOptimum, arduinoIp, this);

        for(int i = 0; i <= 10; i++) {
            comboSetMoistureOptimum.getItems().add(i * 10);
            comboSetHumidityOptimum.getItems().add(i * 10);
        }

        for (int i = (int)PlantModel.minTemperature; i <= PlantModel.maxTemperature; i++)
            comboSetTemperatureOptimum.getItems().add(i);

//        imageViewMoisture.setImage(new Image("file:" + new File(imagesPath,"flat life icon.png").getAbsolutePath()));
  //      imageViewTemperature.setImage(new Image("file:" + new File(imagesPath,"flat temperature icon final.png").getAbsolutePath()));
    //    imageViewHumidity.setImage(new Image("file:" + new File(imagesPath,"flat humidity fin.png").getAbsolutePath()));
      //  imageViewSunlight.setImage(new Image("file:" + new File(imagesPath,"flat sun.png").getAbsolutePath()));
        //imageViewSettings.setImage(new Image("file:" + new File(imagesPath,"settingsIcon.png").getAbsolutePath()));

        //PANZ
        imgJava.setImage(new Image("file:" + new File(imagesPath, "java.jpg").getAbsolutePath()));
        imgArduino.setImage(new Image("file:" + new File(imagesPath, "arduino.png").getAbsolutePath()));

        imgPichler.setImage(new Image("file:" + new File(imagesPath, "pichler.jpg").getAbsolutePath()));
        imgKrauck.setImage(new Image("file:" + new File(imagesPath, "krauck.jpg").getAbsolutePath()));
        imgPanz.setImage(new Image("file:" + new File(imagesPath, "panz.jpg").getAbsolutePath()));
        imgRiedl.setImage(new Image("file:" + new File(imagesPath, "riedl.jpg").getAbsolutePath()));

        //moisture
        hboxMoistureHistory.widthProperty().addListener((observable, oldValue, newValue) -> {
            canvasMoistureHistory.resize(newValue.doubleValue(), canvasMoistureHistory.getHeight());
            redraw();
        });

        hboxMoistureHistory.heightProperty().addListener((observable, oldValue, newValue) -> {
            canvasMoistureHistory.resize(canvasMoistureHistory.getWidth(), newValue.doubleValue());
            redraw();
        });

        hboxMoisture.heightProperty().addListener((observable, oldValue, newValue) -> {
            canvasCurrentMoisture.resize(newValue.doubleValue() / 2, newValue.doubleValue());
            System.out.println(newValue);
            redraw();
        });

        //temperature
        hboxTemperatureHistory.widthProperty().addListener(((observable, oldValue, newValue) -> {
            canvasTemperatureHistory.resize(newValue.doubleValue(), canvasTemperatureHistory.getHeight());
            redraw();
        }));

        hboxTemperatureHistory.heightProperty().addListener(((observable, oldValue, newValue) -> {
            canvasTemperatureHistory.resize(canvasTemperatureHistory.getWidth(), newValue.doubleValue());
            redraw();
        }));

        hboxTemperature.heightProperty().addListener(((observable, oldValue, newValue) -> {
            canvasCurrentTemperature.resize(newValue.doubleValue() / 2, newValue.doubleValue());
            redraw();
        }));

        //humidity
        hboxHumidityHistory.widthProperty().addListener(((observable, oldValue, newValue) -> {
            canvasHumidityHistory.resize(newValue.doubleValue(), canvasHumidityHistory.getHeight());
            redraw();
        }));

        hboxHumidityHistory.heightProperty().addListener(((observable, oldValue, newValue) -> {
            canvasHumidityHistory.resize(canvasHumidityHistory.getWidth(), newValue.doubleValue());
            redraw();
        }));

        hboxHumidity.heightProperty().addListener(((observable, oldValue, newValue) -> {
            canvasCurrentHumidity.resize(newValue.doubleValue() / 2, newValue.doubleValue());
            redraw();
        }));
    }

    //region FXMLEvents
    @FXML
    void onBtnSetIPAction(ActionEvent event) {
        arduinoIp = tfSetIP.getText();
        model.getPlant().setIp(arduinoIp);
        storeMainProperties();
    }
    @FXML
    void onComboSetMoistureOptimumAction(ActionEvent event) {
        moistureOptimum = comboSetMoistureOptimum.getValue();
        model.getPlant().getMoistureHistory().setOptimum(moistureOptimum);
        storeMainProperties();
    }
    @FXML
    void onComboSetHumidityOptimumAction(ActionEvent event) {
        humidityOptimum = comboSetHumidityOptimum.getValue();
        model.getPlant().getHumidityHistory().setOptimum(humidityOptimum);
        storeMainProperties();
    }

    @FXML
    void onComboSetTemperatureOptimumAction(ActionEvent event) {
        temperatureOptimum = comboSetTemperatureOptimum.getValue();
        model.getPlant().getTemperatureHistory().setOptimum(temperatureOptimum);
        storeMainProperties();
    }

    @FXML
    void onBtnGermanAction(ActionEvent event)
    {
        setLanguage("de", "DE");
    }

    @FXML
    void onBtnEnglishAction(ActionEvent event)
    {
        setLanguage("en", "US");
    }
    //endregion

    private void warnings() {
        WarningType warning = model.getPlant().getMoistureHistory().getWarning();
        if (warning == WarningType.Optimum)
            circleMoistureWarning.setFill(optimumWarning);
        else if (warning == WarningType.Normal)
            circleMoistureWarning.setFill(normalWarning);
        else if (warning == WarningType.Critical)
            circleMoistureWarning.setFill(criticalWarning);

        warning = model.getPlant().getHumidityHistory().getWarning();
        if (warning == WarningType.Optimum)
            circleHumidityWarning.setFill(optimumWarning);
        else if (warning == WarningType.Normal)
            circleHumidityWarning.setFill(normalWarning);
        else if (warning == WarningType.Critical)
            circleHumidityWarning.setFill(criticalWarning);

        warning = model.getPlant().getTemperatureHistory().getWarning();
        if (warning == WarningType.Optimum)
            circleTemperatureWarning.setFill(optimumWarning);
        else if (warning == WarningType.Normal)
            circleTemperatureWarning.setFill(normalWarning);
        else if (warning == WarningType.Critical)
            circleTemperatureWarning.setFill(criticalWarning);

        circleOHumidityWarning.setFill(circleHumidityWarning.getFill());
        circleOMoistureWarning.setFill(circleMoistureWarning.getFill());
        circleOTemperatureWarning.setFill(circleTemperatureWarning.getFill());
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
                labelCurrentTemperatureCelsius.setText(model.getPlant().getTemperatureHistory().getFirstMeasurement().getValue() + "°C");
            else
                labelCurrentTemperatureCelsius.setText("Not available");
            if (model.getPlant().getHumidityHistory().getFirstMeasurement() != null
                    && !Float.isNaN(model.getPlant().getHumidityHistory().getFirstMeasurement().getValue()))
                labelCurrentHumidityPercent.setText(model.getPlant().getHumidityHistory().getFirstMeasurement().getValue() + "%");
            else
                labelCurrentHumidityPercent.setText("Not available");

            //Set text of Overview values
            labelOCurrentMoisturePercent.setText(labelCurrentMoisturePercent.getText());
            labelOCurrentTemperatureCelsius.setText(labelCurrentTemperatureCelsius.getText());
            labelOCurrentHumidityPercent.setText(labelCurrentHumidityPercent.getText());
            warnings();
            redraw();
        });
    }
    private void redraw() {
        //moisture
        redrawCurrentMeasurement(model.getPlant().getMoistureHistory(),canvasCurrentMoisture,moistureColor, 5, 0);
        redrawHistroy(model.getPlant().getMoistureHistory(), canvasMoistureHistory, moistureColor);

        //humidity
        redrawCurrentMeasurement(model.getPlant().getHumidityHistory(), canvasCurrentHumidity, humidityColor, 5, 0);
        redrawHistroy(model.getPlant().getHumidityHistory(), canvasHumidityHistory, humidityColor);

        //temperature
        redrawCurrentMeasurement(model.getPlant().getTemperatureHistory(), canvasCurrentTemperature, temperatureColor, 5, 0);
        redrawHistroy(model.getPlant().getTemperatureHistory(), canvasTemperatureHistory, temperatureColor);
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
                        width - 1 - ((lastDate - time) * -1) / elementSpan, width - 0.9 -((mDate - time) * -1) / elementSpan,
                        width - 0.9 -((mDate - time) * -1) / elementSpan, width - 1 - ((lastDate - time) * -1) / elementSpan
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

    private void setLanguage(String language, String country)
    {
            Locale locale = new Locale(language, country);
            ResourceBundle rB = ResourceBundle.getBundle(languagePropertyFile, locale);
        try {
            labelLanguage.setText(rB.getString("language"));
            tabHumidityText.setText(rB.getString("humidity")); //rename to label
            tabMoistureText.setText(rB.getString("moisture"));
            tabSettingsText.setText(rB.getString("settings"));
            labelOverviewText.setText(rB.getString("overview"));
            tabSunlightText.setText(rB.getString("sunlight"));
            tabTemperatureText.setText(rB.getString("temperature"));
            labelOCurrentMoisture.setText(rB.getString("moisture"));
            labelOCurrentHumidity.setText(rB.getString("humidity"));
            labelOCurrentTemperature.setText(rB.getString("temperature"));
            labelCurrentHumidity.setText(rB.getString("currenthumidity"));
            labelCurrentMoisture.setText(rB.getString("currentmoisture"));
            labelCurrentTemperature.setText(rB.getString("currenttemperature"));
            labelCurrentSunlight.setText(rB.getString("currentsunlight"));
            labelSetArduinoIP.setText(rB.getString("setarduinoip"));
            labelSetMoistureOptimum.setText(rB.getString("setmoistureopt"));
            labelSetTemperatureOptimum.setText(rB.getString("settemperatureopt"));
            labelSetHumidityOptimum.setText(rB.getString("sethumidityopt"));
            btnSetIP.setText(rB.getString("setip"));



        } catch (MissingResourceException ex) {
            System.out.println("error");
        }
    }


    //@krauck use enum for mode
    private void redrawCurrentMeasurement(MeasurementHistory history, Canvas canvas, Color color, int lineWidth, int mode) {
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        Measurement measurement = history.getFirstMeasurement();
        if (measurement == null)
            return;
        float minimum = history.getMinumum();
        float maximum = history.getMaximum();
        float value = measurement.getValue();
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
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

                graphicsContext.strokeLine(lineWidth, 0, lineWidth, height - width / 2 - lineWidth + 1);
                graphicsContext.strokeLine(width - lineWidth, 0, width - lineWidth, height - width / 2 - lineWidth + 1 );
                break;
            case 1: //temperature
                double circlewidth = width-4*lineWidth;
                double stickwidth = circlewidth*0.66;
                graphicsContext.fillArc(lineWidth * 2, height - circlewidth - lineWidth * 2, circlewidth, circlewidth,
                        360, 360, ArcType.OPEN);
                graphicsContext.fillArc(lineWidth*2+(circlewidth-stickwidth)/2,lineWidth*2,stickwidth,stickwidth,360,360,ArcType.OPEN);
                graphicsContext.fillRect(lineWidth*2+(circlewidth-stickwidth)/2,lineWidth*2+stickwidth/2,stickwidth,height-(lineWidth*2+stickwidth/2)-lineWidth*2-circlewidth/2);

                double per =1-(value-minimum)/(maximum-minimum);
                graphicsContext.clearRect(0,0,width,lineWidth*2+(height-lineWidth*4)*per);

                double angle = Math.atan((stickwidth/2+lineWidth*2)/(circlewidth/2+lineWidth*2))/Math.PI*180;
                angle=90-angle;
                double newwidth = circlewidth/2 - Math.tan(angle/180*Math.PI)*(stickwidth/2);
                //System.out.println((stickwidth/2+lineWidth*2)/(circlewidth/2+lineWidth*2));
                graphicsContext.strokeLine((width-stickwidth-lineWidth*4)/2+lineWidth/2,lineWidth/2+stickwidth/2+lineWidth*2,(width-stickwidth-lineWidth*4)/2+lineWidth/2,height-lineWidth/2-lineWidth*2-circlewidth+newwidth);

                graphicsContext.strokeArc(lineWidth/2,height-circlewidth-lineWidth/2-lineWidth*3,circlewidth+lineWidth*3,circlewidth+lineWidth*3,180-90+angle,360-angle*2,ArcType.OPEN);
                break;
        }

    }
}

