package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.annotations.ValueProcessor;
import main.data.Step;
import main.dialog.CreateStepController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovesController {

    @FXML
    private Parent parent;

    @FXML
    private TabPane allTabs;

    @FXML
    private Slider counts;

    private final List<Step> stepList = new ArrayList<>();


    @FXML
    private void addNewStep() throws IOException, IllegalAccessException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/createStepDialog.fxml"));
        parent = fxmlLoader.load();
        CreateStepController newStep = fxmlLoader.getController();
        Scene scene = new Scene(parent, 391, 476);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons()
                .add(new Image(getClass().getResourceAsStream("/main/resources/robot.png")));
        stage.setTitle("Create new step");
        stage.setScene(scene);
        ValueProcessor.fillValues(newStep);
        stage.showAndWait();

        if (newStep.getStep() != null) {
            stepList.add(newStep.getStep());
            addNewTab(newStep.getStep());
        }
    }

    private void addNewTab(Step step) throws IOException {
        Tab tab = new Tab("Step " + (stepList.size()));

        ContentTabController contentTabController = new ContentTabController("suck", "dick");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/tabContent.fxml"));
        fxmlLoader.setRoot(tab);
        fxmlLoader.setController(contentTabController);
        fxmlLoader.load();

        String actions = String.join(" ", step.getActions());

        contentTabController.setDescription(step.getDescription());
        contentTabController.setActions(actions);

        allTabs.getTabs().add(tab);
    }

    @FXML
    private void startRobot() throws AWTException {
        Robot robot = new Robot();
        for (int i = 0; i < counts.getValue(); ++i) {
            for (Step currStep : stepList) {
                if (currStep.getPoint().getX() != null) {
                    robot.mouseMove((int) currStep.getPoint().getX().longValue(), (int) currStep.getPoint().getY().longValue());
                    switch (currStep.getType()) {
                        case CLICK:
                            robot.mousePress(InputEvent.BUTTON1_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_MASK);
                            break;
                        case DOUBLE_CLICK:
                            //TODO: find why double ckick doesn't work
                            //Получается только через 2 одинарных нажатия открыть какой-то файл
                            break;
                    }
                } else {
                    //TODO: make in create step controller blocking for more than 2 keys
                    //TODO: check and implement here some special events, like a shift + smth or ctrl + smth.
                    //TODO: check why space can't be pressed
                    if (currStep.getActions().size() == 2) {
                        KeyStroke first = KeyStroke.getKeyStroke(currStep.getActions().get(0));
                        KeyStroke second = KeyStroke.getKeyStroke(currStep.getActions().get(0));
                        robot.keyPress(first.getKeyCode());
                        robot.keyPress(second.getKeyCode());
                        robot.keyRelease(first.getKeyCode());
                        robot.keyRelease(second.getKeyCode());
                    } else {
                        for (String key : currStep.getActions()) {
                            KeyStroke btn = KeyStroke.getKeyStroke(key);
                            robot.keyPress(btn.getKeyCode());
                            robot.keyRelease(btn.getKeyCode());
                        }
                    }
                }
            }
        }
    }
}
