package proyekuas.uas.service;

import proyekuas.uas.entity.Alamat;

public interface AlamatService {
    void addAlamat(Alamat alamat);

    Alamat findByUser(Long id);

    void updateAlamat(Alamat alamat);
}
