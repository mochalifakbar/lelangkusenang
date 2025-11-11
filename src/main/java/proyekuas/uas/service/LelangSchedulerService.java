package proyekuas.uas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.Barang.Status;
import proyekuas.uas.entity.Transaksi;

@Service
public class LelangSchedulerService {

    private final LelangService lelangService;
    private final TransaksiService transaksiService;
    private final BarangService barangService;

    public LelangSchedulerService(LelangService lelangService, TransaksiService transaksiService, BarangService barangService) {
        this.lelangService = lelangService;
        this.transaksiService = transaksiService;
        this.barangService = barangService;
    }

     @Scheduled(fixedRate = 60000)
    public void cekLelangSelesai() {
        List<Barang> lelangLewatWaktu = barangService.findAll().stream()
            .filter(barang -> barang.getStatus() == Status.DILELANG && barang.getBatasWaktu().isBefore(LocalDateTime.now()))
            .collect(Collectors.toList());

        for (Barang barang : lelangLewatWaktu) {
            if (barang.getPemenang() != null) {
                // Ini seharusnya sudah ditangani oleh addLelang/beliSekarang, tapi sebagai pengaman
                barang.setStatus(Status.TERJUAL);
                lelangService.prosesLelangSelesai(barang);
            } else {
                // Tidak ada pemenang, lelang kadaluarsa
                barang.setStatus(Status.KADALUARSA);
            }
            barangService.updateBarang(barang);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void cekTransaksiSelesai(){
        List<Transaksi> listTransaksi = transaksiService.findAll().stream().filter(transaksi -> transaksi.getBatasWaktu().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        for (Transaksi transaksi : listTransaksi) {
            if(transaksi.getStatusPembayaran().equals(Transaksi.StatusPembayaran.pending)) {
                transaksi.setStatusPembayaran(Transaksi.StatusPembayaran.gagal);
                transaksiService.updateTransaksi(transaksi);
            }
        }
    }
}
