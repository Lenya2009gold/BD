package bd.bd;

public class Provider {
    private int id;
    private String name;
    private String detailDelivery;
    private int availableAmount;

    public Provider(int id, String name, String detailDelivery, int avaliableAmount) {
        this.id = id;
        this.name = name;
        this.detailDelivery = detailDelivery;
        this.availableAmount = avaliableAmount;
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
        return name + " " + detailDelivery; // Измените это в соответствии с вашими требованиями
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }
}
