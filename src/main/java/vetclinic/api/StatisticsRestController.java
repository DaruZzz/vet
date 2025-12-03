package vetclinic.api;

import vetclinic.application.StatisticsService;
import vetclinic.application.outputDTO.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {

    private final StatisticsService statisticsService;

    public StatisticsRestController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    // UC 1.8: List specialities ordered by demand in a given period
    @GetMapping("/specialities/demand")
    public List<SpecialityDemandDTO> getSpecialitiesByDemand(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return statisticsService.getSpecialitiesByDemand(startDate, endDate);
    }

    // UC 1.9: List veterinarians ordered by demand in a given period
    @GetMapping("/veterinarians/demand")
    public List<VeterinarianDemandDTO> getVeterinariansByDemand(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return statisticsService.getVeterinariansByDemand(startDate, endDate);
    }

    // UC 2.6: List medication ordered by prescription in a period
    @GetMapping("/medications/prescriptions")
    public List<MedicationPrescriptionStatsDTO> getMedicationsByPrescription(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return statisticsService.getMedicationsByPrescription(startDate, endDate);
    }

    // UC 2.7: List veterinarians ordered by prescription of a medication in a period
    @GetMapping("/veterinarians/medication-prescriptions")
    public List<VeterinarianPrescriptionStatsDTO> getVeterinariansByMedicationPrescription(
            @RequestParam Long medicationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return statisticsService.getVeterinariansByMedicationPrescription(
                medicationId, startDate, endDate
        );
    }
}