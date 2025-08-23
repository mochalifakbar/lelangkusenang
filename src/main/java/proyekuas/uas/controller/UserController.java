package proyekuas.uas.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import proyekuas.uas.dto.RegisterForm;
import proyekuas.uas.entity.Alamat;
import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.User;
import proyekuas.uas.service.AlamatService;
import proyekuas.uas.service.BarangService;
import proyekuas.uas.service.UserService;

@Controller
public class UserController {
    private final UserService userService;
    private final AlamatService alamatService;
    private final BarangService barangService;

    @Autowired
    public UserController(UserService userService, AlamatService alamatService, BarangService barangService) {
        this.userService = userService;
        this.alamatService = alamatService;
        this.barangService = barangService;
    }

    @PostMapping("/register")
    public String registerprocess(@ModelAttribute RegisterForm registerForm) {

        userService.addUser(registerForm);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginprocess(@ModelAttribute User user, HttpSession session) {
        if (userService.login(user)) {
            session.setAttribute("loggedUser", user.getUsername());
            return "redirect:/beranda";
        } else {
            return "redirect:/login";
        }
    }

    @DeleteMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedUser");
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String register(Model model, HttpSession session){
        if(session.getAttribute("loggedUser") != null){
            return "redirect:/beranda";
        }
        model.addAttribute("registerForm", new RegisterForm(new User(), new Alamat()));
        return "register";
    }


    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if(session.getAttribute("loggedUser") != null){
            return "redirect:/beranda";
        }
        model.addAttribute("user", new User());
        return "login";
    }


    @GetMapping("/beranda")
    public String home(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        List<Barang> barang = barangService.findLelang(user);
        model.addAttribute("user", user);
        model.addAttribute("barang", barang);
        return "beranda";
    }


    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Alamat alamat = alamatService.findByUser(user.getId());
        model.addAttribute("userAtribute", new RegisterForm(user, alamat));
        return "profil";
    }

    @PostMapping("/profil")
    public String updateProfil(@ModelAttribute RegisterForm registerForm, 
                               @RequestParam("file") MultipartFile file, 
                               HttpSession session) throws IOException{

        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        userService.updateUser(registerForm, file, userService.findByUsername(session.getAttribute("loggedUser").toString()));
        
        return "redirect:/beranda";
    }
}