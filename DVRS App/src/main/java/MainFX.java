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

    private List<Node> nodes = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Load GUI from FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainFX.fxml"));
        Parent root = loader.load();
        guiController = loader.getController();
        primaryStage.setTitle("Delivery Vehicle Routing System");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}