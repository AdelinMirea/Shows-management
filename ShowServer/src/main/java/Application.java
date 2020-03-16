//import gui.controller.LogInController;
//import javafx.application.Platform;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import service.Service;
//
//public class Application extends javafx.application.Application {
//    public static void main(String[] args) {
////        RepoShowDB repository = new RepoShowDB();
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        primaryStage.setTitle("Events");
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/login.fxml"));
//        Parent root = loader.load();
//        LogInController controller = loader.getController();
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
////        controller.setService(getFactory().getBean(Service.class));
////        controller.setStage(primaryStage);
////
////        primaryStage.setOnCloseRequest(t -> {
////            controller.onClose();
////            Platform.exit();
////            System.exit(0);
////        }
////        );
//
////        ApplicationContext ctx = new AnnotationConfigApplicationContext(ConnectionFactory.class);
//    }
//
//    static ApplicationContext getFactory(){
//        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:../resources/spring-events.xml");
//        return factory;
//    }
//}
