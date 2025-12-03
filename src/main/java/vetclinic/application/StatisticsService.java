package vetclinic.application;

import vetclinic.application.outputDTO.*;
import vetclinic.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {

    private final VisitRepository visitRepository;
    private final MedicationPrescriptionRepository medicationPrescriptionRepository;

    public StatisticsService(VisitRepository visitRepository,
                             MedicationPrescriptionRepository medicationPrescriptionRepository) {
        this.visitRepository = visitRepository;
        this.medicationPrescriptionRepository = medicationPrescriptionRepository;
    }

    // UC 1.8: List specialities ordered by demand in a given period
    @Transactional(readOnly = true)
    public List<SpecialityDemandDTO> getSpecialitiesByDemand(LocalDate startDate, LocalDate endDate) {
        return visitRepository.findSpecialitiesByDemand(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    // UC 1.9: List veterinarians ordered by demand in a given period
    @Transactional(readOnly = true)
    public List<VeterinarianDemandDTO> getVeterinariansByDemand(LocalDate startDate, LocalDate endDate) {
        return visitRepository.findVeterinariansByDemand(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    // UC 2.6: List medication ordered by prescription in a given period
    @Transactional(readOnly = true)
    public List<MedicationPrescriptionStatsDTO> getMedicationsByPrescription(LocalDate startDate, LocalDate endDate) {
        return medicationPrescriptionRepository.findMedicationsByPrescriptionCount(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }

    // UC 2.7: List veterinarians ordered by prescription of a medication in a given period
    @Transactional(readOnly = true)
    public List<VeterinarianPrescriptionStatsDTO> getVeterinariansByMedicationPrescription(
            Long medicationId,
            LocalDate startDate,
            LocalDate endDate) {
        return medicationPrescriptionRepository.findVeterinariansByMedicationPrescription(
                medicationId,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }
}