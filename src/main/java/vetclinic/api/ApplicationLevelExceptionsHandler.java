package vetclinic.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import vetclinic.application.exceptions.*;

import java.time.Instant;

@ControllerAdvice
public class ApplicationLevelExceptionsHandler {

    @ExceptionHandler(VeterinarianNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleVeterinarianNotFoundException(VeterinarianNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Veterinarian Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MedicationNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleMedicationNotFoundException(MedicationNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Medication Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(PromotionNotFoundException.class)
    @ResponseBody
    public ProblemDetail handlePromotionNotFoundException(PromotionNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Promotion Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(LoyaltyTierNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleLoyaltyTierNotFoundException(LoyaltyTierNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Loyalty Tier Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(DiscountNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleDiscountNotFoundException(DiscountNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Discount Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(AvailabilityNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleAvailabilityNotFoundException(AvailabilityNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Availability Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(PetOwnerNotFoundException.class)
    @ResponseBody
    public ProblemDetail handlePetOwnerNotFoundException(PetOwnerNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Pet Owner Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseBody
    public ProblemDetail handlePetNotFoundException(PetNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Pet Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(VisitNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleVisitNotFoundException(VisitNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Visit Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
    @ExceptionHandler(MedicationIncompatibilityNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleMedicationIncompatibilityNotFoundException(MedicationIncompatibilityNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Medication Incompatibility Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}