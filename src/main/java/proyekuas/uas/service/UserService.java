package proyekuas.uas.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import proyekuas.uas.dto.RegisterForm;
import proyekuas.uas.entity.User;

public interface UserService {
    void addUser(User user);

    void addUser(RegisterForm registerForm);

    User findByUsername(String string);

    boolean login(User user);

    void updateUser(User user);

    void updateUser(RegisterForm registerForm, MultipartFile file, User user) throws IOException;

    List<User> getAllUsers();

    List<User> searchUsers(String query);

    User getUserById(Long userId);
}
