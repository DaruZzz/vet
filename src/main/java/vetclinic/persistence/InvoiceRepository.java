package vetclinic.persistence;

import vetclinic.domain.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    @Query("""
        SELECT i
        FROM Invoice i
        LEFT JOIN FETCH i.items
        LEFT JOIN FETCH i.payments
        WHERE i.invoiceId = :id
        """)
    Optional<Invoice> findByIdWithDetails(Long id);
}