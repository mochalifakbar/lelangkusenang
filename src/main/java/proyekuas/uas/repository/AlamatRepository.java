package proyekuas.uas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import proyekuas.uas.entity.Alamat;

public interface AlamatRepository extends JpaRepository<Alamat, Long> {
    @Query("SELECT a FROM Alamat a WHERE a.user.id = :id")
    Alamat findByUser(Long id);
}
