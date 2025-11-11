package proyekuas.uas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import proyekuas.uas.entity.Barang;

public interface BarangRepository extends JpaRepository<Barang, Long>, JpaSpecificationExecutor<Barang> {
    @Query("SELECT b FROM Barang b WHERE b.penjual.id = :id")
    List<Barang> findByPenjualId(Long id);
}
