package me.p3074098.stringingmanager.controller;

import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import me.p3074098.stringingmanager.Transaction;
import me.p3074098.stringingmanager.util.AnimationUtil;

import java.io.IOException;

public class TransactionsController extends AnchorPane {
    
    @FXML private Button addButton;

    private ApplicationController applicationController;

    protected TransactionsController(ApplicationController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transactions.fxml"));

            loader.setController(this);

            Node n = loader.load();

            getChildren().add(n);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.applicationController = controller;
    
        addButton.focusedProperty().addListener(object -> {
            if(((ObservableBooleanValue) object).get())
                AnimationUtil.animateBorder(addButton);
            else {
                AnimationUtil.stopAnimateBorder(addButton, "transparent");
            }
        });
    
        addButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                addButton.fire();
        });
    }
}
