package bd.bd;

import javafx.collections.ObservableList;
public interface OrderDAO {
    ObservableList<OrderData> loadOrderData();
    void deleteOrderFromDatabase(int orderID);
}
