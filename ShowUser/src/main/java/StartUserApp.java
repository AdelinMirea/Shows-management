import gui.controller.LogInController;
import gui.controller.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.ShowServerProxy;

import java.io.FileInputStream;
import java.util.Properties;

public class StartUserApp extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("LogIn");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/login.fxml"));
        Parent root = loader.load();
        LogInController controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        Properties properties = new Properties();
        properties.load(new FileInputStream("ShowUser/src/main/resources/showclient.properties"));

        String host = properties.getProperty("show.client.host");
        int port = Integer.parseInt(properties.getProperty("show.client.port"));
        ShowServerProxy server = new ShowServerProxy(host, port);
        controller.setStage(primaryStage);
        controller.setServer(server);



        FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("../resources/mainScreen.fxml"));
        Parent sroot = loaderMain.load();
        MenuController controllerMain = loaderMain.getController();
        controller.setServer(server);
        controller.setController(controllerMain);
        controller.setParent(sroot);
    }

}
