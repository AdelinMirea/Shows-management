package gui.controller;


import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import show.services.IShowObserver;
import show.services.IShowServer;

import java.io.IOException;

public class LogInController {

    private IShowServer server;
    private MenuController controller;
    private Parent mainSceneParent;

    private Stage stage;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void onLogInPressed(){
        boolean answer = server.logIn(new User(username.getText(), password.getText()), controller);
        if(answer){
            try {
                createMainStage();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("A aparut o eroare");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Acces respins");
            alert.show();
        }
    }

    public void setServer(IShowServer server){
        this.server = server;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setController(MenuController controller){
        this.controller = controller;
    }

    public void setParent(Parent parent){
        this.mainSceneParent = parent;
    }

    private void createMainStage() throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("LogIn");

        Scene scene = new Scene(mainSceneParent);
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.setThisStage(primaryStage);
        controller.setServer(server);
        controller.setParent(mainSceneParent);
        stage.close();

    }

}
