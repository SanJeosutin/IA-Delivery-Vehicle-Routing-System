import jade.tools.sniffer.AgentList;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.swing.text.Highlighter;
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
    private Circle nodeSelect = null;
    private Paint nodeSelectColour = null;

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

        mapPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                EventTarget target = mouseEvent.getTarget();

                if (target != mapPane) {
                    Circle newNodeTarget = (Circle) target;
                    if (circleReference.containsKey(newNodeTarget)) {
                        getClickedNode(newNodeTarget);
                    }
                } else {
                    addNodeWindow(mouseEvent);
                }
            }
        });

        parcelsList.setItems(parcelObjectList);
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

    public void addNodeWindow(MouseEvent mouseEvent) {
        double posX = mouseEvent.getX();
        double posY = mouseEvent.getY();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Node");
        dialog.setHeaderText("New Node");
        dialog.setContentText("Node Name: ");

        boolean complete = false;

        while (!complete) {
            Optional<String> result = dialog.showAndWait();

            String nodeName = result.get();

            if (nodeName.isBlank()) {
                displayModal(Alert.AlertType.ERROR, "ERROR - Node", "Please enter new node name.");
            } else if (MainFXClass.nodeExist(nodeName)) {
                displayModal(Alert.AlertType.ERROR, "ERROR - Node", "Name already registered. Try a new one.");
            } else {
                MainFXClass.addNode(nodeName, new Position(posX, posY));
                displayModal(Alert.AlertType.INFORMATION, "SUCCESS - Node", "Successfully creating Node with name " + nodeName + ".");

                complete = true;
            }
        }
    }

    public void removeNodeWindow() throws StaleProxyException {
        if (nodeSelect == null) {
            displayModal(Alert.AlertType.WARNING, "WARNING - Node", "Please select a node from the map.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Node");
        confirm.setContentText("Do you want to delete selected node?");

        Optional<ButtonType> output = confirm.showAndWait();
        if (output.isPresent() && output.get() == ButtonType.OK) {
            //removing all parcel within the selected node

            for (int i = 0; i < parcelObjectList.size(); ) {
                Parcel parcel = parcelObjectList.get(i);

                if (parcel.getDestination().equals(circleReference.get(nodeSelect))) {
                    MainFXClass.removeParcel(parcel);
                    parcelObjectList.remove(parcel);
                } else {
                    // go to the next one if parcel is not deleted
                    i++;
                }
            }

            Text removeText = circleReference.get(nodeSelect);
            MainFXClass.removeNode(removeText.getText());
            circleReference.remove(nodeSelect);

            mapPane.getChildren().removeAll(nodeSelect, removeText);

            displayModal(Alert.AlertType.INFORMATION, "SUCCESS - Node", "Successfully remove selected node.");
            parcelsList.setItems(parcelObjectList);
            populateAgentslist();
        }
    }

    @FXML
    public void addParcelWindow() throws StaleProxyException {
        if (nodeSelect == null) {
            displayModal(Alert.AlertType.WARNING, "WARNING", "Please select a destination node first");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Parcel");
        dialog.setHeaderText("New Parcel");
        dialog.setContentText("Parcel Weight:");
        int weight = 0;
        boolean hasWeight = false;

        while (!hasWeight) {
            Optional<String> response = dialog.showAndWait();
            if (response.isPresent()) {
                String newWeight = response.get();
                if (newWeight.isBlank()) {
                    displayModal(Alert.AlertType.ERROR, "ERROR - New Parcel", "Please enter a weight");
                } else {
                    try {
                        weight = Integer.parseInt(newWeight);
                        hasWeight = true;
                    } catch (NumberFormatException e) {
                        displayModal(Alert.AlertType.ERROR, "ERROR - New Parcel", "Please enter a number for weight");
                    }
                }
            } else {
                return;
            }
        }

        dialog = new TextInputDialog();
        dialog.setTitle("Create New Parcel");
        dialog.setHeaderText("New Parcel");
        dialog.setContentText("Parcel Name:");

        String parcelName = "";
        boolean hasName = false;

        while (!hasName) {
            Optional<String> response = dialog.showAndWait();
            if (response.isPresent()) {
                String newName = response.get();
                if (newName.isBlank()) {
                    displayModal(Alert.AlertType.ERROR, "ERROR - New Parcel", "Please enter parcel name");
                } else {
                    parcelName = newName;
                    hasName = true;
                }
            } else {
                return;
            }
        }

        Parcel parcel = new Parcel(weight, circleReference.get(nodeSelect).getText(), parcelName);
        MainFXClass.addParcel(parcel);

        displayModal(Alert.AlertType.INFORMATION, "SUCCESS - New Parcel", parcelName + " with weight of " + weight + ". Has been successfully created.");
    }

    @FXML
    public void removeParcelWindow() throws StaleProxyException {
        Parcel selectedParcel = (Parcel) parcelsList.getSelectionModel().getSelectedItem();
        if (selectedParcel == null) {
            displayModal(Alert.AlertType.WARNING, "WARNING - Remove Parcel", "Please select a parcel first");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Removing Parcel");
        confirmation.setContentText("Are you sure you want to delete this parcel?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MainFXClass.removeParcel(selectedParcel);

            displayModal(Alert.AlertType.INFORMATION, "SUCCESS - Remove Parcel", "Parcel has been removed.");
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
        text.setX(circle.getCenterX() + text.getBoundsInLocal().getWidth() / 2);
        text.setY(circle.getCenterY() + text.getBoundsInLocal().getHeight() / 2);

        mapPane.getChildren().add(circle);
        mapPane.getChildren().add(text);

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

    public void getClickedNode(Circle circle) {
        if (nodeSelect != null) {
            nodeSelect.setFill(nodeSelectColour);
            circleReference.get(nodeSelect).setFill(Color.BLACK);
        }

        nodeSelect = circle;
        nodeSelectColour = circle.getFill();

        nodeSelect.setFill(Color.GREEN);
        circleReference.get(nodeSelect).setFill(Color.GREEN);
    }

    public void refreshGUI() {
        agentsList.setItems(FXCollections.observableArrayList(agentsObjectList));
    }

}
