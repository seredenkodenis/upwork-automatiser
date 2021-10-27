package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.annotations.ValueProcessor;
import main.data.ActionType;
import main.data.Step;
import main.dialog.CreateStepController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovesController implements Initializable{

    private final List<Step> stepList = new ArrayList<>();

    @FXML
    private Parent parent;

    @FXML
    private TabPane allTabs;

    @FXML
    private Slider counts;

    @FXML
    private ChoiceBox<Integer> delayParam;

    @FXML
    private ChoiceBox<Integer> hours;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delayParam.getItems().removeAll(delayParam.getItems());
        delayParam.getItems().addAll(0, 1, 2, 3, 5, 7, 10, 13, 15, 20, 30);
        delayParam.getSelectionModel().select(0);

        hours.getItems().removeAll(hours.getItems());
        hours.getItems().addAll(0, 1, 2, 3, 5, 7, 10, 13, 15, 20, 30);
        hours.getSelectionModel().select(0);
    }


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
        //TODO: make here integers to codes
        String actions = "Not implemented yet!";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/tabContent.fxml"));
        fxmlLoader.setRoot(tab);
        fxmlLoader.load();

        allTabs.getTabs().add(tab);
        int tabNumber = tab.getTabPane().getTabs().size();
        Accordion accordion = (Accordion) tab.getTabPane().getTabs().get(tabNumber - 1).getContent().lookup("#acordion");
        accordion.getPanes().get(1).setContent(new Label(actions));
        accordion.getPanes().get(2).setContent(new Label(step.getDescription()));
    }

    public static void moveMouse(int x, int y, int maxTimes, Robot screenWin) {
        for(int count = 0;(MouseInfo.getPointerInfo().getLocation().getX() != x ||
                MouseInfo.getPointerInfo().getLocation().getY() != y) &&
                count < maxTimes; count++) {
            screenWin.mouseMove(x, y);
        }
    }

    @FXML
    private void startRobot() throws AWTException, InterruptedException {
        Robot robot = new Robot();
        LocalTime endTime = LocalTime.now().plusHours(hours.getValue());
        while (LocalTime.now().isBefore(endTime)) {
            for (int i = 0; i < counts.getValue(); ++i) {
                for (Step currStep : stepList) {
                    if (currStep.getPoint() != null) {
                        //This made due to bug in Javafx on scaled monitors(for example 120% scale Windows)
                        moveMouse((int) currStep.getPoint().getX().longValue(), (int) currStep.getPoint().getY().longValue(), 10, robot);
                        if (currStep.getType() == ActionType.CLICK) {
                            robot.mousePress(InputEvent.BUTTON1_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        }
                    } else {
                        if (currStep.getActions().size() == 2) {
                            KeyStroke first = KeyStroke.getKeyStroke(currStep.getActions().get(0), 0, false);
                            KeyStroke second = KeyStroke.getKeyStroke(currStep.getActions().get(1), 0, false);
                            robot.keyPress(first.getKeyCode());
                            robot.keyPress(second.getKeyCode());
                            robot.keyRelease(first.getKeyCode());
                            robot.keyRelease(second.getKeyCode());
                        } else {
                            //TODO: maybe we don't need this
                            for (Integer key : currStep.getActions()) {
                                KeyStroke btn = KeyStroke.getKeyStroke(key, 0, false);
                                robot.keyPress(btn.getKeyCode());
                                robot.keyRelease(btn.getKeyCode());
                            }
                        }
                    }
                    Thread.sleep(1000 * currStep.getSleep());
                }
                Thread.sleep(1000 * delayParam.getValue());
            }
        }
    }
}
