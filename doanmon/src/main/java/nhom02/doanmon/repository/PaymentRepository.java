package nhom02.doanmon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nhom02.doanmon.entity.Payment;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);
    List<Payment> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
    long countByStatusIn(List<String> statuses);
    long countByUserIdAndUserReadFalse(Long userId);
    long countByAdminReadFalse();
}
