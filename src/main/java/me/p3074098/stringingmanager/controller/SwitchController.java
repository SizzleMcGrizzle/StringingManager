package me.p3074098.stringingmanager.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import me.p3074098.stringingmanager.util.AnimationUtil;

import java.io.IOException;

public class SwitchController extends Pane {
    
    @FXML private Pane anchor;
    @FXML private HBox fieldBox;
    @FXML private Label label;
    @FXML private Circle switchCircle;
    @FXML private Pane switchPane;
    
    private final BooleanProperty enabled;
    private final StringProperty labelText;

    private Node nextTarget;
    private Node previousTarget;
    
    public SwitchController(@NamedArg("labelText") String labelText) {
        this(labelText, false);
    }
    
    public SwitchController(@NamedArg("labelText") String labelText, @NamedArg("enabled") boolean enabled) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("switch.fxml"));
            
            loader.setController(this);
    
            Node n = loader.load();
            
            getChildren().add(n);
    
            anchor.prefWidthProperty().bind(prefWidthProperty());
            fieldBox.prefWidthProperty().bind(prefWidthProperty().subtract(20));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.labelText = new SimpleStringProperty(labelText);
        this.enabled = new SimpleBooleanProperty(enabled);
        
        label.textProperty().bind(this.labelText);
        
        this.enabled.addListener((observableValue, enable, t1) -> render());
        
        render();
        
        switchPane.setOnMouseClicked(e -> setEnabled(!isEnabled()));
        label.setOnMouseClicked(e -> setEnabled(!isEnabled()));

        fieldBox.focusedProperty().addListener(object -> {
            if(((ObservableBooleanValue) object).get())
                select();
            else
                clearColor();
        });

        fieldBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                setEnabled(!isEnabled());
            if (nextTarget != null)
                if (e.getCode() == KeyCode.DOWN)
                    nextTarget.requestFocus();
            if (previousTarget != null)
                if (e.getCode() == KeyCode.UP)
                    previousTarget.requestFocus();
        });
    }

    private void select() {
        setColor("selected");
    }

    protected void clearColor() {
        setColor("normal");
    }

    private void setColor(String className) {
        label.getStyleClass().removeIf(s -> s.contains("fancy-input-"));
        fieldBox.getStyleClass().removeIf(s -> s.contains("fancy-input-"));

        label.getStyleClass().add("fancy-input-" + className + "-text-fill");
        fieldBox.getStyleClass().add("fancy-input-" + className + "-border-fill");
    }
    
    private void render() {
        double translateX = isEnabled() ? 0 : 32;
        String className = isEnabled() ? "green-accent" : "background-1";
    
        switchCircle.getStyleClass().clear();
        switchCircle.getStyleClass().add(className);
    
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                new KeyValue(switchCircle.translateXProperty(), translateX)
        ));
        timeline.play();
    }

    public void setPreviousTarget(Node previousTarget) {
        this.previousTarget = previousTarget;
    }

    public void setNextTarget(Node nextTarget) {
        this.nextTarget = nextTarget;
    }

    public boolean isEnabled() {
        return enabled.get();
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }
    
    public BooleanProperty enabledProperty() {
        return enabled;
    }
    
    public String getLabelText() {
        return labelText.get();
    }
    
    public void setLabelText(String labelText) {
        this.labelText.set(labelText);
    }
    
    public StringProperty labelTextProperty() {
        return labelText;
    }

    public HBox getFieldBox() {
        return fieldBox;
    }
}
