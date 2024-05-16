package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CustomerDAOImpl implements CustomerDAO {
    private static final String SQL_SELECT_ALL = "SELECT * FROM \"Customer\"";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM \"Customer\" WHERE Customer_id = ?";
    private static final String SQL_UPDATE = "UPDATE \"Customer\" SET Name = ?, Phone = ? WHERE Customer_id = ?";
    private static final String SQL_DELETE = "DELETE FROM \"Customer\" WHERE Customer_id = ?";
    private Connection getConnection() throws SQLException {
        return DatabaseHandler.connect();
    }
    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try (Connection conn = getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    customers.add(new Customer(
                            rs.getInt("Customer_id"),
                            EncryptionUtility.decrypt(rs.getString("Name")),
                            EncryptionUtility.decrypt(rs.getString("Phone"))
                    ));
                }
            }
        } catch (SQLException ex) {
            Alerts.showErr(ex.getMessage());
        }
        return customers;
    }

    public void addCustomer(String nameCustomer, String phoneCustomer, String vinAutoSQL, String dateAuto, String brandAutoSQL,String modelAutoSQL)
    {
        try (Connection conn = getConnection()) {
        //String DETAILAUTOSQL = "INSERT INTO \"Detail_auto\" (\"VIN\", \"BRD\", \"Brand\", \"Model\") VALUES (?, ?, ?, ?);\n";
        String call = "SELECT add_customer_and_auto(?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(call)) {
            pstmt.setString(1, EncryptionUtility.encrypt(nameCustomer));
            pstmt.setString(2, EncryptionUtility.encrypt(phoneCustomer));
            pstmt.setString(3, EncryptionUtility.encrypt(vinAutoSQL));
            pstmt.setString(4, dateAuto);
            pstmt.setString(5,brandAutoSQL);
            pstmt.setString(6,modelAutoSQL);
            boolean affected = pstmt.execute();
            if(affected)
            {
                Alerts.showSuc("Новый клиент успешно добавлен");
            }
        } catch (SQLException ex)
        {
            Alerts.showErrSQL(ex.getMessage());
        }

    } catch (SQLException e) {
        Alerts.showErrSQL(e.getMessage());
    }
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("Customer_id"),
                        EncryptionUtility.decrypt(rs.getString("Name")),
                        EncryptionUtility.decrypt(rs.getString("Phone")));
            }
        } catch (SQLException ex) {
            Alerts.showErr(ex.getMessage());
        }
        return customer;
    }
    @Override
    public void updateCustomer(Customer customer) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, EncryptionUtility.encrypt(customer.getName()));
            pstmt.setString(2, EncryptionUtility.encrypt(customer.getPhone()));
            pstmt.setInt(3, customer.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Alerts.showErr(ex.getMessage());
        }
    }

    @Override
    public void deleteCustomer(int id) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Alerts.showErr(ex.getMessage());
        }
    }
    public int getCustomerId(int orderId)
    {
        String SQL_GET_CUSTOMER_ID = "SELECT \"Customer_id\" FROM \"Order\" WHERE \"Order_id\" = ?";
        int customerId=-1;
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement(SQL_GET_CUSTOMER_ID))
        {
            preparedStatement.setInt(1,orderId);
            try (ResultSet resultSet= preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    customerId=resultSet.getInt("Customer_id");
                }

            }
        } catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
        return customerId;

    }
    public void updateCustomerNameDatabase(int orderId, String newName)
    {
        String SQL_UPDATE = "UPDATE \"Customer\" SET \"Name\" = ? WHERE \"Customer_id\" = ?;";
        int customerId = getCustomerId(orderId);
        try (Connection conn = getConnection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)){
                pstmt.setString(1, EncryptionUtility.encrypt(newName));
                pstmt.setInt(2,customerId);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Alerts.showErrSQL(ex.getMessage() + ex.getErrorCode() + ex.getSQLState());
        }

    }

    public void updateOrderStatusInDatabase(int orderId, String newStatus) {
        String SQL_UPDATE = "UPDATE \"Order\" SET \"Status\" = ? WHERE \"Order_id\" = ?;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }  catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));
        }
    }

    public void updatePhoneInDatabase(int orderId, String newPhone) {
        String SQL_UPDATE = "UPDATE \"Customer\" SET \"Phone\" = ? WHERE \"Customer_id\" = ?;";
        int customerId = getCustomerId(orderId);
        try (Connection conn = getConnection();
             PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, EncryptionUtility.encrypt(newPhone));
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
        }  catch (SQLException ex) {
            Alerts.showErrSQL(ex.getMessage());
        }
    }
}
