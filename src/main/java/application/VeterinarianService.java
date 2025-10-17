package application;

import application.exceptions.AvailabilityNotFoundException;
import application.exceptions.VeterinarianNotFoundException;
import application.inputDTO.AvailabilityCommand;
import application.inputDTO.AvailabilityExceptionCommand;
import application.mappers.AvailabilityMapper;
import application.outputDTO.AvailabilityInformation;
import domain.Availability;
import domain.AvailabilityException;
import domain.Veterinarian;
import persistence.AvailabilityRepository;
import persistence.VeterinarianRepository;
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