package proyekuas.uas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.Lelang;
import proyekuas.uas.entity.User;

public interface LelangService {
    void addLelang(Lelang lelang);
    void addLelang(Long id, BigDecimal bid, User user);
    void beliSekarang(Long id, User user);
    List<Lelang> getAllLelang(Long id);
    LocalDateTime getWaktuLelangSelesai(Barang barang);
    void prosesLelangSelesai(Barang barang);
    Barang mulaiLelang(Long barangId, User currentUser);
}
