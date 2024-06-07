package app.timepiece.repository;

import app.timepiece.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u JOIN u.account a WHERE a.email = :email")
//    boolean existsByEmail(@Param("email") String email);
}
