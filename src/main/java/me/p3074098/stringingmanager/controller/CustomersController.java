package me.p3074098.stringingmanager.controller;

import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import me.p3074098.stringingmanager.Customer;
import me.p3074098.stringingmanager.util.AnimationUtil;
import me.p3074098.stringingmanager.util.Settings;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomersController extends AnchorPane {
    
    @FXML private AnchorPane anchor;
    @FXML private Button addButton;
    @FXML private FancyInput input1;
    @FXML private FancyInput input2;
    @FXML private FancyInput input3;
    @FXML private FancyInput input4;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> firstNameColumn;
    @FXML private TableColumn<Customer, String> lastNameColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    
    private final FancyInput[] inputs;

    private ApplicationController applicationController;
    
    public CustomersController(ApplicationController applicationController) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customers.fxml"));
            
            loader.setController(this);
    
            Node n = loader.load();
            
            getChildren().add(n);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.applicationController = applicationController;

        input1.setPreviousTarget(applicationController.getModeButton());
        input1.setNextTarget(input2.getTextField());
        input2.setPreviousTarget(input1.getTextField());
        input2.setNextTarget(input3.getTextField());
        input3.setPreviousTarget(input2.getTextField());
        input3.setNextTarget(input4.getTextField());
        input4.setPreviousTarget(input3.getTextField());
        input4.setNextTarget(addButton);
        
        input3.setValidateDouble(d -> String.valueOf(BigDecimal.valueOf(d)).length() == 10);

        input4.setValidateString(s -> {
            String[] split = s.split("@");
            
            if (split.length != 2)
                return false;
            
            if (split[0].isEmpty() || split[1].isEmpty())
                return false;
            
            String[] split2 = split[1].split("\\.");
            
            if (split2.length < 2)
                return false;
            
            return !split2[0].isEmpty() && !split2[1].isEmpty();
        });
        
        inputs = new FancyInput[]{input1, input2, input3, input4};

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        customerTable.setItems(Settings.CUSTOMERS);

        setListeners();
    }
    
    private void setListeners() {
        addButton.setOnAction(e -> {
            
            String[] args = new String[inputs.length];
            
            for (int i = 0; i < inputs.length; i++) {
                args[i] = inputs[i].validateString();
            }
            
            for (String s : args)
                if (s == null)
                    return;
            
            Customer customer = new Customer(args[0],
                    args[1],
                    args[2].substring(0, 3) + "-" + args[2].substring(3,6) + "-" + args[2].substring(6,10),
                    args[3]);
    
            for (FancyInput input : inputs) {
                input.clearText();
            }
            
            Settings.CUSTOMERS.add(customer);
            input1.getTextField().requestFocus();
        });

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


        customerTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE)
                Settings.CUSTOMERS.removeAll(customerTable.getSelectionModel().getSelectedItems());
        });
    
        customerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
