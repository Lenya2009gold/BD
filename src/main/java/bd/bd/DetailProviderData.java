package bd.bd;

public class DetailProviderData {
    private String detailsNumber;
    private String manufactur;
    private int amount;
    private int price;

    private String providerName;
    private String detailDelivery;
    private String adress;
    private String contact;

    public DetailProviderData(String detailsNumber, String manufactur, int amount,int price, String providerName, String detailDelivery) {
        this.detailsNumber = detailsNumber;
        this.manufactur = manufactur;
        this.amount = amount;
        this.price = price;
        this.providerName = providerName;
        this.detailDelivery = detailDelivery;
    }

    public String getDetailsNumber() {
        return detailsNumber;
    }

    public void setDetailsNumber(String detailsNumber) {
        this.detailsNumber = detailsNumber;
    }

    public String getManufactur() {
        return manufactur;
    }

    public void setManufactur(String manufactur) {
        this.manufactur = manufactur;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDetailDelivery() {
        return detailDelivery;
    }

    public void setDetailDelivery(String detailDelivery) {
        this.detailDelivery = detailDelivery;
    }

    @Override
    public String toString() {
        return "DetailProviderData{" +
                "detailsNumber='" + detailsNumber + '\'' +
                ", manufactur='" + manufactur + '\'' +
                ", amount=" + amount +
                ", providerName='" + providerName + '\'' +
                ", detailDelivery='" + detailDelivery + '\'' +
                ", pirce=" + price +
                '}';
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
