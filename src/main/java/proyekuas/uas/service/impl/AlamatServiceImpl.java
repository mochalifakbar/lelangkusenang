package proyekuas.uas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyekuas.uas.entity.Alamat;
import proyekuas.uas.repository.AlamatRepository;
import proyekuas.uas.service.AlamatService;

@Service
public class AlamatServiceImpl implements AlamatService {
    @Autowired
    private AlamatRepository alamatRepository;

    @Override
    public void addAlamat(Alamat alamat) {
        alamatRepository.save(alamat);
    }

    @Override
    public Alamat findByUser(Long id) {
        return alamatRepository.findByUser(id);
    }

    @Override
    public void updateAlamat(Alamat alamat) {
        alamatRepository.save(alamat);
    }
}
