package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotBlank;

public record RecordDiagnosisCommand(
        @NotBlank String diagnosis,
        String notes
) {}