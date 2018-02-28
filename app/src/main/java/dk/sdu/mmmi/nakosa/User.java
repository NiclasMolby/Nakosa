package dk.sdu.mmmi.nakosa;

import java.io.Serializable;

/**
 * Created by Antonio on 27-02-2018.
 */

public class User implements Serializable {

    private String firstName;
    private String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
