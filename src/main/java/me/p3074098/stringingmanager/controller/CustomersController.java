package me.p3074098.stringingmanager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import me.p3074098.stringingmanager.Customer;
import me.p3074098.stringingmanager.settings.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomersController extends AnchorPane {
    
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private Button addButton;
    
    @FXML
    private FancyInput input1;
    
    @FXML
    private FancyInput input2;
    
    @FXML
    private FancyInput input3;
    
    @FXML
    private FancyInput input4;
    
    private FancyInput[] inputs;
    private List<Customer> customers;
    
    public CustomersController() {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customers.fxml"));
            
            loader.setController(this);
    
            Node n = loader.load();
            
            getChildren().add(n);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        customers = Settings.getDataConfig().getList("customers", new ArrayList<>());
    
        System.out.println(customers);
        
        input1.setNextTarget(input2.getTextField());
        input2.setPreviousTarget(input1.getTextField());
        input2.setNextTarget(input3.getTextField());
        input3.setPreviousTarget(input2.getTextField());
        input3.setNextTarget(input4.getTextField());
        input4.setPreviousTarget(input3.getTextField());
        input4.setNextTarget(addButton);
        
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
    
        Settings.registerSaveTask(config -> config.set("customers", customers));
        
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
            
            Customer customer = new Customer(args[0], args[1], args[2], args[3]);
    
            for (FancyInput input : inputs) {
                input.clearText();
            }
            
            customers.add(customer);
        });
    }

}
