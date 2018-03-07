package dk.sdu.mmmi.nakosa;

/**
 * Created by Antonio on 27-02-2018.
 */

public class UserData {

    private String firstName;
    private String lastName;
    private static UserData instance;

    private UserData() {
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
