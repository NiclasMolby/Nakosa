package dk.sdu.mmmi.nakosa;

import java.io.Serializable;

public class AdvertisementData implements Serializable {

    private String productName;
    private String description;
    private String price;
    private String seller;
    private String imagePath;

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getSeller() {
        return seller;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
