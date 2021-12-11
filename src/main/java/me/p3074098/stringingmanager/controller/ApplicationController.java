package me.p3074098.stringingmanager.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import me.p3074098.stringingmanager.settings.Settings;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    
    @FXML
     Label customersButton;
    
    @FXML
     Label modeButton;
    
    @FXML
     Label statisticsButton;
    
    @FXML
     Label transactionsButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListeners();
    }
    
    private void setListeners() {
        modeButton.setOnMouseClicked(event -> {
            
            final String path = "/me/p3074098/stringingmanager/";
            modeButton.getParent().getParent()
                    .getStylesheets()
                    .remove(getClass().getResource(path + Settings.CURRENT_STYLESHEET).toString());
            
            int index = Settings.ALL_STYLESHEETS.indexOf(Settings.CURRENT_STYLESHEET);
            
            if (index == Settings.ALL_STYLESHEETS.size()-1)
                index = 0;
            else
                index++;
            
            Settings.CURRENT_STYLESHEET = Settings.ALL_STYLESHEETS.get(index);
    
            modeButton.getParent().getParent().getStylesheets().add(getClass().getResource(path + Settings.CURRENT_STYLESHEET).toString());
        });
    }
}
