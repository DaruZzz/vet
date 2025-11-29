package vetclinic.persistence;

import vetclinic.domain.InvoiceItem;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceItemRepository extends CrudRepository<InvoiceItem, Long> {
}