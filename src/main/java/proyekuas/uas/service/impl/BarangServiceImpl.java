package proyekuas.uas.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.BarangRepository;
import proyekuas.uas.service.BarangService;

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
            .filter(b -> b.getPenjual().getId() != user.getId())
            .filter(b -> b.getBatasWaktu().isAfter(LocalDateTime.now()))
            .collect(Collectors.toList());
    }
}
