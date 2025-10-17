package vetclinic.application;

import vetclinic.application.exceptions.AvailabilityNotFoundException;
import vetclinic.application.exceptions.VeterinarianNotFoundException;
import vetclinic.application.inputDTO.AvailabilityCommand;
import vetclinic.application.inputDTO.AvailabilityExceptionCommand;
import vetclinic.application.mappers.AvailabilityMapper;
import vetclinic.application.outputDTO.AvailabilityInformation;
import vetclinic.domain.Availability;
import vetclinic.domain.AvailabilityException;
import vetclinic.domain.Veterinarian;
import vetclinic.persistence.AvailabilityRepository;
import vetclinic.persistence.VeterinarianRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;
    private final AvailabilityRepository availabilityRepository;

    public VeterinarianService(VeterinarianRepository veterinarianRepository,
                               AvailabilityRepository availabilityRepository) {
        this.veterinarianRepository = veterinarianRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // UC 1.7: Manage Veterinarian Availability
    @Transactional
    public Long addAvailability(Long veterinarianId, AvailabilityCommand command) {
        Veterinarian veterinarian = veterinarianRepository.findByIdWithAvailabilities(veterinarianId)
                .orElseThrow(() -> new VeterinarianNotFoundException(
                        "Veterinarian with id " + veterinarianId + " not found"
                ));

        Availability availability = AvailabilityMapper.commandToDomain(command);
        veterinarian.addAvailability(availability);

        Availability saved = availabilityRepository.save(availability);
        return saved.getAvailabilityId();
    }

    @Transactional
    public void updateAvailability(Long availabilityId, AvailabilityCommand command) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException(
                        "Availability with id " + availabilityId + " not found"
                ));

        availability.setDayOfWeek(command.dayOfWeek());
        availability.setStartTime(command.startTime());
        availability.setEndTime(command.endTime());
        availability.setInitialDate(command.initialDate());
        availability.setFinalDate(command.finalDate());

        availabilityRepository.save(availability);
    }

    @Transactional
    public void deleteAvailability(Long veterinarianId, Long availabilityId) {
        Veterinarian veterinarian = veterinarianRepository.findByIdWithAvailabilities(veterinarianId)
                .orElseThrow(() -> new VeterinarianNotFoundException(
                        "Veterinarian with id " + veterinarianId + " not found"
                ));

        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException(
                        "Availability with id " + availabilityId + " not found"
                ));

        veterinarian.removeAvailability(availability);
        availabilityRepository.delete(availability);
    }

    @Transactional
    public void addAvailabilityException(Long availabilityId, AvailabilityExceptionCommand command) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException(
                        "Availability with id " + availabilityId + " not found"
                ));

        AvailabilityException exception = new AvailabilityException();
        exception.setExceptionDate(command.exceptionDate());
        exception.setStartTime(command.startTime());
        exception.setEndTime(command.endTime());
        exception.setReason(command.reason());

        availability.addException(exception);
        availabilityRepository.save(availability);
    }

    @Transactional
    public void deleteAvailabilityException(Long availabilityId, Long exceptionId) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException(
                        "Availability with id " + availabilityId + " not found"
                ));

        AvailabilityException exceptionToRemove = availability.getExceptions().stream()
                .filter(ex -> ex.getExceptionId().equals(exceptionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Exception with id " + exceptionId + " not found"
                ));

        availability.removeException(exceptionToRemove);
        availabilityRepository.save(availability);
    }

    public List<AvailabilityInformation> getVeterinarianAvailabilities(Long veterinarianId) {
        Veterinarian veterinarian = veterinarianRepository.findByIdWithAvailabilities(veterinarianId)
                .orElseThrow(() -> new VeterinarianNotFoundException(
                        "Veterinarian with id " + veterinarianId + " not found"
                ));

        return veterinarian.getAvailabilities().stream()
                .map(AvailabilityMapper::toInformation)
                .collect(Collectors.toList());
    }
}