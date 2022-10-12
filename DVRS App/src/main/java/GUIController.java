import jade.wrapper.StaleProxyException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URL;
import java.util.Optional;
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

    @FXML
    public ObservableList<Object> agentsObjectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        agentCreateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    createAgentWindow();
                } catch (StaleProxyException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void createAgentWindow() throws StaleProxyException {
        //default agent capacity
        int capacity = 15;
        boolean hasCapacity = false;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Delivery Agent");
        dialog.setHeaderText("New Delivery Agent");
        dialog.setContentText("Max Capacity: ");

        while (!hasCapacity) {
            Optional<String> response = dialog.showAndWait();
            if (response.isPresent()) {
                String newCapacity = response.get();
                if (newCapacity.isEmpty()) {
                    displayModal(Alert.AlertType.ERROR, "Error - Agent Capacity", "Please enter the max capacity for the new Delivery Agent.");
                } else {
                    try {
                        capacity = Integer.parseInt(newCapacity);
                        hasCapacity = true;
                    } catch (NumberFormatException e) {
                        displayModal(Alert.AlertType.ERROR, "Error - Agent Capacity", "Please enter the max capacity for the new Delivery Agent.");
                    }
                }
            }
        }

        MainFXClass.addDeliveryAgent(capacity);
        populateAgentslist();

        displayModal(Alert.AlertType.CONFIRMATION, "SUCCESS - Delivery Agent", "Successfully creating Delivery Agent with " + capacity + " max capacity.");
    }

    public void populateAgentslist() {
        agentsList.setItems(FXCollections.observableArrayList(agentsObjectList));
    }

    private void displayModal(Alert.AlertType type, String title, String description) {
        Alert alertType = new Alert(type);
        alertType.setTitle(title);
        alertType.setContentText(description);
        alertType.showAndWait();
    }

    public void alocatePosition(Circle circle) {
        mapPane.getChildren().add(circle);
    }

    public void registerNode(Circle circle, String id) {
        circle.setPickOnBounds(false);

        Text text = new Text(id);
        text.setX(circle.getCenterX() - text.getBoundsInLocal().getWidth() / 2);
        text.setY(circle.getCenterY() - text.getBoundsInLocal().getHeight() / 2);

        mapPane.getChildren().add(circle);
    }
}
