package proyekuas.uas.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    

    @PostMapping("/editbarang")
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
    public String showGudangPage(
            HttpSession session,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String searchKeyword,
            @RequestParam(name = "status", required = false) String statusFilter,
            Model model) {

        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }
        
        // Buat objek Pageable untuk paginasi
        PageRequest pageable = PageRequest.of(page - 1, size);
        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        
        
        
        Page<Barang> gudangPage = barangService.findPaginatedAndFiltered(searchKeyword, statusFilter, user.getId(), pageable);
        
        // --- CONTOH DATA DUMMY (Ganti dengan logic service Anda) ---
        // List<Barang> allItems = ...; // Ambil semua data dari database
        // Lalu filter di sini atau lebih baik di query database
        // Page<Barang> gudangPage = ...;
        // -----------------------------------------------------------
        model.addAttribute("user", user);
        model.addAttribute("gudangItems", gudangPage.getContent());
        model.addAttribute("currentPage", gudangPage.getNumber() + 1);
        model.addAttribute("totalPages", gudangPage.getTotalPages());
        model.addAttribute("totalItems", gudangPage.getTotalElements());

        // PENTING: Kembalikan nilai filter ke view agar input tetap terisi
        model.addAttribute("currentSearch", searchKeyword);
        model.addAttribute("currentStatus", statusFilter);
        
        // Asumsi gudangItems adalah nama variabel di Thymeleaf Anda
        // model.addAttribute("gudangItems", ...); 

        return "barang"; // nama file HTML Anda (tanpa .html)
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


    @GetMapping("/editbarang")
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

        List<BigDecimal> daftarBid = new ArrayList<>();
        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Barang barang = barangService.findById(id);
        List<Lelang> lelang = lelangService.getAllLelang(id);
        List<Lelang> lelangModifiable = new ArrayList<>(lelang);
        Collections.reverse(lelangModifiable);

        for (BigDecimal bid = barang.getHargaAwal(); bid.compareTo(barang.getBatasHarga()) <= 0; bid = bid.add(barang.getKelipatanBid())) {
            if(barang.getHargaTertinggi() != null && bid.compareTo(barang.getHargaTertinggi()) <= 0){
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
