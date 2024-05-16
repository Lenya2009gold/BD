package bd.bd;
import bd.bd.Customer;
import javafx.collections.ObservableList;

public interface CustomerDAO {
    ObservableList<Customer> getAllCustomers();
    void addCustomer(String nameCustomer, String phoneCustomer, String vinAutoSQL, String dateAuto, String brandAutoSQL,String modelAutoSQL);
    void updateCustomerNameDatabase(int orderId, String newName);
    void updateOrderStatusInDatabase(int orderId, String newStatus);
    void updatePhoneInDatabase(int orderId, String newStatus);
    Customer getCustomerById(int id);
    void updateCustomer(Customer customer);
    void deleteCustomer(int id);

}
