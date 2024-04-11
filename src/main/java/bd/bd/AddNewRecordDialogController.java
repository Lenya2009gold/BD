package bd.bd;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AddNewRecordDialogController {
    public TextField nameTextField;
    public TextField phoneTextField;
    public TextField brdAuto;
    public TextField brandAuto;
    public TextField modelAuto;
    public TextField vinAuto;
    public Button addButton;
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    public void initialize() {
        setApp(app);
        BooleanBinding isFormIncomplete = nameTextField.textProperty().isEmpty()
                .or(phoneTextField.textProperty().isEmpty())
                .or(brdAuto.textProperty().isEmpty())
                .or(modelAuto.textProperty().isEmpty())
                .or(vinAuto.textProperty().isEmpty())
                .or(brdAuto.textProperty().isEmpty());

        addButton.disableProperty().bind(isFormIncomplete);
    }


    public void handleCancel(ActionEvent actionEvent) {
        setApp(app);
        app.switchWindow("userWindow.fxml","Работа с БД");

    }

    public void handleAdd(ActionEvent actionEvent) {
        setApp(app);
        String nameCustomer = nameTextField.getText();
        String phoneCustomer = phoneTextField.getText();
        String vinAutoSQL = vinAuto.getText();
        String dateAuto = brdAuto.getText();
        String brandAutoSQL = brandAuto.getText();
        String modelAutoSQL = modelAuto.getText();
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Подтверждение");
        conf.setHeaderText(null);
        conf.setContentText("Вы точно хотите добавить?");
        Optional<ButtonType> action = conf.showAndWait();
        if ((action.isPresent()) && (action.get() == ButtonType.OK)) {

            try (Connection conn = DatabaseHandler.connect()) {
                //String DETAILAUTOSQL = "INSERT INTO \"Detail_auto\" (\"VIN\", \"BRD\", \"Brand\", \"Model\") VALUES (?, ?, ?, ?);\n";
                String call = "SELECT add_customer_and_auto(?, ?, ?, ?, ?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(call)) {
                    pstmt.setString(1, nameCustomer);
                    pstmt.setString(2, phoneCustomer);
                    pstmt.setString(3, vinAutoSQL);
                    pstmt.setString(4, dateAuto);
                    pstmt.setString(5,brandAutoSQL);
                    pstmt.setString(6,modelAutoSQL);
                    boolean affected = pstmt.execute();
                    if(affected)
                    {
                        Alerts.showSuc("Новый клиент успешно добавлен");
                    }
                    //ResultSet rs = pstmt.getGeneratedKeys();
//                    if (rs.next()) {
//                        int detailAutoId = -1;
//                        detailAutoId = rs.getInt("detail_auto_id");
//                        System.out.println(detailAutoId);
//                        String CUSTOMERSQL = "INSERT INTO \"Customer\" (\"Name\", \"Phone\", \"Detail_auto_id\") VALUES (?, ?, ?);\n";
//                        try (PreparedStatement pstmt1 = conn.prepareStatement(CUSTOMERSQL)) {
//                            pstmt1.setString(1, nameCustomer);
//                            pstmt1.setString(2, phoneCustomer);
//                            pstmt1.setInt(3, detailAutoId);
//                            int affected = pstmt1.executeUpdate();
//                            if(affected > 0)
//                            {
//                                Alerts.showSuc("Запись успешно добавлена");
//                            }
//                        }
//                    }

                } catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                    Alerts.showErrSQL(ex.getMessage());
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                Alerts.showErrSQL(e.getMessage());
            }


        }
    }
}
