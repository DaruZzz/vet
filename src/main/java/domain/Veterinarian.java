package domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Veterinarian extends Person {

    private String licenseNumber;
    private Integer yearsOfExperience;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "veterinarian_speciality",
            joinColumns = @JoinColumn(name = "veterinarian_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    private Set<Speciality> specialities = new HashSet<>();

    @OneToMany(
            mappedBy = "veterinarian",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Availability> availabilities = new ArrayList<>();

    public Veterinarian() {
    }

    // Business logic
    public void addAvailability(Availability availability) {
        availabilities.add(availability);
        availability.setVeterinarian(this);
    }

    public void removeAvailability(Availability availability) {
        availabilities.remove(availability);
        availability.setVeterinarian(null);
    }

    public void addSpeciality(Speciality speciality) {
        specialities.add(speciality);
    }

    public void removeSpeciality(Speciality speciality) {
        specialities.remove(speciality);
    }

    // Getters and Setters
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Set<Speciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(Set<Speciality> specialities) {
        this.specialities = specialities;
    }

    public List<Availability> getAvailabilities() {
        return List.copyOf(availabilities);
    }
}