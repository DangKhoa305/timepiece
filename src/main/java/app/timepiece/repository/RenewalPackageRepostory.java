package app.timepiece.repository;


import app.timepiece.entity.RenewalPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenewalPackageRepostory extends JpaRepository<RenewalPackage, Long> {

}
