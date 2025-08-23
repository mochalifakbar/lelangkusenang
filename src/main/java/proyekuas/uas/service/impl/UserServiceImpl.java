package proyekuas.uas.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.dto.RegisterForm;
import proyekuas.uas.entity.Alamat;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.UserRepository;
import proyekuas.uas.service.AlamatService;
import proyekuas.uas.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlamatService alamatService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void addUser(RegisterForm registerForm){
        User user = registerForm.getUser();
        Alamat alamat = registerForm.getAlamat();

        addUser(user);
        alamat.setUser(user);
        alamatService.addAlamat(alamat);
    }

    @Override
    public boolean login(User user) {
        User userDb = userRepository.findByUsername(user.getUsername());
        return userDb != null && passwordEncoder.matches(user.getPassword(), userDb.getPassword());
    }

    @Override
    public User findByUsername(String string) {
        return userRepository.findByUsername(string);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(RegisterForm registerForm, MultipartFile file, User user) throws IOException {
        User userEdit = registerForm.getUser();
        Alamat alamatEdit = registerForm.getAlamat();
        Alamat alamat = alamatService.findByUser(user.getId());

        if (!file.isEmpty()) {
            String username = userEdit.getUsername();
            userEdit.setFotoProfil("/fotoprofil/" + username + ".jpg");
            String directoryPath = "C:/xampp/htdocs/praktikum/uas/src/main/resources/static/fotoprofil/";
            File directory = new File(directoryPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File oldFile = new File(directoryPath + username + ".jpg");
            if (oldFile.exists()) {
                oldFile.delete();
            }

            String filePath = directoryPath + username + ".jpg";
            file.transferTo(new File(filePath));
        }
        if(userEdit.getNama() != null){
            user.setNama(userEdit.getNama());
        }
        if(userEdit.getEmail() != null){
            user.setEmail(userEdit.getEmail());
        }
        if(userEdit.getTanggalLahir() != null){
            user.setTanggalLahir(userEdit.getTanggalLahir());
        }
        if(userEdit.getNomorTelepon() != null){
            user.setNomorTelepon(userEdit.getNomorTelepon());
        }
        if(userEdit.getFotoProfil() != null){
            user.setFotoProfil(userEdit.getFotoProfil());
        }
        if(alamatEdit.getProvinsi() != null){
            alamat.setProvinsi(alamatEdit.getProvinsi());
        }
        if(alamatEdit.getKota() != null){
            alamat.setKota(alamatEdit.getKota());
        }
        if(alamatEdit.getKecamatan() != null){
            alamat.setKecamatan(alamatEdit.getKecamatan());
        }
        if(alamatEdit.getDesa() != null){
            alamat.setDesa(alamatEdit.getDesa());
        }
        if(alamatEdit.getDetail() != null){
            alamat.setDetail(alamatEdit.getDetail());
        }
        updateUser(user);
        alamatService.updateAlamat(alamat);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchUsers(String query) {
        return userRepository.findByNamaContainingIgnoreCase(query);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
