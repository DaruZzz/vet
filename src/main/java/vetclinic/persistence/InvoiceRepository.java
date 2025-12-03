package vetclinic.persistence;

import vetclinic.domain.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

import vetclinic.application.outputDTO.DiscountUtilizationDTO;
import vetclinic.domain.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
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

    // UC 3.10: Get invoice history by pet owner
    @Query("""
        SELECT i
        FROM Invoice i
        WHERE i.petOwner.personId = :petOwnerId
        AND i.invoiceDate BETWEEN :startDate AND :endDate
        ORDER BY i.invoiceDate DESC
        """)
    List<Invoice> findByPetOwnerAndDateRange(
            @Param("petOwnerId") Long petOwnerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // UC 3.10: Get all invoice history
    @Query("""
        SELECT i
        FROM Invoice i
        WHERE i.invoiceDate BETWEEN :startDate AND :endDate
        ORDER BY i.invoiceDate DESC
        """)
    List<Invoice> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // UC 3.12: Analyze discount utilization
    @Query("""
    SELECT new vetclinic.application.outputDTO.DiscountUtilizationDTO(
        d.discountId,
        d.code,
        CAST(d.type AS string),
        d.usesCount,
        d.maxUses,
        COALESCE(SUM(i.discountAmount), 0.0),
        d.startDate,
        d.endDate,
        CASE WHEN CURRENT_DATE BETWEEN d.startDate AND d.endDate THEN true ELSE false END
    )
    FROM Discount d
    LEFT JOIN Invoice i ON i.invoiceDate BETWEEN :startDate AND :endDate
    WHERE d.startDate <= :endDate AND d.endDate >= :startDate
    GROUP BY d.discountId, d.code, d.type, d.usesCount, d.maxUses, d.startDate, d.endDate
    ORDER BY d.usesCount DESC
    """)
    List<DiscountUtilizationDTO> findDiscountUtilization(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}