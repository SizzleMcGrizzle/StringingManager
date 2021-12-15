package me.p3074098.stringingmanager.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import me.p3074098.stringingmanager.Transaction;

import java.io.IOException;

public class TransactionsController extends AnchorPane {


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
    }
}
