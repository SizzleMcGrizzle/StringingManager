package me.p3074098.stringingmanager.controller;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class StatisticCardController extends AnchorPane {
    
    @FXML private Text statInfo;
    @FXML private Text statLabel;
    
    private final StringProperty statisticInformation;
    private final StringProperty statisticLabel;
    
    public StatisticCardController(@NamedArg("statisticLabel") String statLabel, @NamedArg("statInfo") String statInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("statisticCard.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        statisticInformation = new SimpleStringProperty(statInfo);
        statisticLabel = new SimpleStringProperty(statLabel);
        
        this.statInfo.textProperty().bind(statisticInformation);
        this.statLabel.textProperty().bind(statisticLabel);
    }
    
    public String getStatisticInformation() {
        return statisticInformation.get();
    }
    
    public String getStatisticLabel() {
        return statisticLabel.get();
    }
    
    public StringProperty statisticInformationProperty() {
        return statisticInformation;
    }
    
    public StringProperty statisticLabelProperty() {
        return statisticLabel;
    }
    
    public void setStatisticInformation(String statisticInformation) {
        this.statisticInformation.set(statisticInformation);
    }
    
    public void setStatisticLabel(String statisticLabel) {
        this.statisticLabel.set(statisticLabel);
    }
}
