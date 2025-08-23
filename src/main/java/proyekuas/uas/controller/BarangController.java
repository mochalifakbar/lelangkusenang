package proyekuas.uas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import proyekuas.uas.entity.Barang;
import proyekuas.uas.entity.Lelang;
import proyekuas.uas.entity.User;
import proyekuas.uas.service.BarangService;
import proyekuas.uas.service.LelangService;
import proyekuas.uas.service.UserService;

@Controller
public class BarangController {
    private final BarangService barangService;
    private final UserService userService;
    private final LelangService lelangService;

    @Autowired
    public BarangController(BarangService barangService, UserService userService, LelangService lelangService) {
        this.barangService = barangService;
        this.userService = userService;
        this.lelangService = lelangService;
    }


    @PostMapping("/tambahbarang")
    public String prosesBarang(@ModelAttribute Barang barang, 
                               @RequestParam("file") MultipartFile file, 
                               HttpSession session) throws IOException {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        barangService.addBarang(barang, file, user);
        return "redirect:/barang";
    }
    

    @PostMapping("/editbarang/")
    public String editbarang(@ModelAttribute Barang barangEdit, 
                             @RequestParam("file") MultipartFile file, 
                             HttpSession session) throws IOException {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        barangService.updateBarang(barangEdit, file, user);
        
        return "redirect:/barang";
    }

    @PostMapping("/hapusbarang/")
    public String hapusbarang(@RequestParam("id") Long id, HttpSession session) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        Barang barang = barangService.findById(id);
        if(barang != null && barang.getId().equals(id) && barang.getPenjual().getUsername().equals(session.getAttribute("loggedUser").toString())){
            if(barang.getPemenang() != null){
                return "redirect:/barang";
            }
            barangService.deleteBarang(barang.getId());
        }
        return "redirect:/barang";
    }


    @GetMapping("/barang")
    public String barang(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        List<Barang> barang = barangService.findByPenjualId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("barang", barang);

        return "barang";
    }


    @GetMapping("/tambahbarang")
    public String tambahbarang(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Barang barang = new Barang();
        barang.setPenjual(user);
        model.addAttribute("user", user);
        model.addAttribute("barang", barang);
        return "tambahbarang";
    }


    @GetMapping("/editbarang/")
    public String editbarang(@RequestParam("id") Long id, HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Barang barang = barangService.findById(id);

        if(barang == null || !barang.getPenjual().getUsername().equals(user.getUsername())){
            return "redirect:/barang";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("barang", barang);
        return "editbarang";
    }

    @GetMapping("/cekbarang/")
    public String cekbarang(@RequestParam("id") Long id, HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        List<Double> daftarBid = new ArrayList<>();
        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Barang barang = barangService.findById(id);
        List<Lelang> lelang = lelangService.getAllLelang(id);
        List<Lelang> lelangModifiable = new ArrayList<>(lelang);
        Collections.reverse(lelangModifiable);

        for (double bid = barang.getHargaAwal(); bid <= barang.getBatasHarga(); bid += barang.getKelipatanBid()) {
            if(barang.getHargaTertinggi() != null && bid <= barang.getHargaTertinggi()){
                continue;
            }
            daftarBid.add(bid);
        }

        model.addAttribute("daftarBid", daftarBid);
        model.addAttribute("user", user);
        model.addAttribute("barang", barang);
        model.addAttribute("listLelang", lelangModifiable);
        return "cekbarang";
    }
}
