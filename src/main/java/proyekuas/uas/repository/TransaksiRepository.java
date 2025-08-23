package proyekuas.uas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import proyekuas.uas.entity.Transaksi;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long>{
    @Query("SELECT t FROM Transaksi t WHERE t.penjual.id = :userId OR t.pembeli.id = :userId")
    List<Transaksi> findByUserId(Long userId);
}
