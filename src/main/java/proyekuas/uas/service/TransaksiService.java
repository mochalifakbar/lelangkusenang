package proyekuas.uas.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.entity.Transaksi;
import proyekuas.uas.entity.User;

public interface TransaksiService {
    void addTransaksi(Transaksi transaksi);
    List<Transaksi> findByUserId(Long userId);
    List<Transaksi> findAll();
    void updateTransaksi(Transaksi transaksi);
    Transaksi findById(Long id);
    Transaksi konfirmasiPembayaran(Long id, MultipartFile file, User user) throws IOException;
}
