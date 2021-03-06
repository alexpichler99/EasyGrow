package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import scene.ResizeableCanvas;

import java.io.*;
import java.net.URL;
import java.util.*;


public class Controller implements Observer, Initializable {


    //region FXML_CONTROLS
    @FXML
    private Label labelOMoistureStatus;

    @FXML
    private Label labelOHumidityStatus;

    @FXML
    private Label labelOTemperatureStatus;

    @FXML
    private Label labelMoistureStatus;

    @FXML
    private Label labelHumidityStatus;

    @FXML
    private Label labelTemperatureStatus;

    @FXML
    private Label labelDisplayDays;

    @FXML
    private ComboBox<Integer> comboDisplayDays;

    @FXML
    private NumberAxis xAxisaChartMoisture;

    @FXML
    private NumberAxis xAxisaChartTemperature;

    @FXML
    private NumberAxis xAxisaChartHumidity;

    @FXML
    private NumberAxis yAxisaChartTemperature;

    @FXML
    private NumberAxis yAxisaChartHumidity;

    @FXML
    private NumberAxis yAxisaChartMoisture;

    @FXML
    private NumberAxis xAxislChartOverview;

    @FXML
    private NumberAxis yAxislChartOverview;

    @FXML
    private LineChart<Long, Double> lChartOverview;

    @FXML
    private AreaChart<Long, Double> aChartMoisture;

    @FXML
    private AreaChart<Long, Double> aChartHumidity;

    @FXML
    private AreaChart<Long, Double> aChartTemperature;

    @FXML
    private Label labelSetPlantName;

    @FXML
    private TextField tfSetPlantName;

    @FXML
    private Button btnSetPlantName;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label labelSupportTeacher;

    @FXML
    private HBox hboxMoisture;


    @FXML
    private HBox hboxTemperature;


    @FXML
    private HBox hboxHumidity;


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
    private ResizeableCanvas canvasCurrentMoisture;

    @FXML
    private Canvas canvasCurrentTemperature;

    @FXML
    private Canvas canvasCurrentHumidity;

    @FXML
    private Tab tabSettings;

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
    private TextField tfSetIP;

    @FXML
    private Button btnSetIP;

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
    private ImageView imageViewSettings;

    @FXML
    private Label labelLanguage;

    @FXML
    private Button btnGerman;

    @FXML
    private Button btnEnglish;

    @FXML
    private Tab tabAbout;
    @FXML
    private ImageView imgPichler;

    @FXML
    private ImageView imgKrauck;

    @FXML
    private ImageView imgPanz;

    @FXML
    private ImageView imgRiedl;

    @FXML
    private Label labelAboutText;
    //endregion
    //region FXML_EVENTS
    @FXML
    void onComboDisplayDaysAction(ActionEvent event) {
        setDisplayDays(comboDisplayDays.getValue());
        storeMainProperties();
    }

    @FXML
    void onBtnSetIPAction(ActionEvent event) {
        arduinoIp = tfSetIP.getText();
        model.getPlant().setIp(arduinoIp);
        storeMainProperties();
    }

    @FXML
    void onComboSetMoistureOptimumAction(ActionEvent event) {
        moistureOptimum = comboSetMoistureOptimum.getValue();
        model.getPlant().setMoistureOptimum(moistureOptimum);
        storeMainProperties();
        refreshWarnings();
    }

    @FXML
    void onComboSetHumidityOptimumAction(ActionEvent event) {
        humidityOptimum = comboSetHumidityOptimum.getValue();
        model.getPlant().setHumidityOptimum(humidityOptimum);
        storeMainProperties();
        refreshWarnings();
    }

    @FXML
    void onComboSetTemperatureOptimumAction(ActionEvent event) {
        temperatureOptimum = comboSetTemperatureOptimum.getValue();
        model.getPlant().setTemperatureOptimum(temperatureOptimum);
        storeMainProperties();
        refreshWarnings();
    }

    @FXML
    void onBtnGermanAction(ActionEvent event) {
        language = "de";
        country = "DE";
        setLanguage(language, country);
        storeMainProperties();
    }

    @FXML
    void onBtnEnglishAction(ActionEvent event) {
        language = "en";
        country = "US";
        setLanguage("en", "US");
        storeMainProperties();
    }

    @FXML
    void onBtnSetPlantNameAction(ActionEvent event) {
        plantName = tfSetPlantName.getText();
        model.getPlant().setName(plantName);
        if (stage != null)
            stage.setTitle("EasyGrow - " + plantName);
        storeMainProperties();
    }

