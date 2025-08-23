package proyekuas.uas.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "pengirim_id", nullable = false)
    private User pengirim;

    @Column(name = "pesan", nullable = false)
    private String pesan;

    @Column(name = "waktu", nullable = false)
    private LocalDateTime waktu;

    @Column(name = "dibaca", nullable = false)
    private boolean dibaca;

    @PrePersist
    public void prePersist() {
        this.waktu = LocalDateTime.now();
        this.dibaca = false;
    }
}
