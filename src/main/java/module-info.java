module bd.bd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires jasypt;


    opens bd.bd to javafx.fxml;
    exports bd.bd;
    exports bd.bd.controllers;
    opens bd.bd.controllers to javafx.fxml;
}