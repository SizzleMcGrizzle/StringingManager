package me.p3074098.stringingmanager;

import javafx.beans.property.*;
import me.p3074098.bukkitserializationmock.ConfigurationSerializable;
import me.p3074098.stringingmanager.util.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Transaction implements ConfigurationSerializable {

    private Customer customer;
    private final StringProperty name;
    private final StringProperty racket;
    private final DoubleProperty tension;
    private final BooleanProperty preStretch;
    private final BooleanProperty wax;
    private final DoubleProperty due;
    private final DoubleProperty loss;
    private final DoubleProperty paid;
    private final StringProperty dateDroppedOff;
    private final StringProperty dateReturned;

    public Transaction(Customer customer, String racket, double tension, boolean preStretch,
                       boolean wax, double due, double loss, String dateDroppedOff) {

        this.customer = customer;
        this.name = new SimpleStringProperty(customer.getFirstName() + " " + customer.getLastName());
        this.racket = new SimpleStringProperty(racket);
        this.tension = new SimpleDoubleProperty(tension);
        this.preStretch = new SimpleBooleanProperty(preStretch);
        this.wax = new SimpleBooleanProperty(wax);
        this.due = new SimpleDoubleProperty(due);
        this.loss = new SimpleDoubleProperty(loss);
        this.dateDroppedOff = new SimpleStringProperty(dateDroppedOff);
        this.paid = new SimpleDoubleProperty(0);
        this.dateReturned = new SimpleStringProperty("");
    }

    public Transaction(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("customer"));
        Settings.CUSTOMERS.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(c -> customer = c);

        this.name = new SimpleStringProperty((String) map.get("name"));
        this.racket = new SimpleStringProperty((String) map.get("racket"));
        this.tension = new SimpleDoubleProperty((double) map.get("tension"));
        this.preStretch = new SimpleBooleanProperty((boolean) map.get("preStretch"));
        this.wax = new SimpleBooleanProperty((boolean) map.get("wax"));
        this.due = new SimpleDoubleProperty((double) map.get("due"));
        this.loss = new SimpleDoubleProperty((double) map.get("loss"));
        this.dateDroppedOff = new SimpleStringProperty((String) map.get("dateDroppedOff"));
        this.paid = new SimpleDoubleProperty((double) map.getOrDefault("paid", 0));
        this.dateReturned = new SimpleStringProperty((String) map.getOrDefault("dateReturned", ""));
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", getName());
        map.put("customer", customer.getId().toString());
        map.put("racket", getRacket());
        map.put("tension", getTension());
        map.put("preStretch", isPreStretched());
        map.put("wax", isWaxed());
        map.put("due", getDue());
        map.put("loss", getLoss());
        map.put("paid", getPaid());
        map.put("dateDroppedOff", getDateDroppedOff());
        map.put("dateReturned", getDateReturned());

        return map;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getName() {
        return name.get();
    }

    public double getDue() {
        return due.get();
    }

    public double getLoss() {
        return loss.get();
    }

    public double getPaid() {
        return paid.get();
    }

    public double getTension() {
        return tension.get();
    }

    public String getDateDroppedOff() {
        return dateDroppedOff.get();
    }

    public String getRacket() {
        return racket.get();
    }

    public String getDateReturned() {
        return dateReturned.get();
    }

    public boolean isPreStretched() {
        return preStretch.get();
    }

    public boolean isWaxed() {
        return wax.get();
    }

    public void setDateReturned(String dateReturned) {
        this.dateReturned.set(dateReturned);
    }

    public void setPaid(double paid) {
        this.paid.set(paid);
    }
}
