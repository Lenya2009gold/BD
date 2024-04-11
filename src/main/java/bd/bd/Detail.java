package bd.bd;

public class Detail {
    private int id;
    private String detailsNumber;
    private int amount;
    private String manufactur;
    private String providerName;
    public Detail(int id, String detailsNumber, int amount, String manufactur,String providerName) {
        this.id = id;
        this.detailsNumber = detailsNumber;
        this.amount = amount;
        this.manufactur = manufactur;
        this.providerName = providerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufactur() {
        return manufactur;
    }

    public void setManufactur(String manufactur) {
        this.manufactur = manufactur;
    }

    public String getDetailsNumber() {
        return detailsNumber;
    }

    public void setDetailsNumber(String detailsNumber) {
        this.detailsNumber = detailsNumber;
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

    public void setProviderName(String name) {
        this.providerName = name;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", detailsNumber='" + detailsNumber + '\'' +
                ", amount=" + amount +
                ", manufactur='" + manufactur + '\'' +
                ", Name='" + providerName + '\'' +
                '}';
    }
}
