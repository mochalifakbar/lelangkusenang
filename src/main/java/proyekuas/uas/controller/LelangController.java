package proyekuas.uas.controller;


import java.math.BigDecimal;
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
                            @RequestParam("bid") BigDecimal bid, 
                            HttpSession session) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        
        lelangService.addLelang(id, bid, user);

        return "redirect:/lelang";
    }

    @PostMapping("/beli-sekarang")
    public String beliSekarang(@RequestParam("id") Long id, HttpSession session) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());

        lelangService.beliSekarang(id, user);

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

    @GetMapping("/lelang/mulai")
    public String mulaiLelang(@RequestParam("id") Long id, HttpSession session /* atau @AuthenticationPrincipal User currentUser */) {
        if (session.getAttribute("loggedUser") == null) { // Ganti dengan if(currentUser == null) jika sudah migrasi
            return "redirect:/login";
        }
        User currentUser = userService.findByUsername(session.getAttribute("loggedUser").toString());
        
        try {
            lelangService.mulaiLelang(id, currentUser);
        } catch (IllegalStateException e) {
            // Opsional: Tambahkan flash attribute untuk menampilkan pesan error di halaman
            System.err.println(e.getMessage());
        }

        return "redirect:/barang"; // Kembali ke halaman gudang
    }
}
