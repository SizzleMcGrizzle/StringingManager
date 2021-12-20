package me.p3074098.stringingmanager.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import me.p3074098.bukkitserializationmock.ConfigurationSerializable;
import me.p3074098.stringingmanager.Customer;
import me.p3074098.stringingmanager.api.StatisticsSupplier;
import me.p3074098.stringingmanager.Transaction;
import me.p3074098.stringingmanager.util.Settings;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsController extends AnchorPane implements StatisticsSupplier {
    
    @FXML private AnchorPane anchor;
    @FXML private FancyInput customerInput;
    @FXML private FancyInput maxPriceInput;
    @FXML private FancyInput maxTensionInput;
    @FXML private FancyInput minPriceInput;
    @FXML private FancyInput minTensionInput;
    @FXML private SwitchController preStretchInput;
    @FXML private Pane statisticGrid;
    @FXML private SwitchController waxedInput;
    @FXML private Button searchButton;
    
    private List<Transaction> transactions;
    private List<Customer> customers;
    
    private StatisticCardController[] cards = new StatisticCardController[10];
    
    public StatisticsController(ApplicationController applicationController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("statistics.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < 10; i++) {
            cards[i] = (StatisticCardController) statisticGrid.getChildren().get(i);
        }
        
        renderStatistics();
        
        Settings.TRANSACTIONS.addListener((ListChangeListener<Transaction>) change -> renderStatistics());
        Settings.CUSTOMERS.addListener((ListChangeListener<Customer>) change -> renderStatistics());
        
        searchButton.setOnAction(e -> renderStatistics());
        
        customerInput.setWarnable(false);
        minPriceInput.setWarnable(false);
        maxPriceInput.setWarnable(false);
        minTensionInput.setWarnable(false);
        maxTensionInput.setWarnable(false);
    }
    
    private void filter() {
        transactions = Settings.TRANSACTIONS.stream()
                .filter(t -> {
                    String s = customerInput.validateString();
                    
                    if (s == null)
                        return true;
                    
                    return t.getFullName().equalsIgnoreCase(s);
                })
                .filter(t -> {
                    Double d = minTensionInput.validateDouble();
                    
                    if (d == null)
                        return true;
                    
                    return t.getTension() >= d;
                })
                .filter(t -> {
                    Double d = maxTensionInput.validateDouble();
            
                    if (d == null)
                        return true;
            
                    return t.getTension() <= d;
                })
                .filter(t -> !waxedInput.isEnabled() || t.isWaxed())
                .filter(t -> !preStretchInput.isEnabled() || t.isPreStretched())
                .filter(t -> {
                    Double d = minPriceInput.validateDouble();
            
                    if (d == null)
                        return true;
            
                    return t.getDue() >= d;
                })
                .filter(t -> {
                    Double d = maxPriceInput.validateDouble();
            
                    if (d == null)
                        return true;
            
                    return t.getDue() <= d;
                })
                .collect(Collectors.toList());
        
        customers = Settings.CUSTOMERS.stream()
                .filter(t -> {
                    String s = customerInput.validateString();
                
                    if (s == null)
                        return true;
                
                    return t.getFullName().equalsIgnoreCase(s);
                }).collect(Collectors.toList());
    }
    
    private void renderStatistics() {
        filter();
        cards[0].setStatisticLabel("Total Revenue");
        cards[0].setStatisticInformation(format(getTotalRevenue()));
        cards[1].setStatisticLabel("Total Loss");
        cards[1].setStatisticInformation(format(getTotalLoss()));
        cards[2].setStatisticLabel("Total Profit");
        cards[2].setStatisticInformation(format(getTotalProfit()));
        cards[3].setStatisticLabel("Average Tension");
        cards[3].setStatisticInformation(String.valueOf(getAverageTension()));
        cards[4].setStatisticLabel("Number of Customers");
        cards[4].setStatisticInformation(String.valueOf(getNumberCustomers()));
        cards[5].setStatisticLabel("Average Revenue per Transaction");
        cards[5].setStatisticInformation(format(getAverageRevenue()));
        cards[6].setStatisticLabel("Average Loss per Transaction");
        cards[6].setStatisticInformation(format(getAverageLoss()));
        cards[7].setStatisticLabel("Average Profit per Transaction");
        cards[7].setStatisticInformation(format(getAverageProfit()));
        cards[8].setStatisticLabel("Number Pre-Stretched");
        cards[8].setStatisticInformation(String.valueOf(getNumberPreStretched()));
        cards[9].setStatisticLabel("Number Waxed");
        cards[9].setStatisticInformation(String.valueOf(getNumberWaxed()));
    }
    
    private String format(double d) {
        return "$" + String.format("%.2f",d);
    }
    
    @Override
    public double getTotalRevenue() {
        double total = 0;
    
        for (Transaction transaction : transactions) {
            total += transaction.getDue();
        }
        
        return total;
    }
    
    @Override
    public double getTotalLoss() {
        double loss = 0;
    
        for (Transaction transaction : transactions) {
            loss += transaction.getLoss();
        }
        
        return loss;
    }
    
    @Override
    public double getTotalProfit() {
        return getTotalRevenue()-getTotalLoss();
    }
    
    @Override
    public double getAverageTension() {
        double total = 0;
    
        for (Transaction transaction : transactions) {
            total += transaction.getTension();
        }
        
        return transactions.isEmpty() ? 0 : total/transactions.size();
    }
    
    @Override
    public int getNumberCustomers() {
        return Settings.CUSTOMERS.size();
    }
    
    @Override
    public double getAverageRevenue() {
        return transactions.isEmpty() ? 0 : getTotalRevenue()/transactions.size();
    }
    
    @Override
    public double getAverageLoss() {
        return transactions.isEmpty() ? 0 : getTotalLoss()/transactions.size();
    }
    
    @Override
    public double getAverageProfit() {
        return transactions.isEmpty() ? 0 : getTotalProfit()/transactions.size();
    }
    
    @Override
    public int getNumberPreStretched() {
        return (int) transactions.stream().filter(Transaction::isPreStretched).count();
    }
    
    @Override
    public int getNumberWaxed() {
        return (int) transactions.stream().filter(Transaction::isWaxed).count();
    }
}
