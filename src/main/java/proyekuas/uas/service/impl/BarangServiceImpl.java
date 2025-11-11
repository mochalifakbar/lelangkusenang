package proyekuas.uas.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.BarangRepository;
import proyekuas.uas.service.BarangService;

import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;

@Service
public class BarangServiceImpl implements BarangService {
    @Autowired
    private BarangRepository barangRepository;

    @Override
    public List<Barang> findByPenjualId(Long id) {
        return barangRepository.findByPenjualId(id);
    }

    @Override
    public void addBarang(Barang barang) {
        barangRepository.save(barang);
    }

    @Override
    public Barang findById(Long id) {
        return barangRepository.findById(id).orElse(null);
    }

    @Override
    public void updateBarang(Barang barang) {
        barangRepository.save(barang);
    }
    
    @Override
    public void deleteBarang(Long id) {
        barangRepository.deleteById(id);
    }

    @Override
    public List<Barang> findAll() {
        return barangRepository.findAll();
    }

    @Override
    public void addBarang(Barang barang, MultipartFile file, User user) throws IOException {
        barang.setPenjual(user);

        String directoryPath = "C:/xampp/htdocs/praktikum/uas/src/main/resources/static/fotobarang/";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + user.getId() + barang.getNamaBarang() + ".jpg";
        file.transferTo(new File(filePath));
        barang.setFotoBarang("/fotobarang/" + user.getId() + barang.getNamaBarang() + ".jpg");
        addBarang(barang);
    }

    @Override
    public void updateBarang(Barang barangEdit, MultipartFile file, User user) throws IOException {
        Barang barang = findById(barangEdit.getId());

        if(barangEdit.getNamaBarang() != null){
            barang.setNamaBarang(barangEdit.getNamaBarang());
        }
        if(barangEdit.getDeskripsiBarang() != null){
            barang.setDeskripsiBarang(barangEdit.getDeskripsiBarang());
        }
        if(barangEdit.getHargaAwal() != null){
            barang.setHargaAwal(barangEdit.getHargaAwal());
        }
        if(barangEdit.getKelipatanBid() != null){
            barang.setKelipatanBid(barangEdit.getKelipatanBid());
        }
        if(barangEdit.getBatasHarga() != null){
            barang.setBatasHarga(barangEdit.getBatasHarga());
        }

        if (!file.isEmpty()) {
            String directoryPath = "C:/xampp/htdocs/praktikum/uas/src/main/resources/static/fotobarang/";
            File directory = new File(directoryPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File oldFile = new File(directoryPath + user.getId() + barang.getNamaBarang() + ".jpg");
            if (oldFile.exists()) {
                oldFile.delete();
            }

            String filePath = directoryPath + user.getId() + barangEdit.getNamaBarang() + ".jpg";
            file.transferTo(new File(filePath));
            barang.setFotoBarang("/fotobarang/" + user.getId() + barangEdit.getNamaBarang() + ".jpg");
        }
        updateBarang(barang);
    }

    @Override
    public List<Barang> findLelang(User user) {
        return findAll().stream()
            .filter(b -> !b.getPenjual().getId().equals(user.getId()))
            .filter(b -> b.getStatus() == Barang.Status.DILELANG)
            .filter(b -> b.getBatasWaktu() != null && b.getBatasWaktu().isAfter(LocalDateTime.now())) // Pastikan batas waktu ada dan belum lewat
            .collect(Collectors.toList());
    }

    @Override
    public Page<Barang> findPaginatedAndFiltered(String searchKeyword, String statusFilter, Long userId, Pageable pageable) {
        Specification<Barang> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("penjual").get("id"), userId));

            if (StringUtils.hasText(statusFilter)) {
                try {
                    // Konversi string dari URL (e.g., "dilelang") menjadi Enum
                    Barang.Status statusEnum = Barang.Status.valueOf(statusFilter.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
                } catch (IllegalArgumentException e) {
                    // Jika nilai status tidak valid, abaikan filter
                    System.err.println("Nilai filter status tidak valid: " + statusFilter);
                }
            }

            // ... logic untuk searchKeyword tetap sama ...
            if (StringUtils.hasText(searchKeyword)) {
                Predicate searchByName = criteriaBuilder.like(criteriaBuilder.lower(root.get("namaBarang")), "%" + searchKeyword.toLowerCase() + "%");
                Predicate searchById = criteriaBuilder.like(root.get("id").as(String.class), "%" + searchKeyword + "%");
                predicates.add(criteriaBuilder.or(searchByName, searchById));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return barangRepository.findAll(spec, pageable);
    }
}
