package bd.bd;

import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface DetailDAO {
    Detail getDetailById(int id);
    void addDetail(Detail detail);
    void updateDetail(Detail detail);
    void deleteDetail(int id);
    void updateDetailAmount(int detailId, int newAmount) throws SQLException;
    int getAvailableAmount(int detailId) throws SQLException;
    ObservableList<Detail> loadDetails();
    void insertDetailOrder(int seletctedDetailsid, int orderAmount, Date sqlDate, String status, int selectedCustomerId, int selectedEmployeeId);
}
