package vetclinic.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import vetclinic.domain.exceptions.BatchExpiredException;
import vetclinic.domain.exceptions.DiscountExpiredException;
import vetclinic.domain.exceptions.DiscountMaxUsesExceededException;
import vetclinic.domain.exceptions.InsufficientStockException;

import java.time.Instant;

@ControllerAdvice
public class DomainLevelExceptionsHandler {

    @ExceptionHandler(BatchExpiredException.class)
    @ResponseBody
    public ProblemDetail handleBatchExpiredException(BatchExpiredException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Batch Expired");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseBody
    public ProblemDetail handleInsufficientStockException(InsufficientStockException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Insufficient Stock");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(DiscountExpiredException.class)
    @ResponseBody
    public ProblemDetail handleDiscountExpiredException(DiscountExpiredException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Discount Expired");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(DiscountMaxUsesExceededException.class)
    @ResponseBody
    public ProblemDetail handleDiscountMaxUsesExceededException(DiscountMaxUsesExceededException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Discount Max Uses Exceeded");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}