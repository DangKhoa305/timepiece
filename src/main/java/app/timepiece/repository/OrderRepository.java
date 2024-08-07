package app.timepiece.repository;

import app.timepiece.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.watch.user.id = :sellerId")
    List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);

    Optional<Order> findByUserIdAndWatchId(Long userId, Long watchId);
}
