package domain;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id")
    private Veterinarian veterinarian;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDate initialDate;
    private LocalDate finalDate;

    @OneToMany(
            mappedBy = "availability",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AvailabilityException> exceptions = new ArrayList<>();

    public Availability() {
    }

    // Business logic
    public void addException(AvailabilityException exception) {
        exceptions.add(exception);
        exception.setAvailability(this);
    }

    public void removeException(AvailabilityException exception) {
        exceptions.remove(exception);
        exception.setAvailability(null);
    }

    public boolean isAvailableOnDate(LocalDate date) {
        if (date.isBefore(initialDate) || date.isAfter(finalDate)) {
            return false;
        }

        if (!date.getDayOfWeek().equals(dayOfWeek)) {
            return false;
        }

        // Check if there's an exception for this date
        return exceptions.stream()
                .noneMatch(ex -> ex.getExceptionDate().equals(date));
    }

    // Getters and Setters
    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public List<AvailabilityException> getExceptions() {
        return List.copyOf(exceptions);
    }
}