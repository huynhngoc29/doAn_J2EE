package nhom02.doanmon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nhom02.doanmon.entity.Cake;

@Repository
public interface CakeRepository extends JpaRepository<Cake, Long> {
}
