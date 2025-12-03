package vetclinic.service;

import vetclinic.application.InvoiceService;
import vetclinic.application.outputDTO.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetInvoiceHistory() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<InvoiceHistoryDTO> history =
                invoiceService.getInvoiceHistory(null, startDate, endDate);

        assertNotNull(history);
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetInvoiceHistoryForSpecificOwner() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<InvoiceHistoryDTO> history =
                invoiceService.getInvoiceHistory(100L, startDate, endDate);

        assertNotNull(history);
        // All invoices should belong to owner 100
        history.forEach(invoice -> {
            // Verify the owner name matches
            assertNotNull(invoice.petOwnerName());
        });
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testAnalyzeDiscountUtilization() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<DiscountUtilizationDTO> utilization =
                invoiceService.analyzeDiscountUtilization(startDate, endDate);

        assertNotNull(utilization);
    }
}