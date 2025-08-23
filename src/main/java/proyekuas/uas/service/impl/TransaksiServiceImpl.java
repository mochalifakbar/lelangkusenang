package proyekuas.uas.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.entity.Transaksi;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.TransaksiRepository;
import proyekuas.uas.service.TransaksiService;

@Service
public class TransaksiServiceImpl implements TransaksiService {
    @Autowired
    private TransaksiRepository transaksiRepository;

    @Override
    public void addTransaksi(Transaksi transaksi) {
        transaksiRepository.save(transaksi);
    }

    @Override
    public List<Transaksi> findByUserId(Long userId) {
        return transaksiRepository.findByUserId(userId);
    }

    @Override
    public List<Transaksi> findAll() {
        return transaksiRepository.findAll();
    }

    @Override
    public void updateTransaksi(Transaksi transaksi) {
        transaksiRepository.save(transaksi);
    }

    @Override
    public Transaksi findById(Long id) {
        return transaksiRepository.findById(id).orElse(null);
    }

    @Override
    public Transaksi konfirmasiPembayaran(Long id, MultipartFile file, User user) throws IOException {
        Transaksi transaksi = findById(id);

        if (!file.isEmpty()) {
            String directoryPath = "C:/xampp/htdocs/praktikum/uas/src/main/resources/static/buktibayar/";
            File directory = new File(directoryPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = directoryPath + transaksi.getId() + ".jpg";

            file.transferTo(new File(filePath));
            transaksi.setFotoBuktiPembayaran("/buktibayar/" + transaksi.getId() + ".jpg");

            if(transaksi.getPembeli().getId().equals(user.getId())){
                transaksi.setStatusPembayaran(Transaksi.StatusPembayaran.sukses);
                transaksi.setKonfirmasiBayar(true);
                transaksi.setWaktuKonfirmasi(LocalDateTime.now());
                updateTransaksi(transaksi);
            }
        }

        return transaksi;
    }
}
