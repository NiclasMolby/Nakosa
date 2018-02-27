package dk.sdu.mmmi.nakosa;

import java.io.Serializable;

/**
 * Created by Antonio on 27-02-2018.
 */

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String gender;

    public User(String firstName, String lastName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
