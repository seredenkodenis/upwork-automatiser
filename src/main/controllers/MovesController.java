package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.data.Step;
import main.dialog.CreateStepController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovesController {

    @FXML
    private Parent parent;

    @FXML
    private TabPane allTabs;

    @FXML
    private Slider counts;

    private final List<Step> stepList = new ArrayList<>();


    @FXML
    private void addNewStep() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/createStepDialog.fxml"));
        parent = fxmlLoader.load();
        CreateStepController newStep = fxmlLoader.getController();
        Scene scene = new Scene(parent, 391, 476);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        if (newStep.getStep() == null) {
            stage.setScene(scene);
            stage.showAndWait();
        } else {
            stepList.add(newStep.getStep());
            addNewTab(newStep.getStep());
        }
    }

    private void addNewTab(Step step) throws IOException {
        Tab tab = new Tab("Step " + (stepList.size()));

        ContentTabController contentTabController = new ContentTabController("suck", "dick");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/tabContent.fxml"));
        fxmlLoader.setRoot(tab);
        fxmlLoader.setController(contentTabController);
        fxmlLoader.load();

        String actions = step.getActions().stream().map(KeyCode::getName).collect(Collectors.joining(" "));

        contentTabController.setDescription(step.getDescription());
        contentTabController.setActions(actions);

        allTabs.getTabs().add(tab);
    }

    @FXML
    private void startRobot() {
        Robot robot = new Robot();
        for (int i = 0; i < counts.getValue(); ++i) {
            for (Step currStep : stepList) {
                if (currStep.getPoint().getX() != null) {
                    robot.mouseMove(currStep.getPoint().getX(), currStep.getPoint().getY());
                    switch (currStep.getType()) {
                        case CLICK:
                            robot.mouseClick(MouseButton.PRIMARY);
                            robot.mouseRelease(MouseButton.PRIMARY);
                            break;
                        case DOUBLE_CLICK:
                            //TODO: find why double ckick doesn't work
                            //Получается только через 2 одинарных нажатия открыть какой-то файл
                            break;
                    }
                } else {
                    //TODO: make in create step controller blocking for more than 2 keys
                    if (currStep.getActions().size() == 2) {
                        robot.keyPress(currStep.getActions().get(0));
                        robot.keyPress(currStep.getActions().get(1));
                        robot.keyRelease(currStep.getActions().get(0));
                        robot.keyRelease(currStep.getActions().get(1));
                    } else {
                        for (KeyCode key : currStep.getActions()) {
                            robot.keyPress(key);
                            robot.keyRelease(key);
                        }
                    }
                }
            }
        }
    }
}
