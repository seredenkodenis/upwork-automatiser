package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContentTabController {

    @FXML
    private final Label description;

    @FXML
    private final Label actions;

    public ContentTabController(String description, String actions) {
        this.description = new Label(description);
        this.actions = new Label(actions);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setActions(String actions) {
        this.actions.setText(actions);
    }
}
