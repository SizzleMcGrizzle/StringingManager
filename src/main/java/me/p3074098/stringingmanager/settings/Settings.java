package me.p3074098.stringingmanager.settings;

import me.p3074098.bukkitserializationmock.ConfigurationSection;
import me.p3074098.bukkitserializationmock.ConfigurationSerialization;
import me.p3074098.bukkitserializationmock.FileUtil;
import me.p3074098.bukkitserializationmock.YamlConfiguration;
import me.p3074098.stringingmanager.Customer;
import me.p3074098.stringingmanager.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Settings {
    
    static {
        ConfigurationSerialization.registerClass(Customer.class);
        ConfigurationSerialization.registerClass(Transaction.class);
    }
    
    private static final List<Consumer<ConfigurationSection>> saveTasks = new ArrayList<>();
    
    public static String CURRENT_STYLESHEET;
    public static List<String> ALL_STYLESHEETS;
    
    private static YamlConfiguration DATA_CONFIG;
    
    public static void load() {
        File file = FileUtil.getApplicationFile("StringingManager", "settings.yml");
    
        if (!file.exists())
            FileUtil.createFileAndParents(file);
        
        YamlConfiguration config = new YamlConfiguration(file);
        
        config.load();
        
        CURRENT_STYLESHEET = config.getString("currentStylesheet", "darkTheme.css");
        ALL_STYLESHEETS = config.getList("allStylesheets", Arrays.asList("lightTheme.css", "darkTheme.css"));
    
        File dataFile = FileUtil.getApplicationFile("StringingManager", "data.yml");
    
        if (!dataFile.exists())
            FileUtil.createFileAndParents(dataFile);
    
        DATA_CONFIG = new YamlConfiguration(dataFile);
        
        DATA_CONFIG.load();
    }
    
    public static void registerSaveTask(Consumer<ConfigurationSection> consumer) {
        saveTasks.add(consumer);
    }
    
    public static void save() {
        File file = FileUtil.getApplicationFile("StringingManager", "settings.yml");
    
        YamlConfiguration config = new YamlConfiguration(file);
    
        config.set("currentStylesheet", CURRENT_STYLESHEET);
        config.set("allStylesheets", ALL_STYLESHEETS);
        
        config.save();
        
        saveTasks.forEach(t -> t.accept(DATA_CONFIG));
    
        DATA_CONFIG.save();
    }
    
    public static ConfigurationSection getDataConfig() {
        return DATA_CONFIG;
    }
}
