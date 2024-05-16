package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static bd.bd.controllers.Alerts.showSuc;

public class OrderDAOImpl implements OrderDAO{
    private Connection getConnection() throws SQLException {
        return DatabaseHandler.connect();
    }
    public ObservableList<OrderData> loadOrderData()
    {
        ObservableList<OrderData> orders = FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT\n" +
                "        o.\"Order_id\",\n" +
                "        o.\"Datetime\",\n" +
                "        o.\"Status\",\n" +
                "        c.\"Name\" AS Customer_name,\n" +
                "        c.\"Phone\" AS Customer_phone,\n" +
                "        e.\"FirstName\" AS Employee_first_name,\n" +
                "        det.\"DetailsNumber\",\n" +
                "        doo.\"amount\",\n" +
                "        p.\"Name\" AS Provider_name,\n" +
                "        p.\"Detail_delivery\"\n" +
                "    FROM\n" +
                "        \"Order\" o\n" +
                "        JOIN \"Customer\" c ON o.\"Customer_id\" = c.\"Customer_id\"\n" +
                "        JOIN \"Employee\" e ON o.\"Employee_id\" = e.\"Employee_id\"\n" +
                "        JOIN \"Detail_order\" doo ON o.\"Order_id\" = doo.\"Order_id\"\n" +
                "        JOIN \"Detail\" det ON doo.\"Detail_id\" = det.\"Detail_id\"\n" +
                "        JOIN \"Provider\" p ON det.\"Provider_id\" = p.\"Provider_id\";";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(new OrderData(rs.getInt("order_id"),
                        rs.getString("datetime"),
                        rs.getString("status"),
                        EncryptionUtility.decrypt(rs.getString("customer_name")),
                        EncryptionUtility.decrypt(rs.getString("customer_phone")),
                        EncryptionUtility.decrypt(rs.getString("employee_first_name")),
                        rs.getString("detailsnumber"),
                        rs.getInt("amount"),
                        rs.getString("provider_name"),
                        rs.getString("detail_delivery")));

            }
        } catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));
        }
        return orders;

    }
    public void deleteOrderFromDatabase(int orderID)
    {
        String SQL_DELETE = "DELETE FROM \"Order\" WHERE \"Order_id\" = ?;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE))
        {
            pstmt.setInt(1,orderID);
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0 )
            {
                showSuc("All deleted");

            }
            else
            {
                Alerts.showErr("all bad");
            }


        }  catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));        }
    }



}
