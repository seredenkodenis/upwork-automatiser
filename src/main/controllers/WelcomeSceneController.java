package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.data.Step;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class WelcomeSceneController {

    @FXML
    private Button createButton;

    @FXML
    private void openButtonClicked() throws IOException, ClassNotFoundException {
        List<Step> stepList = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/allMoves.fxml"));

        Stage currentStage = (Stage) createButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(currentStage);
        if (file != null) {
            stepList = openSaveFile(file);
        }

        MovesController movesController = new MovesController();
        movesController.setStepList(stepList);
        loader.setController(movesController);

        Parent parent = loader.load();

        currentStage.setScene(new Scene(parent));
        currentStage.show();
    }

    @FXML
    private void newButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/allMoves.fxml"));

        MovesController movesController = new MovesController();
        loader.setController(movesController);

        Parent parent = loader.load();

        Stage currentStage = (Stage) createButton.getScene().getWindow();
        currentStage.setScene(new Scene(parent));
        currentStage.show();
    }

    private List<Step> openSaveFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        ArrayList<Step> p2 = (ArrayList<Step>) objectInputStream.readObject();

        objectInputStream.close();

        return p2;
    }
}
