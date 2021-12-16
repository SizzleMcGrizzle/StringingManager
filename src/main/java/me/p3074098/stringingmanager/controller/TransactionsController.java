package me.p3074098.stringingmanager.controller;

import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import me.p3074098.stringingmanager.Customer;
import me.p3074098.stringingmanager.Transaction;
import me.p3074098.stringingmanager.util.AnimationUtil;
import me.p3074098.stringingmanager.util.Settings;

import java.io.IOException;

public class TransactionsController extends AnchorPane {

    @FXML private Button addButton;
    @FXML private AnchorPane anchor;
    @FXML private TableView<Transaction> customerTable;
    @FXML private FancyInput input1;
    @FXML private FancyInput input2;
    @FXML private FancyInput input3;
    @FXML private FancyInput input4;
    @FXML private FancyInput input5;
    @FXML private FancyInput input6;
    @FXML private TableColumn<Transaction, String> lossColumn;
    @FXML private TableColumn<Transaction, String> nameColumn;
    @FXML private TableColumn<Transaction, String> paidColumn;
    @FXML private TableColumn<Transaction, String> racketColumn;
    @FXML private TableColumn<Transaction, String> stretchedColumn;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> dueColumn;
    @FXML private TableColumn<Transaction, Double> tensionColumn;
    @FXML private TableColumn<Transaction, String> waxColumn;
    @FXML private SwitchController switch1;
    @FXML private SwitchController switch2;

    private FancyInput[] inputs;


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

        inputs = new FancyInput[]{input1, input2, input3, input4, input5, input6};

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

        input1.setNextTarget(input2.getTextField());
        input2.setPreviousTarget(input1.getTextField());
        input2.setNextTarget(input3.getTextField());
        input3.setPreviousTarget(input2.getTextField());
        input3.setNextTarget(input4.getTextField());
        input4.setPreviousTarget(input3.getTextField());
        input4.setNextTarget(input5.getTextField());
        input5.setPreviousTarget(input4.getTextField());
        input5.setNextTarget(input6.getTextField());
        input6.setPreviousTarget(input5.getTextField());
        input6.setNextTarget(switch1.getFieldBox());
        switch1.setPreviousTarget(input6.getTextField());
        switch1.setNextTarget(switch2.getFieldBox());
        switch2.setPreviousTarget(switch1.getFieldBox());

        input2.setAllowPeriods(true);
        input3.setAllowPeriods(true);

        input1.setValidateString(s -> Settings.CUSTOMERS
                .stream()
                .anyMatch(c -> c.getFullName().equalsIgnoreCase(s)));

        customerTable.setItems(Settings.TRANSACTIONS);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        racketColumn.setCellValueFactory(new PropertyValueFactory<>("racket"));
        tensionColumn.setCellValueFactory(new PropertyValueFactory<>("tension"));
        waxColumn.setCellValueFactory(new PropertyValueFactory<>("waxedFanciful"));
        stretchedColumn.setCellValueFactory(new PropertyValueFactory<>("preStretchedFanciful"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDroppedOff"));
        dueColumn.setCellValueFactory(new PropertyValueFactory<>("dueFanciful"));
        lossColumn.setCellValueFactory(new PropertyValueFactory<>("lossFanciful"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paidFanciful"));

        addButton.setOnAction(e -> {

            String name = input1.validateString();
            Double due = input2.validateDouble();
            Double loss = input3.validateDouble();
            String racket = input4.validateString();
            String date = input5.validateString();
            Double tension = input6.validateDouble();
            boolean wax = switch1.isEnabled();
            boolean stretch = switch2.isEnabled();

            Object[] values = new Object[]{name, due, loss, racket ,date, tension};

            for (Object v : values)
                if (v == null)
                    return;

            Transaction transaction = new Transaction(
                    Settings.CUSTOMERS.stream().filter(c -> c.getFullName().equalsIgnoreCase(name)).findFirst().get(),
                    racket,
                    tension,
                    stretch,
                    wax,
                    due,
                    loss,
                    date
            );

            for (FancyInput input : inputs) {
                input.clearText();
            }

            Settings.TRANSACTIONS.add(transaction);
            input1.getTextField().requestFocus();
        });
    }
}
