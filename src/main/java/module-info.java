module me.p3074098.stringingmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires bukkitserializationmock;
    
    
    opens me.p3074098.stringingmanager to javafx.fxml;
    exports me.p3074098.stringingmanager;
    opens me.p3074098.stringingmanager.controller to javafx.fxml;
    exports me.p3074098.stringingmanager.controller to javafx.fxml;
}