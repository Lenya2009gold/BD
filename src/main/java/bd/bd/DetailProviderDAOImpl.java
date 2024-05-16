package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailProviderDAOImpl implements DetailProviderDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseHandler.connect();
    }
        public  ObservableList<DetailProviderData>  loadDetails()
    {
        ObservableList<DetailProviderData> detailsproviders= FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT \"Detail\".\"DetailsNumber\", \"Detail\".\"Manufactur\", " +
                "\"Provider\".\"Name\" AS \"ProviderName\", \"Provider\".\"Detail_delivery\", \"Provider\".\"Price\", \"Provider\".\"Amount_available\"\n" +
                "FROM \"Detail\"\n" +
                "JOIN \"Provider\" ON \"Detail\".\"Provider_id\" = \"Provider\".\"Provider_id\";";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT))
        {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                detailsproviders.add(new DetailProviderData(rs.getString("detailsNumber"),
                        rs.getString("manufactur"),
                        rs.getInt("Amount_available"),
                        rs.getInt("price"),
                        rs.getString("providerName"),
                        rs.getString("Detail_delivery")));
            }

        }
        catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
        return detailsproviders;

    }


}
