package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class WelcomeSceneController {

    private Stage stage;
    private Parent parent;

    @FXML
    private Button createButton;

    @FXML
    private void openButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("No option");
        alert.setContentText("Ooops, we have not implemented it yet!");

        alert.showAndWait();
    }

    @FXML
    private void newButtonClicked() throws IOException {
        URL newScriptURL = getClass().getResource("/main/resources/allMoves.fxml");
        parent = FXMLLoader.load(newScriptURL);
        stage = (Stage) createButton.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

}
