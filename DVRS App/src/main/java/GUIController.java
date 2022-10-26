import jade.tools.sniffer.AgentList;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

    @FXML
    public ObservableList<Parcel> parcelObjectList = FXCollections.observableArrayList();

    @FXML
    public void programStartButton() throws StaleProxyException {
        MainFXClass.playScenario();
    }
    private Map<Circle, Text> circleReference = new HashMap<>();

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

        agentDeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    removeAgentWindow();
                } catch (ControllerException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        parcelCreateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Creating Parcel...");
            }
        });

        parcelDeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Deleting Parcel...");
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
                if (newCapacity.isBlank()) {
                    displayModal(Alert.AlertType.ERROR, "Error - Agent Capacity", "Please enter the max capacity for the new Delivery Agent.");
                } else {
                    try {
                        capacity = Integer.parseInt(newCapacity);
                        hasCapacity = true;
                    } catch (NumberFormatException e) {
                        displayModal(Alert.AlertType.ERROR, "Error - Agent Capacity", "Please enter the max capacity for the new Delivery Agent.");
                    }
                }
            } else {
                return;
            }
        }

        MainFXClass.addDeliveryAgent(capacity);
        populateAgentslist();

        displayModal(Alert.AlertType.CONFIRMATION, "SUCCESS - Delivery Agent", "Successfully creating Delivery Agent with " + capacity + " max capacity.");
    }

    public void removeAgentWindow() throws ControllerException {
        int index = agentsList.getSelectionModel().getSelectedIndex();

        if (index < 0 || index >= agentsObjectList.size()) {
            displayModal(Alert.AlertType.WARNING, "WARNING - Delivery Agent", "Please select Agent First before removing them.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delivery Agent Deletion");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to remove DA" + index + "1?");

        Optional<ButtonType> output = confirm.showAndWait();

        if (output.isPresent() && output.get() == ButtonType.OK) {
            MainFXClass.removeDeliveryAgent(index);
            agentsObjectList.remove(index);
            populateAgentslist();
            displayModal(Alert.AlertType.INFORMATION, "INFO - Delivery Agent Deletion", "Successfully deleted agent DA" + index + "1.");
        }
    }

    public void populateAgentslist() {
        agentsList.setItems(FXCollections.observableArrayList(agentsObjectList));
    }
    public void populateParcelslist() {
        parcelsList.setItems(FXCollections.observableArrayList(parcelObjectList));
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

        circleReference.put(circle, text);
    }

    public void deregisterNode(String reference, int i) {
        for (Circle circle : circleReference.keySet()) {
            if (circleReference.get(circle).getText().equals(reference)) {
                mapPane.getChildren().remove(circle);
            }
        }
    }

    public void registerParcel(Parcel parcel) {
        parcelObjectList.add(parcel);
    }

    public void deregisterParcel(Parcel parcel) {
        parcelObjectList.remove(parcel);
    }

    public void refreshGUI() {
        agentsList.setItems(FXCollections.observableArrayList(agentsObjectList));
    }

}
