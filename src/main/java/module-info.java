module bd.bd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;


    opens bd.bd to javafx.fxml;
    exports bd.bd;
}