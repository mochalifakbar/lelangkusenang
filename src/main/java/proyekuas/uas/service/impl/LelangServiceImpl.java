package proyekuas.uas.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.Barang.Status;
import proyekuas.uas.entity.Lelang;
import proyekuas.uas.entity.Transaksi;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.LelangRepository;
import proyekuas.uas.service.BarangService;
import proyekuas.uas.service.TransaksiService;


@Service
public class LelangServiceImpl implements proyekuas.uas.service.LelangService{
    private LelangRepository lelangRepository;
    private BarangService barangService;
    private TransaksiService transaksiService;

    @Autowired
    public LelangServiceImpl(LelangRepository lelangRepository, BarangService barangService, TransaksiService transaksiService) {
        this.lelangRepository = lelangRepository;
        this.barangService = barangService;
        this.transaksiService = transaksiService;
    }

    @Override
    public void addLelang(Lelang lelang) {
        lelangRepository.save(lelang);
    }

    @Override
    public List<Lelang> getAllLelang(Long id) {
        List<Lelang> lelang = lelangRepository.findAll().stream().filter(l -> l.getBarang().getId().equals(id)).toList();
        return lelang;
    }

    @Override
    public LocalDateTime getWaktuLelangSelesai(Barang barang) {
        LocalDateTime waktuLelangSelesai = lelangRepository.findAll()
                                .stream()
                                .filter(lelang -> lelang.getBarang().equals(barang))
                                .sorted(Comparator.comparing(Lelang::getWaktuBid))
                                .reduce((first, second) -> second)
                                .map(Lelang::getWaktuBid)
                                .orElse(null);

        if(waktuLelangSelesai == null){
            waktuLelangSelesai = LocalDateTime.now();
        }
        return waktuLelangSelesai;
    }

    @Override
    public void addLelang(Long id, BigDecimal bid, User user) {
        Barang barang = barangService.findById(id);
        Lelang lelang = new Lelang();
        lelang.setBarang(barang);
        lelang.setPelelang(user);
        lelang.setHargaBid(bid);
        lelang.setWaktuBid(LocalDateTime.now());

        barang.setHargaTertinggi(bid);
        barang.setPemenang(user);
        if(barang.getBatasHarga().equals(bid)){
            barang.setStatus(Status.TERJUAL);
            barangService.updateBarang(barang);
            prosesLelangSelesai(barang);
        }else{
            barangService.updateBarang(barang);
        }
        addLelang(lelang);
        barangService.updateBarang(barang);
    }

    @Override
    public void beliSekarang(Long id, User user) {
        Barang barang = barangService.findById(id);
        String status = barang.getStatus().toString();
        if(barang != null && status == null) {
            barang.setStatus(Status.TERJUAL);
            barang.setPemenang(user);
            barangService.updateBarang(barang);
            prosesLelangSelesai(barang);
        }
    }

    @Override
    public void prosesLelangSelesai(Barang barang) {
       Transaksi transaksi = new Transaksi();
       transaksi.setPenjual(barang.getPenjual());
       transaksi.setPembeli(barang.getPemenang());
       transaksi.setBarang(barang);
       transaksi.setHarga(barang.getHargaTertinggi());
       transaksi.setWaktuPembelian(getWaktuLelangSelesai(barang));
       transaksiService.addTransaksi(transaksi);
    }

    @Override
    public Barang mulaiLelang(Long barangId, User currentUser) {
        Barang barang = barangService.findById(barangId);

        // Validasi
        if (barang == null || !barang.getPenjual().getId().equals(currentUser.getId()) || barang.getStatus() != Status.TERSEDIA) {
            throw new IllegalStateException("Barang tidak valid untuk memulai lelang.");
        }

        // --- LOGIKA BARU DITAMBAHKAN DI SINI ---
        // Atur batas waktu lelang, misalnya 7 hari dari sekarang
        LocalDateTime sekarang = LocalDateTime.now();
        LocalDateTime batasWaktuBaru = sekarang.plusDays(7); 
        barang.setBatasWaktu(batasWaktuBaru);
        
        // Ubah status menjadi DILELANG
        barang.setStatus(Status.DILELANG);

        // Simpan perubahan ke database
        barangService.updateBarang(barang);
        return barang;
    }
}