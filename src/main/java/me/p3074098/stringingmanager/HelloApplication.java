package me.p3074098.stringingmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.p3074098.stringingmanager.controller.CustomersController;
import me.p3074098.stringingmanager.controller.TransactionsController;
import me.p3074098.stringingmanager.util.Settings;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("controller/application.fxml"));
        
        Parent parent = fxmlLoader.load();

        parent.getStylesheets().removeIf(c -> c.contains("Theme"));
        parent.getStylesheets().add(getClass().getResource(Settings.CURRENT_STYLESHEET).toString());
        
        Scene scene = new Scene(parent);
        stage.setTitle("Stringing Transaction Manager");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() {
        Settings.save();
    }
    
    public static void main(String[] args) {
        Settings.load();
        launch();
    }
}