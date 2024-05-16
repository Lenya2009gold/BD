package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailDAOImpl implements DetailDAO{
    private Connection getConnection() throws SQLException {
        return DatabaseHandler.connect();
    }


    @Override
    public Detail getDetailById(int id) {
        String sql = "SELECT * FROM Detail WHERE detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Detail(
                            rs.getInt("detail_id"),
                            rs.getString("detailsNumber"),
                            rs.getInt("amount"),
                            rs.getString("manufactur"),
                            rs.getString("providerName")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    @Override
    public void addDetail(Detail detail) {
        String sql = "INSERT INTO Detail (detailsNumber, amount, manufactur, providerName) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, detail.getDetailsNumber());
            pstmt.setInt(2, detail.getAmount());
            pstmt.setString(3, detail.getManufactur());
            pstmt.setString(4, detail.getProviderName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateDetail(Detail detail) {
        String sql = "UPDATE Detail SET detailsNumber = ?, amount = ?, manufactur = ?, providerName = ? WHERE detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, detail.getDetailsNumber());
            pstmt.setInt(2, detail.getAmount());
            pstmt.setString(3, detail.getManufactur());
            pstmt.setString(4, detail.getProviderName());
            pstmt.setInt(5, detail.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void deleteDetail(int id) {
        String sql = "DELETE FROM Detail WHERE detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateDetailAmount(int detailId, int newAmount) throws SQLException {
        String sql = "UPDATE Detail SET amount_available = ? WHERE detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newAmount);
            pstmt.setInt(2, detailId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing updateDetailAmount: " + e.getMessage());
            throw e; // Re-throw the exception to handle it in the calling method
        }
    }
    @Override
    public int getAvailableAmount(int detailId) throws SQLException {
        String sql = "SELECT amount_available FROM Detail WHERE detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detailId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("amount_available");
            } else {
                throw new SQLException("Detail not found with ID: " + detailId);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching available amount: " + e.getMessage());
            throw e;  // Re-throw the exception to be handled by the caller
        }
    }
    public void insertDetailOrder(int seletctedDetailsid,int orderAmount, Date sqlDate,String status,int selectedCustomerId
    ,int selectedEmployeeId)
    {
        int availableAmount = 0;
        try (Connection conn = getConnection()) {
            String checkAmountSQL = "SELECT p.\"Amount_available\"\n" +
                    "FROM \"Provider\" p\n" +
                    "JOIN \"Detail\" dp ON p.\"Provider_id\" = dp.\"Provider_id\"\n" +
                    "WHERE dp.\"Detail_id\" = ?;\n";
            String updateAmountSQL = "UPDATE \"Detail_order\" SET \"amount\" =  ? WHERE \"Detail_order_id\" = ?;";
            String insertOrderSQL = "INSERT INTO \"Order\" (\"Datetime\", \"Status\", \"Customer_id\", \"Employee_id\") VALUES (?, ?, ?, ?) RETURNING \"Order_id\";";
            int orderId;
            try (PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement checkAmountpstmt = conn.prepareStatement(checkAmountSQL)) {

                checkAmountpstmt.setInt(1, seletctedDetailsid);
                ResultSet rs = checkAmountpstmt.executeQuery();
                while (rs.next()) {
                    availableAmount = rs.getInt("Amount_available");
                }
                if (availableAmount >= orderAmount)
                {
                    insertOrderStmt.setDate(1, sqlDate);
                    insertOrderStmt.setString(2, status);
                    insertOrderStmt.setInt(3, selectedCustomerId);
                    insertOrderStmt.setInt(4, selectedEmployeeId);
                    insertOrderStmt.executeUpdate();
                    try (ResultSet rsOrder = insertOrderStmt.getGeneratedKeys()) {
                        if (rsOrder.next()) {
                            orderId = rsOrder.getInt(1);
                        } else {
                            Alerts.showErr("Не удалось создать заказ.");
                            return;
                        }
                    }

                    String insertDetailOrderSQL = "INSERT INTO \"Detail_order\" (\"Order_id\", \"Detail_id\", \"amount\") VALUES (?, ?, ?);";
                    try (PreparedStatement insertDetailOrderStmt = conn.prepareStatement(insertDetailOrderSQL)) {
                        insertDetailOrderStmt.setInt(1, orderId);
                        insertDetailOrderStmt.setInt(2, seletctedDetailsid);
                        insertDetailOrderStmt.setInt(3, orderAmount);
                        insertDetailOrderStmt.executeUpdate();
                        Alerts.showSuc("Всё добавлено");
                    }
                }
                else
                {
                    Alerts.showErr("У поставщика меньше запчастей");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alerts.showErrSQL(e.getMessage());
        }

    }
    public ObservableList<Detail> loadDetails()
    {
        ObservableList<Detail> allDetails = FXCollections.observableArrayList();
        String SQL_SELECT = " SELECT d.\"Detail_id\", d.\"DetailsNumber\", d.\"Manufactur\", p.\"Name\" AS \"ProviderName\", p.\"Amount_available\"\n" +
                "    FROM \"Detail\" d\n" +
                "    JOIN \"Provider\" p ON d.\"Provider_id\" = p.\"Provider_id\"";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT))
        {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allDetails.add(new Detail(rs.getInt("detail_id"),
                        rs.getString("detailsNumber"),
                        rs.getInt("amount_available"),
                        rs.getString("Manufactur"),
                        rs.getString("ProviderName")));

            }
        }
        catch (SQLException ex) {
            Alerts.showErrSQL(ex.getMessage());
        }
        return allDetails;

    }


}
