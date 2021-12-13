package me.p3074098.stringingmanager;

import javafx.beans.property.SimpleStringProperty;
import me.p3074098.bukkitserializationmock.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Customer implements ConfigurationSerializable {

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    
    public Customer(String firstName, String lastName, String phone, String email) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }
    
    public Customer(Map<String, Object> map) {
        this.firstName = new SimpleStringProperty((String) map.get("firstName"));
        this.lastName = new SimpleStringProperty((String) map.get("lastName"));
        this.phone = new SimpleStringProperty((String) map.get("phone"));
        this.email = new SimpleStringProperty((String) map.get("email"));
    }
    
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("firstName", firstName.get());
        map.put("lastName", lastName.get());
        map.put("phone", phone.get());
        map.put("email", email.get());
        
        return map;
    }
    
    public String getFirstName() {
        return firstName.get();
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public String getPhone() {
        return phone.get();
    }
    
    public String getEmail() {
        return email.get();
    }
}
