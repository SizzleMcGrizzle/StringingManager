package me.p3074098.stringingmanager.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import me.p3074098.stringingmanager.util.Settings;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    
    @FXML
    private Label customersButton;
    
    @FXML
    private Label statisticsButton;
    
    @FXML
    private Label transactionsButton;
    
    @FXML
    private BurgerIconController burgerIcon;
    
    @FXML
    private HBox modeButton;
    
    @FXML
    private HBox menuBox;
    
    @FXML
    private AnchorPane base;
    
    private BackgroundImage dayImage;
    private BackgroundImage nightImage;
    
    @FXML
    private Pane contentPane;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuBox.setTranslateX(-300);
        
        dayImage = new BackgroundImage(new Image(getClass().getResource("/me/p3074098/stringingmanager/image/day.png").toString()),
                BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
                new BackgroundSize(50, 50, false, false, false, true));
        nightImage = new BackgroundImage(new Image(getClass().getResource("/me/p3074098/stringingmanager/image/night.png").toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50, 50, false, false, false, true));
        modeButton.setBackground(new Background(Settings.CURRENT_STYLESHEET.equals("darkTheme.css") ? nightImage : dayImage));
        
        contentPane = new Pane();
        
        contentPane.getStyleClass().add("background-0");
        
        AnchorPane.setTopAnchor(contentPane, 0D);
        AnchorPane.setBottomAnchor(contentPane, 0D);
        AnchorPane.setLeftAnchor(contentPane, 0D);
        AnchorPane.setRightAnchor(contentPane, 0D);
        
        base.getChildren().add(contentPane);
        
        contentPane.setViewOrder(1);
        
        contentPane.getChildren().add(new CustomersController(this));
        
        setListeners();
    }
    
    private void setListeners() {
        modeButton.setOnMouseClicked(event -> {

            final String path = "/me/p3074098/stringingmanager/";
            base
                    .getStylesheets()
                    .remove(getClass().getResource(path + Settings.CURRENT_STYLESHEET).toString());

            int index = Settings.ALL_STYLESHEETS.indexOf(Settings.CURRENT_STYLESHEET);

            if (index == Settings.ALL_STYLESHEETS.size()-1)
                index = 0;
            else
                index++;

            Settings.CURRENT_STYLESHEET = Settings.ALL_STYLESHEETS.get(index);
            
            base.getStylesheets().add(0, getClass().getResource(path + Settings.CURRENT_STYLESHEET).toString());
    
            modeButton.setBackground(new Background(Settings.CURRENT_STYLESHEET.equals("darkTheme.css") ? nightImage : dayImage));
        });
        
        burgerIcon.addIconClickHandler(() -> {
    
            double x = burgerIcon.isCollapsed() ? 0 : -300;
            
            Timeline timeline  = new Timeline();
            timeline.setCycleCount(1);
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(250),
                            new KeyValue(menuBox.translateXProperty(), x)));
            timeline.play();
        });
    }

    public BurgerIconController getBurgerIcon() {
        return burgerIcon;
    }

    public HBox getModeButton() {
        return modeButton;
    }
}
