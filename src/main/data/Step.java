package main.data;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class Step {

    private ActionType type;

    private Point point;

    private List<KeyCode> actions = new ArrayList<>();

    private String description;

    public Step() {
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<KeyCode> getActions() {
        return actions;
    }

    public void setActions(List<KeyCode> actions) {
        this.actions = actions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
