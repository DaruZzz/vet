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
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    public void testGetInvoiceHistory() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<InvoiceHistoryDTO> history =
                invoiceService.getInvoiceHistory(null, startDate, endDate);

        assertNotNull(history);
        // From data-test.sql, should have 3 invoices
        assertEquals(3, history.size());
    }

    @Test
    public void testGetInvoiceHistoryForSpecificOwner() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<InvoiceHistoryDTO> history =
                invoiceService.getInvoiceHistory(100L, startDate, endDate);

        assertNotNull(history);
        // Pet owner 100 has 2 invoices
        assertEquals(2, history.size());

        history.forEach(invoice -> {
            assertTrue(invoice.petOwnerName().contains("Test"));
        });
    }

    @Test
    public void testAnalyzeDiscountUtilization() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<DiscountUtilizationDTO> utilization =
                invoiceService.analyzeDiscountUtilization(startDate, endDate);

        assertNotNull(utilization);
        // Should have at least the test discount
        assertFalse(utilization.isEmpty());
    }
}