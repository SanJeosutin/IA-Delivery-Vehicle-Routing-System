import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    public MainFX MainFXClass;

    @FXML
    public Button agentCreateButton;

    @FXML
    public Button agentDeleteButton;

    @FXML
    public Button parcelCreateButton;

    @FXML
    public Button parcelDeleteButton;

    @FXML
    public Button routeShowButton;

    @FXML
    public Button routeHideButton;

    @FXML
    public Button NodeCreateButton;

    @FXML
    public Button NodeDeleteButton;

    @FXML
    public Button programStartButton;

    @FXML
    public Button programPauseButton;

    @FXML
    public ListView agentsList;

    @FXML
    public ListView parcelsList;

    @FXML
    private Pane mapPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
