import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.ArrayList;
import java.util.List;

public class MainFX extends Application {
    private GUIController guiController;
    private AgentController agentController;
    private ContainerController containerController;

    private List<Node> nodes = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //load GUI from FXML
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

        //booting JADE
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(null, 8888, null);
        containerController = runtime.createMainContainer(profile);
        agentController = containerController.createNewAgent("MRA", MasterRoutingAgent.class.getName(), new Object[0]);
        agentController.start();
    }
}