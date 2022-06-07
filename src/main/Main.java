package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Start of logging");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/main/resources/welcomeScene.fxml");
        loader.setLocation(xmlUrl);

        Parent root = loader.load();
        primaryStage.getIcons()
                .add(new Image(getClass().getResourceAsStream("/main/resources/robot.png")));
        primaryStage.setTitle("Screen cheater");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
