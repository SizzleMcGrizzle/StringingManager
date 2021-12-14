package me.p3074098.stringingmanager;

import me.p3074098.bukkitserializationmock.ConfigurationSerializable;

import java.util.Map;

public class Transaction implements ConfigurationSerializable {
    
    private Customer customer;
    private String racket;
    
    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
