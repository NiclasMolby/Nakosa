package dk.sdu.mmmi.nakosa;

/**
 * Created by Niclas on 05-03-2018.
 */

public class DatabaseAdvertisement {
    private String key;
    public String Product;
    public String Description;
    public String Seller;
    public String Price;
    public String ImagePath;
    public String ImageDownloadPath;

    public DatabaseAdvertisement() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

