package proyekuas.uas.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaksi")
public class Transaksi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "penjual_id", nullable = false)
    private User penjual;

    @ManyToOne
    @JoinColumn(name = "pembeli_id", nullable = false)
    private User pembeli;

    @ManyToOne
    @JoinColumn(name = "barang_id", nullable = false)
    private Barang barang;

    @Column(name = "harga", nullable = false)
    private Double harga;

    @Column(name = "waktu_pembelian", nullable = false)
    private LocalDateTime waktuPembelian;

    @Column(name = "batas_waktu", nullable = false)
    private LocalDateTime batasWaktu;

    @Column(name = "waktu_pembayaran", nullable = true)
    private LocalDateTime waktuPembayaran;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pembayaran", nullable = true)
    private StatusPembayaran statusPembayaran;

    public enum StatusPembayaran {
        pending, sukses, gagal
    }

    @Column(name = "konfirmasi_bayar", nullable = true)
    private Boolean konfirmasiBayar;

    @Column(name = "waktu_konfirmasi", nullable = true)
    private LocalDateTime waktuKonfirmasi;

    @Column(name = "foto_bukti_pembayaran", nullable = true)
    private String fotoBuktiPembayaran;

    @PrePersist
    public void prePersist() {
        this.batasWaktu = this.waktuPembelian.plusDays(3);
        this.statusPembayaran = StatusPembayaran.pending;
        this.konfirmasiBayar = false;
    }
}