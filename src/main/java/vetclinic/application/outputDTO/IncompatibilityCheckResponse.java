package vetclinic.application.outputDTO;

import java.util.List;

public record IncompatibilityCheckResponse(
        boolean hasIncompatibility,
        List<IncompatibilityAlertDTO> alerts
) {}