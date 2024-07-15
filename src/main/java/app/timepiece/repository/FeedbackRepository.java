package app.timepiece.repository;

import app.timepiece.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByOrderId(Long orderId);

    @Query("SELECT f " +
            "FROM Feedback f " +
            "WHERE f.order.watch.id = :watchId")
    List<Feedback> findFeedbacksByWatchId(@Param("watchId") Long watchId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.order.watch.user.id = :sellerId AND f.parentFeedback IS NULL")
    Long countBySellerId(@Param("sellerId") Long sellerId);

   // List<Feedback> findByOrder_Watch_Seller_Id(Long sellerId);
}
