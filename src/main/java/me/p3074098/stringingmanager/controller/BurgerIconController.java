package me.p3074098.stringingmanager.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BurgerIconController extends Pane {
    
    @FXML
    private Line line1;
    
    @FXML
    private Line line2;
    
    @FXML
    private Line line3;
    
    private List<Runnable> onClickHandlers = new ArrayList<>();
    
    private boolean collapsed;
    
    public BurgerIconController() {
        
        try {
    
            FXMLLoader loader = new FXMLLoader(getClass().getResource("burgerIcon.fxml"));
            
            loader.setController(this);
    
            Node parent = loader.load();
            
            getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        setListeners();
    }
    
    public void addIconClickHandler(Runnable runnable) {
        onClickHandlers.add(runnable);
    }
    
    private void setListeners() {
        line1.getParent().setOnMouseClicked(event -> {
            
            collapsed = !collapsed;
    
            onClickHandlers.forEach(Runnable::run);
            
            double endY = 0;
            double opacity = 1;
            double startX = 0;
            double layoutX = 11;
            
            if (collapsed) {
                endY = 20;
                opacity = 0;
                startX = 4.5;
                layoutX = 8.5;
            }
            
    
            Timeline timeline  = new Timeline();
            timeline.setCycleCount(1);
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(250),
                            new KeyValue(line1.endYProperty(), endY),
                            new KeyValue(line1.startXProperty(), startX),
                            new KeyValue(line3.startXProperty(), startX),
                            new KeyValue(line1.layoutXProperty(), layoutX),
                            new KeyValue(line3.layoutXProperty(), layoutX),
                            new KeyValue(line2.opacityProperty(), opacity),
                            new KeyValue(line3.endYProperty(), -1*endY)));
            timeline.play();
        });
    }
    
    public boolean isCollapsed() {
        return collapsed;
    }
}
