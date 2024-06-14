package app.timepiece.repository;

import app.timepiece.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountEmail(String email);

    @Query("SELECT u.account.email FROM User u WHERE u.id = :userId")
    String findEmailByUserId(@Param("userId") Long userId);
}
