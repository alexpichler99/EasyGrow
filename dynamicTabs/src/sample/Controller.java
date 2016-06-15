/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */
package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class Controller {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id="tpTabPane"
    private TabPane tpTabPane; // Value injected by FXMLLoader

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnNewTab"
    private Button btnNewTab; // Value injected by FXMLLoader

    @FXML // fx:id="tfNewTab"
    private TextField tfNewTab; // Value injected by FXMLLoader

    @FXML
    void handleNewTabButton(ActionEvent event) {
        Tab newTab = new Tab(tfNewTab.getText());
        tpTabPane.getTabs().add(newTab);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnNewTab != null : "fx:id=\"btnNewTab\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfNewTab != null : "fx:id=\"tfNewTab\" was not injected: check your FXML file 'sample.fxml'.";
    }
}
