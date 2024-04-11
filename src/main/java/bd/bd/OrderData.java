package bd.bd;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderData {
    private final SimpleIntegerProperty orderId = new SimpleIntegerProperty();
    private final SimpleStringProperty datetime = new SimpleStringProperty();
    private final SimpleStringProperty status = new SimpleStringProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty customerPhone = new SimpleStringProperty();
    private final SimpleStringProperty employeeName = new SimpleStringProperty();
    private final SimpleStringProperty detailsNumber = new SimpleStringProperty();
    private final SimpleIntegerProperty amount = new SimpleIntegerProperty();
    private final SimpleStringProperty providerName = new SimpleStringProperty();
    private final SimpleStringProperty detailDelivery = new SimpleStringProperty();

    public OrderData(int orderId, String datetime, String status, String customerName, String customerPhone,
                     String employeeFirstName, String detailsNumber, int amount, String providerName, String detailDelivery) {
        this.orderId.set(orderId);
        this.datetime.set(datetime);
        this.status.set(status);
        this.customerName.set(customerName);
        this.customerPhone.set(customerPhone);
        this.employeeName.set(employeeFirstName);
        this.detailsNumber.set(detailsNumber);
        this.amount.set(amount);
        this.providerName.set(providerName);
        this.detailDelivery.set(detailDelivery);
    }

    public String getDetailDelivery() {
        return detailDelivery.get();
    }

    public SimpleStringProperty detailDeliveryProperty() {
        return detailDelivery;
    }

    public void setDetailDelivery(String detailDelivery) {
        this.detailDelivery.set(detailDelivery);
    }

    public String getProviderName() {
        return providerName.get();
    }

    public SimpleStringProperty providerNameProperty() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName.set(providerName);
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public String getDetailsNumber() {
        return detailsNumber.get();
    }

    public SimpleStringProperty detailsNumberProperty() {
        return detailsNumber;
    }

    public void setDetailsNumber(String detailsNumber) {
        this.detailsNumber.set(detailsNumber);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public SimpleStringProperty employeeNameProperty() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public String getCustomerPhone() {
        return customerPhone.get();
    }

    public SimpleStringProperty customerPhoneProperty() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone.set(customerPhone);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getDatetime() {
        return datetime.get();
    }

    public SimpleStringProperty datetimeProperty() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime.set(datetime);
    }

    public int getOrderId() {
        return orderId.get();
    }

    public SimpleIntegerProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "orderId=" + orderId +
                ", datetime=" + datetime +
                ", status=" + status +
                ", customerName=" + customerName +
                ", customerPhone=" + customerPhone +
                ", employeeName=" + employeeName +
                ", detailsNumber=" + detailsNumber +
                ", amount=" + amount +
                ", providerName=" + providerName +
                ", detailDelivery=" + detailDelivery +
                '}';
    }
}
