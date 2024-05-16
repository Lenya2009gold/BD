package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProviderDAOImpl implements ProviderDAO {
    String SQL_INSERT_PROVIDER = "INSERT INTO \"Provider\" (\"Name\", \"Adress\", \"Contact\", \"Price\", \"Detail_delivery\", \"Amount_available\") VALUES (?, ?, ?, ?, ?, ?) RETURNING \"Provider_id\";";
    String SQL_INSERT_DETAIL = "INSERT INTO \"Detail\" (\"DetailsNumber\", \"Manufactur\", \"Provider_id\") VALUES (?, ?, ?);";
    private Connection getConnection() throws SQLException {
        return DatabaseHandler.connect();
    }
    @Override
    public void addProvider(String Name, String Adress,String Contact,String Detail_delivery,int Price, int Amount_available,
     String DetailsNumber,String Manufactur) {
        ResultSet rs = null;
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement(SQL_INSERT_PROVIDER))
        {
            preparedStatement.setString(1,Name);
            preparedStatement.setString(2,Adress);
            preparedStatement.setString(3,Contact);
            preparedStatement.setInt(4,Price);
            preparedStatement.setString(5,Detail_delivery);
            preparedStatement.setInt(6,Amount_available);
            rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                int Provider_id=rs.getInt(1);

                try(PreparedStatement preparedStatement1=connection.prepareStatement(SQL_INSERT_DETAIL))
                {
                    preparedStatement1.setString(1,DetailsNumber);
                    preparedStatement1.setString(2,Manufactur);
                    preparedStatement1.setInt(3,Provider_id);
                    int af = preparedStatement1.executeUpdate();
                    if(af>0)
                    {
                        Alerts.showSuc("Новая запчасть добавлена.");
                    }
                }

            }


        } catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
    }
}
