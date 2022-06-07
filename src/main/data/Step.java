package main.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Step implements Serializable {

    private ActionType type;

    private Point point;

    private List<Integer> actions = new ArrayList<>();

    private String description;

    private Integer sleep;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getActions() {
        return actions;
    }

    public void setActions(List<Integer> actions) {
        this.actions = actions;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }
}
