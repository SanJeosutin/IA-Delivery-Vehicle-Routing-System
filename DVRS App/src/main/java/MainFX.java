import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFX extends Application {
    private GUIController guiController;
    private AgentController agentController;
    private ContainerController containerController;

    private List<Node> nodes = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();

    private int initPosX = 100;
    private int initPosY = 100;

    //could generate colours instead of hard coding it :/
    private Color[] agentColors = {
            Color.DARKGREEN,
            Color.DARKBLUE,
            Color.DARKORANGE,
            Color.DARKRED,
            Color.DARKCYAN,
            Color.DARKGOLDENROD,
            Color.DARKKHAKI,
            Color.DARKMAGENTA,
            Color.DARKORANGE
    };

    private int colorPos = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupScene(primaryStage);
        killProgram(primaryStage);
        setupAgents();
    }

    private void killProgram(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void setupScene(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainFX.fxml"));
        Parent root = loader.load();
        guiController = loader.getController();
        primaryStage.setTitle("Delivery Vehicle Routing System");

        //setting-up the 'scene'
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupAgents() throws StaleProxyException {


        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(null, 8888, null);
        containerController = runtime.createMainContainer(profile);
        agentController = containerController.createNewAgent("MRA", MasterRoutingAgent.class.getName(), new Object[0]);
        agentController.start();

        Circle warehouse = new Circle(initPosX, initPosY, 12, Color.BLUE);
        guiController.alocatePosition(warehouse);
    }

    private void addDeliveryAgent(int capacity) {
        Circle drawAgent = new Circle(initPosX, initPosY, 6, agentColors[colorPos]);
        colorPos++;
    }

    private void readData() throws IOException {
        CSVReader reader = new CSVReader();
        List<List<String>> data = reader.readFile("/data/test.txt");

        for (List<String> eachData : data) {
            String type = eachData.get(0);

            switch (type) {
                case "AGENT":
                    for (int i = 0; i < Integer.parseInt(eachData.get(1)); i++) {
                        addDeliveryAgent(Integer.parseInt(eachData.get(i + 1)));
                    }
                    break;
            }

        }

    }

    private void initialiseMRA() {

    }
}