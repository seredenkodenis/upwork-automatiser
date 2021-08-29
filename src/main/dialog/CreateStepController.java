package main.dialog;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.annotations.Value;
import main.data.ActionType;
import main.data.Step;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class CreateStepController {

    private final List<String> actions = new ArrayList<>();

    private Scene scene;

    @FXML
    private Button submitButton;

    @FXML
    private Label bindedButtons;

    @FXML
    private Button bindButton;

    @FXML
    private CheckBox doubleClick;

    @FXML
    private CheckBox clickedStep;

    @FXML
    private CheckBox specialKeys;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button pictureButton;

    @FXML
    private Button rebindButton;

    private int tries = 2;

    private final EventHandler<KeyEvent> event = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            if (tries == 1) {
                rebindButton.setDisable(false);
                bindButton.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, this);
            }
            changeText(event.getCode().getName());
            actions.add(event.getCode().getName());
            tries = tries - 1;
        }
    };

    private Double pointX;

    private Double pointY;

    private Step step;

    private ActionType type;

    @Value(key = "dialog-title")
    private String title;

    @FXML
    private void saveStep() {
        if (actions.size() != 0 || (pointX != null && pointY != null)) {
            step = new Step();
            step.setPoint(new main.data.Point(pointX, pointY));
            step.setActions(actions);
            step.setType(type);
            step.setDescription(descriptionField.getText());
        }
        Stage currentStage = (Stage) bindedButtons.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void changeText(String key) {
        bindedButtons.setText(bindedButtons.getText() + " " + key);
    }

    @FXML
    private void rebindButtons() {
        tries = 2;
        actions.clear();
        bindedButtons.setText("");
        bindButton.setDisable(false);
    }

    @FXML
    private void isClicked() {
        if (clickedStep.isSelected()) {
            type = ActionType.CLICK;
            specialKeys.setDisable(true);
            doubleClick.setDisable(true);
            bindButton.setDisable(true);
        } else {
            type = null;
            specialKeys.setDisable(false);
            doubleClick.setDisable(false);
            bindButton.setDisable(false);
        }
    }

    @FXML
    private void useSpecialKeys() {
        if (specialKeys.isSelected()) {
            doubleClick.setDisable(true);
            pictureButton.setDisable(true);
            clickedStep.setDisable(true);
            bindButton.setDisable(false);
        } else {
            doubleClick.setDisable(false);
            pictureButton.setDisable(false);
            clickedStep.setDisable(false);
            bindButton.setDisable(true);
        }
    }

    @FXML
    private void bindButtons() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("How to use this button");
        alert.setContentText("After you understand press UNDERSTAND. Then press first key, then other.");

        alert.showAndWait();

        bindButton.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event);
        bindButton.setDisable(true);
    }

    @FXML
    private void isDoubleClicked() {
        if (doubleClick.isSelected()) {
            type = ActionType.CLICK;
            specialKeys.setDisable(true);
            clickedStep.setDisable(true);
            bindButton.setDisable(true);
        } else {
            type = null;
            specialKeys.setDisable(false);
            clickedStep.setDisable(false);
            bindButton.setDisable(false);
        }
    }

    @FXML
    public void makePicture() throws AWTException, InterruptedException {

        hideWindows(FXRobotHelper.getStages(), (Stage) bindedButtons.getScene().getWindow());

        Robot robot = new Robot();
        final Dimension screenSize = Toolkit.getDefaultToolkit().
                getScreenSize();
        final BufferedImage screen = robot.createScreenCapture(
                new Rectangle(screenSize));

        SwingUtilities.invokeLater(() -> {
            JLabel screenLabel = new JLabel(new ImageIcon(screen));
            JScrollPane screenScroll = new JScrollPane(screenLabel);
            screenScroll.setPreferredSize(new Dimension(
                    (int) (screenSize.getWidth() * 0.75),
                    (int) (screenSize.getHeight() * 0.75)));

            final Point pointOfInterest = new Point();

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(screenScroll, BorderLayout.CENTER);

            final JLabel pointLabel = new JLabel(
                    "Click on any point in the screen shot!");
            panel.add(pointLabel, BorderLayout.SOUTH);

            screenLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    pointOfInterest.setLocation(me.getPoint());
                    for (int i = pointOfInterest.x - 7; i < pointOfInterest.x + 7; ++i){
                        for (int j = pointOfInterest.y - 7; j < pointOfInterest.y + 7; ++j){
                            screen.setRGB(i, j, Color.RED.getRGB());
                        }
                    }
                    screenLabel.repaint();
                    pointLabel.setFont(new Font("Roboto", 50, 30));
                    pointLabel.setText(
                            "Point: " +
                                    pointOfInterest.getX() +
                                    "x" +
                                    pointOfInterest.getY());
                }
            });

            JOptionPane.showMessageDialog(null, panel);

            pointX = pointOfInterest.getX();
            pointY = pointOfInterest.getY();
        });
    }
    //TODO: add here field with previous description

    public Step getStep() {
        return step;
    }

    public void hideWindows(ObservableList<Stage> stages, Stage dialog) throws InterruptedException {
        for (Stage stage : stages){
            stage.hide();
        }
        Thread.sleep(150);

        dialog.hide();
        Thread.sleep(150);
    }

}
