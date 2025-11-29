// ScheduleRestController.java
package vetclinic.api;

import vetclinic.application.VisitService;
import vetclinic.application.outputDTO.ScheduleInformation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleRestController {

    private final VisitService visitService;

    public ScheduleRestController(VisitService visitService) {
        this.visitService = visitService;
    }

    // UC 1.2: View Veterinarian's Schedule
    @GetMapping("/veterinarian/{veterinarianId}")
    public ScheduleInformation getVeterinarianSchedule(
            @PathVariable Long veterinarianId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return visitService.getVeterinarianSchedule(veterinarianId, date);
    }
}