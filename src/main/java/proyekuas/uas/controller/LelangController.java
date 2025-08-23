package proyekuas.uas.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.User;
import proyekuas.uas.service.BarangService;
import proyekuas.uas.service.LelangService;
import proyekuas.uas.service.UserService;

@Controller
public class LelangController {

    private final LelangService lelangService;
    private final UserService userService;
    private final BarangService barangService;

    @Autowired
    public LelangController(LelangService lelangService, UserService userService, BarangService barangService) {
        this.lelangService = lelangService;
        this.userService = userService;
        this.barangService = barangService;
    }
    @PostMapping("/submit-bit")
    public String submitBid(@RequestParam("id") Long id, 
                            @RequestParam("bid") double bid, 
                            HttpSession session) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        
        lelangService.addLelang(id, bid, user);

        return "redirect:/lelang";
    }


    @GetMapping("/lelang")
    public String listLelang(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        List<Barang> barang = barangService.findLelang(user);
        
        model.addAttribute("user", user);
        model.addAttribute("barang", barang);
        return "lelang";
    }
}
