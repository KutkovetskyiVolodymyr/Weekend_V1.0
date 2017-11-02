package weekend.repository;

import weekend.model.Weekend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WeekendRepository extends JpaRepository<Weekend,Long> {
     List<Weekend> findAllByWeekendBetween(Date after, Date before);
}
