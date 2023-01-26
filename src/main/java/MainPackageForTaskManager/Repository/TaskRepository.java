package MainPackageForTaskManager.Repository;

import MainPackageForTaskManager.Entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface TaskRepository extends CrudRepository<Tasks,Integer> {
    List<Tasks> findAllByUserId(int UserId);
    Page<Tasks> findAllByUserId(int userId, Pageable pageable);
    // this query is comparing dates and times
    @Query("SELECT t FROM Tasks t WHERE t.user.id = :userId AND ((t.startDate <= :endDate AND t.endDate >= :startDate) OR (t.startDate <= :startDate AND t.endDate >= :startDate) OR (t.startDate <= :endDate AND t.endDate >= :endDate))")
    List<Tasks> findOverlappingTasks(@Param("userId") Integer userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);}