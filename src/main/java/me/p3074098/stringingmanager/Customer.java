package me.p3074098.stringingmanager;

import javafx.beans.property.SimpleStringProperty;
import me.p3074098.bukkitserializationmock.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Customer implements ConfigurationSerializable {

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final UUID id;
    
    public Customer(String firstName, String lastName, String phone, String email) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.id = UUID.randomUUID();
    }
    
    public Customer(Map<String, Object> map) {
        this.firstName = new SimpleStringProperty((String) map.get("firstName"));
        this.lastName = new SimpleStringProperty((String) map.get("lastName"));
        this.phone = new SimpleStringProperty((String) map.get("phone"));
        this.email = new SimpleStringProperty((String) map.get("email"));
        this.id = UUID.fromString((String) map.getOrDefault("id", UUID.randomUUID().toString()));
    }
    
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("firstName", firstName.get());
        map.put("lastName", lastName.get());
        map.put("phone", phone.get());
        map.put("email", email.get());
        map.put("id", id.toString());
        
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

    public UUID getId() {
        return id;
    }
}
