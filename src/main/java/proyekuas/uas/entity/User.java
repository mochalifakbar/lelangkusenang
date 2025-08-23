package proyekuas.uas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 25, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 68)
    private String password;

    @Column(name = "nama", nullable = false, length = 50)
    private String nama;

    @Column(name = "tanggal_lahir", nullable = false, length = 10)
    private String tanggalLahir;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "nomor_telepon", nullable = false, length = 15, unique = true)
    private String nomorTelepon;

    @Column(name = "foto_profil")
    private String fotoProfil;

    @PrePersist
    public void prePersist() {
        if(this.fotoProfil == null) {
            this.fotoProfil = "/fotoprofil/default/default.png";
        }
    }
}