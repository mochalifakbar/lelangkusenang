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
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lelang")
public class Lelang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_barang", nullable = false)
    private Barang barang;

    @ManyToOne
    @JoinColumn(name = "id_pelelang", nullable = false)
    private User pelelang;

    @Column(name = "harga_bid", nullable = false)
    private Double hargaBid;

    @Column(name = "waktu_bid", nullable = false)
    private LocalDateTime waktuBid;
}