    //endregion
    //region CONSTANTS
    private static final String mainPropertiesFile = "MainProperties.properties";
    private static final String languagePropertyFile = "LanguageProperty";
    private final Color temperatureColor = Color.web("#d35400");
    private final Color humidityColor = Color.web("#1abc9c");
    private final Color moistureColor = Color.web("#2980b9");
    private final Color humidityFillColor = Color.web("#1abc9c0f");
    private final Color moistureFillColor = Color.web("#2980b90f");
    private final Color temperatureFillColor = Color.web("#d354000f");
    private final Color normalWarning = Color.ORANGE; //rename
    private final Color criticalWarning = Color.RED;
    private final Color optimumWarning = Color.LIME;
    private final Color unknownWarning = Color.GRAY;
    private final String defaultArduinoIp = "localhost";
    private final float defaultMoistureOptimum = 50;
    private final float defaultHumidityOptimum = 50;
    private final float defaultTemperatureOptimum = 30;
    private final String defaultLanguage = "en";
    private final String defaultCountry = "US";
    private final int currentValueLineWidth = 5;
    private final int defaultDisplayDays = 30;
    private final StringConverter<Number> displayDaysStringConverter = new StringConverter<Number>() {
        @Override
        public String toString(Number object) {
            int val = object.intValue();
            if ((val % 4 == 0 || val == 1) && val != 0)
                return dayText +  (val == 1 ? 0 : (val / 4));
            return "";
        }

        @Override
        public Number fromString(String string) {
            return 0;
        }
    };
    //endregion

    //region VARS
    private PlantModel model;
    private Stage stage;
    private String arduinoIp = defaultArduinoIp;
    private float moistureOptimum = defaultMoistureOptimum;
    private float humidityOptimum = defaultHumidityOptimum;
    private float temperatureOptimum = defaultTemperatureOptimum;
    private String language = defaultLanguage;
    private String country = defaultCountry;
    private String plantName = "";
    private String notAvailableText = "Not available";
    private int displayDays = defaultDisplayDays;
    private String dayText = "Day";
    private String moistureText = "Moisture";
    private String humidityText = "Humidity";
    private String temperatureText = "Temperature";
    private String moistureBelow = "You need to water the plant!";
    private String moistureAbove = "The plant has too much water!";
    private String humidityBelow = "Air is too dry!";
    private String humidityAbove = "Air is too wet!";
    private String temperatureBelow = "It's too cold!";
    private String temperatureAbove = "It's too hot!";
    //endregion

    //region MainProperties
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
            if (tmp != null) {
                try {
                    moistureOptimum = Float.parseFloat(tmp);
                } catch (NumberFormatException ex) {
                    moistureOptimum = defaultMoistureOptimum;
                }
            }
            else
                moistureOptimum = defaultMoistureOptimum;

            //Humidity optimum
            tmp = prop.getProperty("hOptimum");
            if (tmp != null) {
                try {
                    humidityOptimum = Float.parseFloat(tmp);
                } catch (NumberFormatException ex) {
                    humidityOptimum = defaultHumidityOptimum;
                }
            }
            else
                humidityOptimum = defaultHumidityOptimum;

            //Temperature optimum
            tmp = prop.getProperty("tOptimum");
            if (tmp != null) {
                try {
                    temperatureOptimum = Float.parseFloat(tmp);
                } catch (NumberFormatException ex) {
                    temperatureOptimum = defaultTemperatureOptimum;
                }
            }
            else
                temperatureOptimum = defaultTemperatureOptimum;


            //DisplayDays
            tmp = prop.getProperty("displayDays");
            if (tmp != null) {
                try {
                    displayDays = Integer.parseInt(tmp);
                } catch (NumberFormatException ex) {
                    displayDays = defaultDisplayDays;
                }
            }
            else
                displayDays = defaultDisplayDays;

            //language
            language = prop.getProperty("language");
            if (language == null)
                language = defaultLanguage;

            //country
            country = prop.getProperty("country");
            if (country == null)
                country = defaultCountry;

            //plantName
            plantName = prop.getProperty("plantName");
            if (plantName == null)
                plantName = "";


            comboSetHumidityOptimum.setValue((int) humidityOptimum);
            comboSetMoistureOptimum.setValue((int) moistureOptimum);
            comboSetTemperatureOptimum.setValue((int) temperatureOptimum);
            comboDisplayDays.setValue(displayDays);
            setDisplayDays(displayDays);
            tfSetIP.setText(arduinoIp);
            tfSetPlantName.setText(plantName);
            if (stage != null)
                stage.setTitle("EasyGrow - " + plantName);
            setLanguage(language, country);

