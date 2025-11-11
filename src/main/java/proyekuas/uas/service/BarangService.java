package proyekuas.uas.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.User;

@Service
public interface BarangService {
    List<Barang> findByPenjualId(Long id);
    void addBarang(Barang barang);
    void addBarang(Barang barang, MultipartFile file, User user) throws IOException;
    Barang findById(Long id);
    void updateBarang(Barang barang);
    void updateBarang(Barang barangEdit, MultipartFile file, User user) throws IOException;
    void deleteBarang(Long id);
    List<Barang> findAll();
    List<Barang> findLelang(User user);

    Page<Barang> findPaginatedAndFiltered(String searchKeyword, String statusFilter, Long userId, Pageable pageable);
}
