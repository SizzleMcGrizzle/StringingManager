package me.p3074098.stringingmanager;

import me.p3074098.bukkitserializationmock.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Customer implements ConfigurationSerializable {
    
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
    
    public Customer(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }
    
    public Customer(Map<String, Object> map) {
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");
        this.phone = (String) map.get("phone");
        this.email = (String) map.get("email");
    }
    
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("phone", phone);
        map.put("email", email);
        
        return map;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPhoneNumber() {
        return phone;
    }
    
    public String getEmail() {
        return email;
    }
}
