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
    List<Watch> findByStatusOrderByCreateDateDesc(String status);

//    @Query("SELECT w FROM Watch w WHERE w.status <> :status")
//    List<Watch> findAllExcludingStatus(@Param("status") String status);

    @Query("SELECT w FROM Watch w ORDER BY w.expired ASC, w.startDate ASC")
    List<Watch> findAllByExpiredFalseOrderByStartDateAsc();

    @Query("SELECT w FROM Watch w WHERE w.status NOT IN :statuses")
    List<Watch> findAllExcludingStatuses(@Param("statuses") List<String> statuses);
//    @Query("SELECT w FROM Watch w " +
//            "WHERE (:price IS NULL OR w.price = :price) " +
//            "AND (:address IS NULL OR w.area = :area) " +
//            "AND (:type IS NULL OR w.watchType.typeName = :type) " +
//            "AND (:brand IS NULL OR w.brand.brandName = :brand) " +
//            "AND (:watchStatus IS NULL OR w.watchStatus = :watchStatus)" +
//            "AND (:status IS NULL OR w.status = :status) " +
//            "AND (:accessories IS NULL OR w.accessories = :accessories)" +
//            "AND (:name IS NULL OR w.name = :name)")
//    Page<Watch> searchWatches(
//            @Param("price") Double price,
//            @Param("area") String area,
//            @Param("type") String type,
//            @Param("brand") String brand,
//            @Param("watchStatus") String watchStatus,
//            @Param("status") String status,
//            @Param("accessories") String accessories,
//            @Param("name") String name,
//            Pageable pageable
//    );
//
//    @Query("SELECT w FROM Watch w " +
//            "WHERE (:keyword IS NULL OR " +
//            "LOWER(w.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(w.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(w.watchType.typeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(w.brand.brandName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(w.watchStatus) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(w.status) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(w.accessories) LIKE LOWER(CONCAT('%', :keyword, '%')))")
//    Page<Watch> searchWatchesByKeyword(
//            @Param("keyword") String keyword,
//            Pageable pageable
//    );

    @Query("SELECT w FROM Watch w WHERE " +
            "(:keyword IS NULL OR w.name LIKE %:keyword% OR w.brand.brandName LIKE %:keyword% OR w.watchType.typeName LIKE %:keyword%) AND " +
            "(:minPrice IS NULL OR w.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR w.price <= :maxPrice) AND " +
            "(:areaList IS NULL OR w.area IN :areaList) AND " +
            "(:typeList IS NULL OR w.watchType.typeName IN :typeList) AND " +
            "(:brandList IS NULL OR w.brand.brandName IN :brandList) AND " +
            "(:watchStatusList IS NULL OR w.watchStatus IN :watchStatusList) AND " +
            "(:status IS NULL OR w.status = :status) AND " +
            "(:accessoriesList IS NULL OR w.accessories IN :accessoriesList) AND " +
            "(:name IS NULL OR w.name LIKE %:name%)")
    Page<Watch> searchByKeywordAndFilter(@Param("keyword") String keyword,
                                         @Param("minPrice") Double minPrice,
                                         @Param("maxPrice") Double maxPrice,
                                         @Param("areaList") List<String> areaList,
                                         @Param("typeList") List<String> typeList,
                                         @Param("brandList") List<String> brandList,
                                         @Param("watchStatusList") List<String> watchStatusList,
                                         @Param("status") String status,
                                         @Param("accessoriesList") List<String> accessoriesList,
                                         @Param("name") String name,
                                         Pageable pageable);

    List<Watch> findByUserId(Long userId);
}
