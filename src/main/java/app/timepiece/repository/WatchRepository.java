package app.timepiece.repository;
import app.timepiece.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByNameContainingIgnoreCase(String name);
    List<Watch> findByUserIdAndStatus(Long userId, String status);

    @Query("SELECT w FROM Watch w " +
            "WHERE (:price IS NULL OR w.price = :price) " +
            "AND (:address IS NULL OR w.address = :address) " +
            "AND (:type IS NULL OR w.watchType.typeName = :type) " +
            "AND (:brand IS NULL OR w.brand.brandName = :brand) " +
            "AND (:watchStatus IS NULL OR w.watchStatus = :watchStatus)" +
            "AND (:status IS NULL OR w.status = :status)")
    Page<Watch> searchWatches(
            @Param("price") Double price,
            @Param("address") String address,
            @Param("type") String type,
            @Param("brand") String brand,
            @Param("watchStatus") String watchStatus,
            @Param("status") String status,
            Pageable pageable
    );

}