            if (model != null && model.getPlant() != null) {
                model.getPlant().setIp(arduinoIp);
                model.getPlant().setMoistureOptimum(moistureOptimum);
                model.getPlant().setHumidityOptimum(humidityOptimum);
                model.getPlant().setTemperatureOptimum(temperatureOptimum);
                model.getPlant().setName(plantName);
            }
            propFile.close();
        } catch (FileNotFoundException e) {
            System.out.println(mainPropertiesFile + " not found!");
        } catch (IOException e) {
            System.out.println("Error loading properties.\n" + e.getMessage());
        }
    }

    private void storeMainProperties() {
        try {
            if (model == null || model.getPlant() == null)
                return;
            FileOutputStream propFile = new FileOutputStream(mainPropertiesFile);
            Properties prop = new Properties();
            prop.setProperty("ip", model.getPlant().getIp());
            prop.setProperty("mOptimum", String.valueOf(model.getPlant().getMoistureOptimum()));
            prop.setProperty("hOptimum", String.valueOf(model.getPlant().getHumidityOptimum()));
            prop.setProperty("tOptimum", String.valueOf(model.getPlant().getTemperatureOptimum()));
            prop.setProperty("language", language);
            prop.setProperty("country", country);
            prop.setProperty("plantName", model.getPlant().getName());
            prop.setProperty("displayDays", String.valueOf(displayDays));
            prop.store(propFile, "");
            propFile.close();
        } catch (FileNotFoundException e) {
            System.out.println(mainPropertiesFile + " not found!");
        } catch (IOException e) {
            System.out.println("Error storing properties.\n" + e.getMessage());
        }
    }
    //endregion



    //region ChartDragging
    double chartMouse = -1;
    @FXML
    void onaChartMoistureMousePressed(MouseEvent event) {
        chartMouse = event.getX();
    }
    @FXML
    void onaChartMoistureMouseDragged(MouseEvent event) {
        if (chartMouse == -1)
            chartMouse = event.getX();

        if (chartMouse - event.getX() >= 1) {
            double percent = (chartMouse - event.getX()) / (double)10;
            xAxisaChartMoisture.setLowerBound(xAxisaChartMoisture.getLowerBound() + percent * (double)displayDays / (double)20);
            xAxisaChartMoisture.setUpperBound(xAxisaChartMoisture.getUpperBound() + percent * (double)displayDays / (double)20);
        }
        if (event.getX() - chartMouse >= 1) {
            double percent = (event.getX() - chartMouse) / (double)10;
            xAxisaChartMoisture.setLowerBound(xAxisaChartMoisture.getLowerBound() - percent * (double)displayDays / (double)20);
            xAxisaChartMoisture.setUpperBound(xAxisaChartMoisture.getUpperBound() - percent * (double)displayDays / (double)20);
        }
        chartMouse = event.getX();
    }

    @FXML
    void onaChartTemperatureMousePressed(MouseEvent event) {
        chartMouse = event.getX();
    }

    @FXML
    void onaChartTemperatureMouseDragged(MouseEvent event) {
        if (chartMouse == -1)
            chartMouse = event.getX();

        if (chartMouse - event.getX() >= 1) {
            double percent = (chartMouse - event.getX()) / (double)10;
            xAxisaChartTemperature.setLowerBound(xAxisaChartTemperature.getLowerBound() + percent * (double)displayDays / (double)20);
            xAxisaChartTemperature.setUpperBound(xAxisaChartTemperature.getUpperBound() + percent * (double)displayDays / (double)20);
        }
        if (event.getX() - chartMouse >= 1) {
            double percent = (event.getX() - chartMouse) / (double)10;
            xAxisaChartTemperature.setLowerBound(xAxisaChartTemperature.getLowerBound() - percent * (double)displayDays / (double)20);
            xAxisaChartTemperature.setUpperBound(xAxisaChartTemperature.getUpperBound() - percent * (double)displayDays / (double)20);
        }
        chartMouse = event.getX();
    }

    @FXML
    void onaChartHumidityMousePressed(MouseEvent event) {
        chartMouse = event.getX();
    }

    @FXML
    void onaChartHumidityMouseDragged(MouseEvent event) {
        if (chartMouse == -1)
            chartMouse = event.getX();

        if (chartMouse - event.getX() >= 1) {
            double percent = (chartMouse - event.getX()) / (double)10;
            xAxisaChartHumidity.setLowerBound(xAxisaChartHumidity.getLowerBound() + percent * (double)displayDays / (double)20);
            xAxisaChartHumidity.setUpperBound(xAxisaChartHumidity.getUpperBound() + percent * (double)displayDays / (double)20);
        }
        if (event.getX() - chartMouse >= 1) {
            double percent = (event.getX() - chartMouse) / (double)10;
            xAxisaChartHumidity.setLowerBound(xAxisaChartHumidity.getLowerBound() - percent * (double)displayDays / (double)20);
            xAxisaChartHumidity.setUpperBound(xAxisaChartHumidity.getUpperBound() - percent * (double)displayDays / (double)20);
        }
        chartMouse = event.getX();
    }

    @FXML
    void onlChartOverviewMousePressed(MouseEvent event) {
        chartMouse = event.getX();
    }
    @FXML
    void onlChartOverviewMouseDragged(MouseEvent event) {
        if (chartMouse == -1)
            chartMouse = event.getX();

        if (chartMouse - event.getX() >= 1) {
            double percent = (chartMouse - event.getX()) / (double)10;
            xAxislChartOverview.setLowerBound(xAxislChartOverview.getLowerBound() + percent * (double)displayDays / (double)20);
            xAxislChartOverview.setUpperBound(xAxislChartOverview.getUpperBound() + percent * (double)displayDays / (double)20);
        }
        if (event.getX() - chartMouse >= 1) {
            double percent = (event.getX() - chartMouse) / (double)10;
            xAxislChartOverview.setLowerBound(xAxislChartOverview.getLowerBound() - percent * (double)displayDays / (double)20);
            xAxislChartOverview.setUpperBound(xAxislChartOverview.getUpperBound() - percent * (double)displayDays / (double)20);
        }
        chartMouse = event.getX();
    }
    //endregion



    //region Initialize
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgPichler.setImage(new Image(getClass().getResource("/images/pichler.jpg").toString()));
        imgKrauck.setImage(new Image(getClass().getResource("/images/krauck.jpg").toString()));
        imgPanz.setImage(new Image(getClass().getResource("/images/panz.jpg").toString()));
        imgRiedl.setImage(new Image(getClass().getResource("/images/riedl.jpg").toString()));
        imageViewSettings.setImage(new Image(getClass().getResource("/images/settingsIcon.png").toString()));


        loadMainProperties();
        model = new PlantModel(moistureOptimum, humidityOptimum, temperatureOptimum, plantName, arduinoIp, this);
        refreshCurrentValues();
        refreshCharts();

        for (int i = 0; i <= 10; i++) {
            comboSetMoistureOptimum.getItems().add(i * 10);
            comboSetHumidityOptimum.getItems().add(i * 10);
        }

        for (int i = (int)PlantModel.minTemperature; i <= PlantModel.maxTemperature; i++)
            comboSetTemperatureOptimum.getItems().add(i);

        for (int i = 1; i <= 30; i++)
            comboDisplayDays.getItems().add(i);


        //charts
        lChartOverview.setStyle("CHART_COLOR_1: #" + Integer.toHexString(moistureColor.hashCode())   + ";" +
                "CHART_COLOR_2: #" + Integer.toHexString(temperatureColor.hashCode())   + ";" +
                "CHART_COLOR_3: #" + Integer.toHexString(humidityColor.hashCode())   + ";");

        aChartMoisture.setStyle("CHART_COLOR_1: #" + Integer.toHexString(moistureColor.hashCode())   +
                "; CHART_COLOR_1_TRANS_20: #" + Integer.toHexString(moistureFillColor.hashCode())   + ";");
        aChartTemperature.setStyle("CHART_COLOR_1: #" + Integer.toHexString(temperatureColor.hashCode())   +
                "; CHART_COLOR_1_TRANS_20: #" + Integer.toHexString(temperatureFillColor.hashCode())   + ";");
        aChartHumidity.setStyle("CHART_COLOR_1: #" + Integer.toHexString(humidityColor.hashCode())   +
                "; CHART_COLOR_1_TRANS_20: #" + Integer.toHexString(humidityFillColor.hashCode())   + ";");

        yAxislChartOverview.setLowerBound(PlantModel.minTemperature);
        yAxislChartOverview.setUpperBound(PlantModel.maxMoisture);

        yAxisaChartMoisture.setLowerBound(PlantModel.minMoisture);
        yAxisaChartMoisture.setUpperBound(PlantModel.maxMoisture);

        yAxisaChartHumidity.setLowerBound(PlantModel.minHumidity);
        yAxisaChartHumidity.setUpperBound(PlantModel.maxHumidity);

        yAxisaChartTemperature.setLowerBound(PlantModel.minTemperature);
        yAxisaChartTemperature.setUpperBound(PlantModel.maxTemperature);

        hboxMoisture.heightProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() / 2;
            if (width >= hboxMoisture.getWidth())
                width = hboxMoisture.getWidth();
            canvasCurrentMoisture.resize(width, newValue.doubleValue());
            redrawCurrentValue(canvasCurrentMoisture, moistureColor, currentValueLineWidth, model.getPlant().getCurrentMoisture(),
                    PlantModel.minMoisture, PlantModel.maxMoisture, model.getPlant().getMoistureOptimum());
        });

        hboxMoisture.widthProperty().addListener(((observable, oldValue, newValue) -> {
            double width = hboxMoisture.getHeight() / 2;
            if (width >= hboxMoisture.getWidth())
                width = hboxMoisture.getWidth();
            canvasCurrentMoisture.resize(width, hboxMoisture.getHeight());
            redrawCurrentValue(canvasCurrentMoisture, moistureColor, currentValueLineWidth, model.getPlant().getCurrentMoisture(),
                    PlantModel.minMoisture, PlantModel.maxMoisture, model.getPlant().getMoistureOptimum());
        }));


        hboxTemperature.heightProperty().addListener(((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() / 2;
            if (width >= hboxTemperature.getWidth())
                width = hboxTemperature.getWidth();
            canvasCurrentTemperature.resize(width, newValue.doubleValue());
            redrawCurrentValue(canvasCurrentTemperature, temperatureColor, currentValueLineWidth, model.getPlant().getCurrentTemperature(),
                    PlantModel.minTemperature, PlantModel.maxTemperature, model.getPlant().getTemperatureOptimum());
        }));

        hboxTemperature.widthProperty().addListener(((observable, oldValue, newValue) -> {
            double width = hboxTemperature.getHeight() / 2;
            if (width >= hboxTemperature.getWidth())
                width = hboxTemperature.getWidth();
            canvasCurrentTemperature.resize(width, hboxTemperature.getHeight());
            redrawCurrentValue(canvasCurrentTemperature, temperatureColor, currentValueLineWidth, model.getPlant().getCurrentTemperature(),
                    PlantModel.minTemperature, PlantModel.maxTemperature, model.getPlant().getTemperatureOptimum());
        }));
        hboxHumidity.widthProperty().addListener(((observable, oldValue, newValue) -> {
            double width = hboxHumidity.getHeight() / 2;
            if (width >= hboxHumidity.getWidth())
                width = hboxHumidity.getWidth();
            canvasCurrentHumidity.resize(width, hboxHumidity.getHeight());
            redrawCurrentValue(canvasCurrentHumidity, humidityColor, currentValueLineWidth, model.getPlant().getCurrentHumidity(),
                    PlantModel.minHumidity, PlantModel.maxHumidity, model.getPlant().getHumidityOptimum());
        }));

        hboxHumidity.heightProperty().addListener(((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() / 2;
            if (width >= hboxHumidity.getWidth())
                width = hboxHumidity.getWidth();
            canvasCurrentHumidity.resize(width, newValue.doubleValue());
            redrawCurrentValue(canvasCurrentHumidity, humidityColor, currentValueLineWidth, model.getPlant().getCurrentHumidity(),
                    PlantModel.minHumidity, PlantModel.maxHumidity, model.getPlant().getHumidityOptimum());
        }));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage != null)
            stage.setTitle("EasyGrow - " + plantName);
    }
    //endregion




    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            RefreshType type;
            if (arg instanceof RefreshType)
                type = (RefreshType)arg;
            else
                return;
            if (type == RefreshType.HISTORY)
                refreshCharts();
            if (type == RefreshType.CURRENT)
                refreshCurrentValues();
        });
    }

    private void setLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        ResourceBundle rB = ResourceBundle.getBundle(languagePropertyFile, locale);
        try {
            try {
                labelLanguage.setText(new String(rB.getString("language").getBytes("ISO-8859-1"), "UTF-8"));
                tabHumidityText.setText(new String(rB.getString("humidity").getBytes("ISO-8859-1"), "UTF-8")); //rename to label
                tabMoistureText.setText(new String(rB.getString("moisture").getBytes("ISO-8859-1"), "UTF-8"));
                tabSettingsText.setText(new String(rB.getString("settings").getBytes("ISO-8859-1"), "UTF-8"));
                labelAboutText.setText(new String(rB.getString("about").getBytes("ISO-8859-1"), "UTF-8"));
                //tabSunlightText.setText(rB.getString("sunlight"));
                labelSupportTeacher.setText(new String(rB.getString("supportteacher").getBytes("ISO-8859-1"), "UTF-8"));
                tabTemperatureText.setText(new String(rB.getString("temperature").getBytes("ISO-8859-1"), "UTF-8"));
                labelOCurrentMoisture.setText(new String(rB.getString("moisture").getBytes("ISO-8859-1"), "UTF-8"));
                labelOCurrentHumidity.setText(new String(rB.getString("humidity").getBytes("ISO-8859-1"), "UTF-8"));
                labelOCurrentTemperature.setText(new String(rB.getString("temperature").getBytes("ISO-8859-1"), "UTF-8"));
                labelCurrentHumidity.setText(new String(rB.getString("currenthumidity").getBytes("ISO-8859-1"), "UTF-8"));
                labelCurrentMoisture.setText(new String(rB.getString("currentmoisture").getBytes("ISO-8859-1"), "UTF-8"));
                labelCurrentTemperature.setText(new String(rB.getString("currenttemperature").getBytes("ISO-8859-1"), "UTF-8"));
                //labelCurrentSunlight.setText(rB.getString("currentsunlight"));
                labelSetArduinoIP.setText(new String(rB.getString("setarduinoip").getBytes("ISO-8859-1"), "UTF-8"));
                labelSetMoistureOptimum.setText(new String(rB.getString("setmoistureopt").getBytes("ISO-8859-1"), "UTF-8"));
                labelSetTemperatureOptimum.setText(new String(rB.getString("settemperatureopt").getBytes("ISO-8859-1"), "UTF-8"));
                labelSetHumidityOptimum.setText(new String(rB.getString("sethumidityopt").getBytes("ISO-8859-1"), "UTF-8"));
                btnSetIP.setText(new String(rB.getString("setip").getBytes("ISO-8859-1"), "UTF-8"));
                labelSetPlantName.setText(new String(rB.getString("setplant").getBytes("ISO-8859-1"), "UTF-8"));
                btnSetPlantName.setText(new String(rB.getString("setip").getBytes("ISO-8859-1"), "UTF-8"));
                notAvailableText = (new String(rB.getString("notavailable").getBytes("ISO-8859-1"), "UTF-8"));
                labelOverviewText.setText(new String(rB.getString("overview").getBytes("ISO-8859-1"), "UTF-8"));
                labelDisplayDays.setText(new String(rB.getString("displaydays").getBytes("ISO-8859-1"), "UTF-8"));
                aChartHumidity.setTitle(new String(rB.getString("humidity").getBytes("ISO-8859-1"), "UTF-8"));
                aChartMoisture.setTitle(new String(rB.getString("moisture").getBytes("ISO-8859-1"), "UTF-8"));
                aChartTemperature.setTitle(new String(rB.getString("temperature").getBytes("ISO-8859-1"), "UTF-8"));
                lChartOverview.setTitle(new String(rB.getString("overview").getBytes("ISO-8859-1"), "UTF-8"));
                dayText = new String(rB.getString("days").getBytes("ISO-8859-1"), "UTF-8");
                humidityText = new String(rB.getString("humidity").getBytes("ISO-8859-1"), "UTF-8");
                moistureText = new String(rB.getString("moisture").getBytes("ISO-8859-1"), "UTF-8");
                temperatureText = new String(rB.getString("temperature").getBytes("ISO-8859-1"), "UTF-8");
                moistureAbove = new String(rB.getString("moistAbove").getBytes("ISO-8859-1"), "UTF-8");
                moistureBelow = new String(rB.getString("moistBelow").getBytes("ISO-8859-1"), "UTF-8");
                humidityAbove = new String(rB.getString("humAbove").getBytes("ISO-8859-1"), "UTF-8");
                humidityBelow = new String(rB.getString("humBelow").getBytes("ISO-8859-1"), "UTF-8");
                temperatureAbove = new String(rB.getString("tempAbove").getBytes("ISO-8859-1"), "UTF-8");
                temperatureBelow = new String(rB.getString("tempBelow").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (MissingResourceException ex) {
            System.out.println("Error setting language!");
        }
        if (model != null) {
            refreshCharts();
            refreshCurrentValues();
            refreshxAxis();
            refreshWarnings();
        }
    }

    private void redrawCurrentValue(Canvas canvas, Color color, int lineWidth, float value, float min, float max, float optimum) {
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, width, height);
        graphicsContext.setFill(color);
        graphicsContext.setLineWidth(lineWidth);

        if (Float.isNaN(value) ||  value > max || value < min)
            return;

        graphicsContext.fillArc(lineWidth * 2, height - (width - lineWidth * 4) - lineWidth * 2, width - lineWidth * 4, width - lineWidth * 4,
                360, 360, ArcType.OPEN);

        graphicsContext.fillRect(lineWidth * 2, 0, width - lineWidth * 4, height - width / 2);//fehler


        double percentage = (value - min) / (max - min);
        double power = height - ((height - lineWidth * 2) * percentage);
        graphicsContext.clearRect(0, 0, width, (height - lineWidth * 2) - ((height - lineWidth * 2) *
                ((value - min) / (max - min))));

        graphicsContext.strokeArc(lineWidth, height - (width - lineWidth * 2) - lineWidth, width - lineWidth * 2,
          width - lineWidth * 2, 180, 180, ArcType.OPEN);

        graphicsContext.strokeLine(lineWidth, 0, lineWidth, height - width / 2 - lineWidth + 1);
        graphicsContext.strokeLine(width - lineWidth, 0, width - lineWidth, height - width / 2 - lineWidth + 1);


    }

    private void setDisplayDays(int days) {
        displayDays = days;
        xAxisaChartHumidity.setTickUnit(4);
        xAxisaChartHumidity.setTickLabelFormatter(displayDaysStringConverter);
        xAxisaChartHumidity.setUpperBound(displayDays * 4);
        xAxisaChartHumidity.setLowerBound(1);

        xAxisaChartTemperature.setTickUnit(4);
        xAxisaChartTemperature.setTickLabelFormatter(displayDaysStringConverter);
        xAxisaChartTemperature.setUpperBound(displayDays * 4);
        xAxisaChartTemperature.setLowerBound(1);

        xAxisaChartMoisture.setTickUnit(4);
        xAxisaChartMoisture.setTickLabelFormatter(displayDaysStringConverter);
        xAxisaChartMoisture.setUpperBound(displayDays * 4);
        xAxisaChartMoisture.setLowerBound(1);

        xAxislChartOverview.setTickUnit(4);
        xAxislChartOverview.setTickLabelFormatter(displayDaysStringConverter);
        xAxislChartOverview.setUpperBound(displayDays * 4);
        xAxislChartOverview.setLowerBound(1);
    }

    //region REFRESHING
    private void refreshWarnings() {
        boolean warningB = false;
        WarningType warning = model.getPlant().getMoistureWarning();
        if (warning == WarningType.OPTIMUM)
            circleMoistureWarning.setFill(optimumWarning);
        else if (warning == WarningType.NORMAL)
            circleMoistureWarning.setFill(normalWarning);
        else if (warning == WarningType.CRITICAL_ABOVE || warning == WarningType.CRITICAL_BELOW) {
            circleMoistureWarning.setFill(criticalWarning);
            if (warning == WarningType.CRITICAL_BELOW)
                labelMoistureStatus.setText(moistureBelow);
            else if (warning == WarningType.CRITICAL_ABOVE)
                labelMoistureStatus.setText(moistureAbove);
            warningB = true;
        }
        else
            circleMoistureWarning.setFill(unknownWarning);
        if (!warningB)
            labelMoistureStatus.setText("");
        warningB = false;
        warning = model.getPlant().getHumidityWarning();
        if (warning == WarningType.OPTIMUM)
            circleHumidityWarning.setFill(optimumWarning);
        else if (warning == WarningType.NORMAL)
            circleHumidityWarning.setFill(normalWarning);
        else if (warning == WarningType.CRITICAL_ABOVE || warning == WarningType.CRITICAL_BELOW) {
            circleHumidityWarning.setFill(criticalWarning);
            if (warning == WarningType.CRITICAL_BELOW)
                labelHumidityStatus.setText(humidityBelow);
            else if (warning == WarningType.CRITICAL_ABOVE)
                labelHumidityStatus.setText(humidityAbove);
            warningB = true;
        }
        else
            circleHumidityWarning.setFill(unknownWarning);
        if (!warningB)
            labelHumidityStatus.setText("");
        warningB = false;
        warning = model.getPlant().getTemperatureWarning();
        if (warning == WarningType.OPTIMUM)
            circleTemperatureWarning.setFill(optimumWarning);
        else if (warning == WarningType.NORMAL)
            circleTemperatureWarning.setFill(normalWarning);
        else if (warning == WarningType.CRITICAL_ABOVE || warning == WarningType.CRITICAL_BELOW) {
            circleTemperatureWarning.setFill(criticalWarning);
            if (warning == WarningType.CRITICAL_BELOW)
                labelTemperatureStatus.setText(temperatureBelow);
            else if (warning == WarningType.CRITICAL_ABOVE)
                labelTemperatureStatus.setText(temperatureAbove);
            warningB = true;
        }
        else
            circleTemperatureWarning.setFill(unknownWarning);
        if (!warningB)
            labelTemperatureStatus.setText("");
        circleOHumidityWarning.setFill(circleHumidityWarning.getFill());
        circleOMoistureWarning.setFill(circleMoistureWarning.getFill());
        circleOTemperatureWarning.setFill(circleTemperatureWarning.getFill());

        labelOMoistureStatus.setText(labelMoistureStatus.getText());
        labelOHumidityStatus.setText(labelHumidityStatus.getText());
        labelOTemperatureStatus.setText(labelTemperatureStatus.getText());
    }

    private void refreshCharts() {
        ObservableList<XYChart.Data<Long, Double>> list;

        lChartOverview.setAnimated(false);
        lChartOverview.setCreateSymbols(false);
        lChartOverview.getData().clear();

        XYChart.Series<Long, Double> seriesMoist;
        XYChart.Series<Long, Double> seriesHum;
        XYChart.Series<Long, Double> seriesTemp;
        XYChart.Series<Long, Double> tmp = new XYChart.Series<>();

        seriesMoist = new XYChart.Series<Long, Double>();
        aChartMoisture.getData().clear();
        List<XYChart.Data<Long, Double>> li = model.getPlant().getMoistures();
        list =  FXCollections.observableArrayList();
        for (XYChart.Data<Long, Double> d : li)
            list.add(d);
        seriesMoist.setData(list);
        seriesMoist.setName(moistureText);
        tmp.setData(list);
        tmp.setName(moistureText);
        aChartMoisture.setAnimated(false);
        aChartMoisture.getData().add(seriesMoist);
        lChartOverview.getData().add(tmp);

        lChartOverview.setCreateSymbols(false);

        seriesTemp = new XYChart.Series<Long, Double>();
        aChartTemperature.getData().clear();
        li = model.getPlant().getTemperatures();
        list =  FXCollections.observableArrayList();
        for (XYChart.Data<Long, Double> d : li)
            list.add(d);
        seriesTemp.setData(list);
        seriesTemp.setName(temperatureText);
        tmp = new XYChart.Series<>();
        tmp.setData(list);
        tmp.setName(temperatureText);
        System.out.println("size: " + li.size());
        aChartTemperature.setAnimated(false);
        aChartTemperature.getData().add(seriesTemp);
        lChartOverview.getData().add(tmp);

        lChartOverview.setCreateSymbols(false);

        seriesHum = new XYChart.Series<Long, Double>();
        aChartHumidity.getData().clear();
        li = model.getPlant().getHumidities();
        list =  FXCollections.observableArrayList();
        for (XYChart.Data<Long, Double> d : li)
            list.add(d);
        seriesHum.setData(list);
        seriesHum.setName(humidityText);
        tmp = new XYChart.Series<>();
        tmp.setData(list);
        tmp.setName(humidityText);
        System.out.println("size: " + li.size());
        aChartHumidity.setAnimated(false);
        aChartHumidity.getData().add(seriesHum);
        lChartOverview.getData().add(tmp);

        lChartOverview.setCreateSymbols(false);
    }

    private void refreshCurrentValues() {
        if (model.getPlant().getCurrentMoisture() >= 0 && !Float.isNaN(model.getPlant().getCurrentMoisture()))
            labelCurrentMoisturePercent.setText(model.getPlant().getCurrentMoisture() + "%");
        else
            labelCurrentMoisturePercent.setText(notAvailableText);
        if (!Float.isNaN(model.getPlant().getCurrentTemperature()))
            labelCurrentTemperatureCelsius.setText(model.getPlant().getCurrentTemperature() + "°C");
        else
            labelCurrentTemperatureCelsius.setText(notAvailableText);
        if (model.getPlant().getCurrentHumidity() >= 0 && !Float.isNaN(model.getPlant().getCurrentHumidity()))
            labelCurrentHumidityPercent.setText(model.getPlant().getCurrentHumidity() + "%");
        else
            labelCurrentHumidityPercent.setText(notAvailableText);

        labelOCurrentHumidityPercent.setText(labelCurrentHumidityPercent.getText());
        labelOCurrentTemperatureCelsius.setText(labelCurrentTemperatureCelsius.getText());
        labelOCurrentMoisturePercent.setText(labelCurrentMoisturePercent.getText());

        redrawCurrentValue(canvasCurrentHumidity, humidityColor, currentValueLineWidth, model.getPlant().getCurrentHumidity(),
                PlantModel.minHumidity, PlantModel.maxHumidity, model.getPlant().getHumidityOptimum());

        redrawCurrentValue(canvasCurrentTemperature, temperatureColor, currentValueLineWidth, model.getPlant().getCurrentTemperature(),
                PlantModel.minTemperature, PlantModel.maxTemperature, model.getPlant().getTemperatureOptimum());

        redrawCurrentValue(canvasCurrentMoisture, moistureColor, currentValueLineWidth, model.getPlant().getCurrentMoisture(),
                PlantModel.minMoisture, PlantModel.maxMoisture, model.getPlant().getMoistureOptimum());

        refreshWarnings();
    }

    private void refreshxAxis() {
        xAxisaChartMoisture.setTickUnit(3);
        xAxisaChartMoisture.setTickUnit(4);

        xAxisaChartTemperature.setTickUnit(3);
        xAxisaChartTemperature.setTickUnit(4);

        xAxisaChartHumidity.setTickUnit(3);
        xAxisaChartHumidity.setTickUnit(4);

        xAxislChartOverview.setTickUnit(3);
        xAxislChartOverview.setTickUnit(4);
    }
    //endregion
}

