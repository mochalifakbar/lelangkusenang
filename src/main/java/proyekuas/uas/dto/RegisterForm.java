package proyekuas.uas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import proyekuas.uas.entity.Alamat;
import proyekuas.uas.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {
    private User user;
    private Alamat alamat;
}
