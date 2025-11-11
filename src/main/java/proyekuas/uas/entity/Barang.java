package proyekuas.uas.entity;

import java.math.BigDecimal;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "barang")
public class Barang {
    public enum Status {
        TERSEDIA,
        DILELANG,
        TERJUAL,
        KADALUARSA,
        DITARIK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_penjual", nullable = false)
    private User penjual;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(name = "deskripsi_barang", nullable = false, columnDefinition = "TEXT")
    private String deskripsiBarang;

    @Column(name = "batas_harga", nullable = false, precision = 15, scale = 2)
    private BigDecimal batasHarga;

    @Column(name = "foto_barang", nullable = false)
    private String fotoBarang;

    @Column(name = "kelipatan_bid", nullable = false, precision = 15, scale = 2)
    private BigDecimal kelipatanBid;

    @Column(name = "harga_awal", nullable = false, precision = 15, scale = 2)
    private BigDecimal hargaAwal;

    @Column(name = "tanggal_masuk", nullable = false, updatable = false)
    private LocalDateTime tanggalMasuk;

    @Column(name = "batas_waktu", nullable = true)
    private LocalDateTime batasWaktu;

    @ManyToOne
    @JoinColumn(name = "pemenang", nullable = true)
    private User pemenang;

    @Column(name = "harga_tertinggi", nullable = true, precision = 15, scale = 2)
    private BigDecimal hargaTertinggi;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = Status.TERSEDIA;
        }

        if (this.tanggalMasuk == null) {
            this.tanggalMasuk = LocalDateTime.now();
        }
    }
}