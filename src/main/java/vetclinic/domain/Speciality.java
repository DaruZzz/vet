package vetclinic.domain;

import jakarta.persistence.*;

@Entity
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialityId;

    @Column(unique = true)
    private String name;

    private String description;

    public Speciality() {
    }

    public Speciality(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Long specialityId) {
        this.specialityId = specialityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Speciality)) return false;
        Speciality that = (Speciality) o;
        return specialityId != null && specialityId.equals(that.specialityId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}