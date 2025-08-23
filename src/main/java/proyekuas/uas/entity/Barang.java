package proyekuas.uas.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_penjual", nullable = false)
    private User penjual;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(name = "deskripsi_barang", nullable = false)
    private String deskripsiBarang;

    @Column(name = "batas_harga", nullable = false)
    private Double batasHarga;

    @Column(name = "foto_barang", nullable = false)
    private String fotoBarang;

    @Column(name = "kelipatan_bid", nullable = false)
    private Double kelipatanBid;

    @Column(name = "harga_awal", nullable = false)
    private Double hargaAwal;

    @Column(name = "batas_waktu", nullable = false)
    private LocalDateTime batasWaktu;

    @ManyToOne
    @JoinColumn(name = "pemenang", nullable = true)
    private User pemenang;

    @Column(name = "harga_tertinggi", nullable = true)
    private Double hargaTertinggi;

    @Column(name = "status", nullable = true)
    private String status;
}