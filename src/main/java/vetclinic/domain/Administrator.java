package vetclinic.domain;

import jakarta.persistence.Entity;

@Entity
public class Administrator extends Person {

    private String role; // e.g., "Receptionist", "Clinic Manager", "Inventory Manager"

    public Administrator() {
        super();
    }

    public Administrator(String role) {
        this.role = role;
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}