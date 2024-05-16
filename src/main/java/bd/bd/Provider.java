package bd.bd;

public class Provider {
    private int id;
    private String name;
    private String detailDelivery;
    private String adress;
    private String contact;
    private int price;
    private int availableAmount;
    private String detailsNumber;
    private String manufactur;

    public Provider(String name, String detailDelivery, String adress, String contact, int price, int availableAmount, String detailsNumber, String manufactur) {
        this.name = name;
        this.adress = adress;
        this.contact = contact;
        this.price = price;
        this.detailDelivery = detailDelivery;
        this.availableAmount = availableAmount;
        this.detailsNumber = detailsNumber;
        this.manufactur = manufactur;
    }

    public String getDetail_delivery() {
        return detailDelivery;
    }

    public void setDetail_delivery(String detail_delivery) {
        this.detailDelivery = detail_delivery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return name + " " + detailDelivery;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }
}
