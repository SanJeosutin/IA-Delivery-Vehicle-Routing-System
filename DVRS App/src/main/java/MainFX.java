import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFX extends Application {
    private GUIController guiController;
    private AgentController agentController;
    private ContainerController containerController;

    private List<Node> nodes = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();

    private int initPosX = 350;
    private int initPosY = 225;

    //could generate colours instead of hard coding it :/
    private Color[] agentColors = {Color.DARKGREEN, Color.DARKBLUE, Color.DARKORANGE, Color.DARKRED, Color.DARKCYAN, Color.DARKGOLDENROD, Color.DARKKHAKI, Color.DARKMAGENTA, Color.DARKORANGE};

    private int colorPos = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupScene(primaryStage);
        killProgram(primaryStage);
        setupAgents();

        readData();
        guiController.populateAgentslist();
        guiController.populateParcelslist();
        guiController.MainFXClass = this;
    }

    public static void main (String[] args) {
        launch(args);
    }

    public void playScenario() throws StaleProxyException {
        List<String> DAs = new ArrayList<>();

        for (Object object : guiController.agentsObjectList) {
            DAs.add(object.toString().substring(0, object.toString().indexOf("@")));
        }

        MRAInterface mraInterface = agentController.getO2AInterface(MRAInterface.class);
        mraInterface.startRoute(DAs);
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

        //placed warehouse (MRA) route pos
        Circle warehouse = new Circle(initPosX, initPosY, 12, Color.BLUE);
        guiController.alocatePosition(warehouse);
    }

    public void addDeliveryAgent(int capacity) throws StaleProxyException {
        Circle drawAgent = new Circle(initPosX, initPosY, 6, agentColors[colorPos]);
        colorPos++;

        String drawAgentRef = "DA" + guiController.agentsObjectList.size() + 1;
        guiController.registerNode(drawAgent, drawAgentRef);

        AgentController newDeliveryAgent = containerController.createNewAgent(drawAgentRef, DeliveryAgent.class.getName(), new Object[]{drawAgent, capacity});
        newDeliveryAgent.start();
        guiController.agentsObjectList.add(newDeliveryAgent.getName());
    }

    public void removeDeliveryAgent(int id) throws ControllerException {
        String agentName = guiController.agentsObjectList.get(id).toString();
        guiController.deregisterNode(agentName.substring(0), agentName.indexOf("@"));
        guiController.refreshGUI();
        containerController.getAgent(agentName.substring(0, agentName.indexOf("@"))).kill();
    }

    public void addParcel(Parcel parcel) throws StaleProxyException {
        parcels.add(parcel);
        guiController.registerParcel(parcel);

        agentController.getO2AInterface(MRAInterface.class).addParcel(parcel);
    }

    public void removeParcel(Parcel parcel) throws StaleProxyException {
        parcels.remove(parcel);
        guiController.deregisterParcel(parcel);

        agentController.getO2AInterface(MRAInterface.class).removeParcel(parcel);
    }

    public void addNode(String id, Position pos) {
        Node node = new Node(id, pos);
        guiController.registerNode(node.create(), id);
        nodes.add(node);

        try {
            agentController.getO2AInterface(MRAInterface.class).newNode(node);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void removeNode(String id) throws StaleProxyException {
        for (Node node : nodes) {
            if (node.amI(id)) {
                nodes.remove(node);
                agentController.getO2AInterface(MRAInterface.class).removeNode(node);
            }
            break;
        }
    }

    public boolean nodeExist(String name) {
        return nodes.stream().anyMatch(node -> node.amI(name));
    }
    private void readData() throws IOException, StaleProxyException {
        CSVReader reader = new CSVReader();
        List<List<String>> data = reader.readFile("G:\\Uni Stuff\\Semester 6\\Intelligent System\\Assignment\\IA-Delivery-Vehicle-Routing-System\\DVRS App\\src\\main\\resources\\data\\test.txt");

        for (List<String> eachData : data) {
            String type = eachData.get(0);

            switch (type) {
                case "AGENT":
                    for (int i = 0; i < Integer.parseInt(eachData.get(1)); i++) {
                        addDeliveryAgent(Integer.parseInt(eachData.get(i + 1)));
                    }
                    break;

                case "NODE":
                    Position nodePos = new Position(Double.parseDouble(eachData.get(1)), Double.parseDouble(eachData.get(2)));
                    addNode(eachData.get(3), nodePos);
                    break;

                case "PARCEL":
                    Parcel parcel = new Parcel(Integer.parseInt(eachData.get(1)), eachData.get(2), eachData.get(3));
                    addParcel(parcel);
                    break;
            }

        }

    }

}