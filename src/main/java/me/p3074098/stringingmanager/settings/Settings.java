package me.p3074098.stringingmanager.settings;

import me.p3074098.bukkitserializationmock.FileUtil;
import me.p3074098.bukkitserializationmock.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Settings {
    
    public static String CURRENT_STYLESHEET;
    public static List<String> ALL_STYLESHEETS;
    
    public static void load() {
        File file = FileUtil.getApplicationFile("StringingManager", "settings.yml");
    
        if (!file.exists())
            FileUtil.createFileAndParents(file);
        
        YamlConfiguration config = new YamlConfiguration(file);
        
        config.load();
        
        CURRENT_STYLESHEET = config.getString("currentStylesheet", "darkTheme.css");
        ALL_STYLESHEETS = config.getList("allStylesheets", Arrays.asList("lightTheme.css", "darkTheme.css"));
    }
    
    public static void save() {
        File file = FileUtil.getApplicationFile("StringingManager", "settings.yml");
    
        YamlConfiguration config = new YamlConfiguration(file);
    
        config.set("stylesheetName", CURRENT_STYLESHEET);
        config.set("allStylesheets", ALL_STYLESHEETS);
        
        config.save();
    }
}
