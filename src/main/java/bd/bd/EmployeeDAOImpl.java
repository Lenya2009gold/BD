package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImpl implements EmployeeDAO {
    String SQL_SELECT = "SELECT * FROM \"Employee\"  ";

    private Connection getConnection() throws SQLException {
        return DatabaseHandler.connect();
    }
    public ObservableList<Employee> loadEmployee()
    {
        ObservableList<Employee> employee = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                employee.add(new Employee(rs.getInt("employee_id"),
                        EncryptionUtility.decrypt(rs.getString("firstName")),
                        EncryptionUtility.decrypt(rs.getString("secondName"))));

            }
        } catch (SQLException ex) {
            Alerts.showErr(ex.getMessage());
        }
        return employee;
    }








}
