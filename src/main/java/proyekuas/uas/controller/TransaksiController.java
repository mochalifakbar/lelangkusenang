package proyekuas.uas.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import proyekuas.uas.entity.Transaksi;
import proyekuas.uas.entity.User;
import proyekuas.uas.service.AlamatService;
import proyekuas.uas.service.TransaksiService;
import proyekuas.uas.service.UserService;

@Controller
public class TransaksiController {

    private final UserService userService;
    private final TransaksiService transaksiService;
    private final AlamatService alamatService;

    @Autowired
    public TransaksiController(UserService userService, TransaksiService transaksiService, AlamatService alamatService) {
        this.userService = userService;
        this.transaksiService = transaksiService;
        this.alamatService = alamatService;
    }


    @GetMapping("/transaksi-penjual")
    public String transaksi(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        List<Transaksi> transaksi = transaksiService.findByUserId(user.getId());
        List<Transaksi> transaksiPenjualPending = transaksi.stream().filter(t -> t.getStatusPembayaran() == Transaksi.StatusPembayaran.pending).filter(t -> t.getPenjual().getId().equals(user.getId())).collect(Collectors.toList());
        List<Transaksi> transaksiPenjualGagal = transaksi.stream().filter(t -> t.getStatusPembayaran() == Transaksi.StatusPembayaran.gagal).filter(t -> t.getPenjual().getId().equals(user.getId())).collect(Collectors.toList());
        List<Transaksi> transaksiPenjualSukses = transaksi.stream().filter(t -> t.getStatusPembayaran() == Transaksi.StatusPembayaran.sukses).filter(t -> t.getPenjual().getId().equals(user.getId())).collect(Collectors.toList());
        
        model.addAttribute("user", user);
        model.addAttribute("transaksiPending", transaksiPenjualPending);
        model.addAttribute("transaksiGagal", transaksiPenjualGagal);
        model.addAttribute("transaksiSukses", transaksiPenjualSukses);
        return "transaksi-penjual";
    }

    @GetMapping("/transaksi-pembeli")
    public String transaksiPembeli(HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        List<Transaksi> transaksi = transaksiService.findByUserId(user.getId());

        List<Transaksi> transaksiPembeliPending = transaksi.stream().filter(t -> t.getStatusPembayaran() == Transaksi.StatusPembayaran.pending).filter(t -> t.getPembeli().getId().equals(user.getId())).collect(Collectors.toList());
        List<Transaksi> transaksiPembeliGagal = transaksi.stream().filter(t -> t.getStatusPembayaran() == Transaksi.StatusPembayaran.gagal).filter(t -> t.getPembeli().getId().equals(user.getId())).collect(Collectors.toList());
        List<Transaksi> transaksiPembeliSukses = transaksi.stream().filter(t -> t.getStatusPembayaran() == Transaksi.StatusPembayaran.sukses).filter(t -> t.getPembeli().getId().equals(user.getId())).collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("transaksiPending", transaksiPembeliPending);
        model.addAttribute("transaksiGagal", transaksiPembeliGagal);
        model.addAttribute("transaksiSukses", transaksiPembeliSukses);

        return "transaksi-pembeli";
    }

    @GetMapping("/cektransaksi/")
    public String cektransaksi(@RequestParam("id") Long id, HttpSession session, Model model) {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Transaksi transaksi = transaksiService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("alamatPembeli", alamatService.findByUser(transaksi.getPembeli().getId()));
        model.addAttribute("alamatPenjual", alamatService.findByUser(transaksi.getPenjual().getId()));

        return "cektransaksi";
    }

    @PostMapping("/konfirmasiPembayaran")
    public String konfirmasiPembayaran(@RequestParam("idTransaksi") Long id, 
                                        @RequestParam("fotoBukti") MultipartFile file, 
                                        HttpSession session, Model model) throws IOException {
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User user = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Transaksi transaksi = transaksiService.konfirmasiPembayaran(id, file, user);

        model.addAttribute("user", user);
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("alamatPembeli", alamatService.findByUser(transaksi.getPembeli().getId()));
        model.addAttribute("alamatPenjual", alamatService.findByUser(transaksi.getPenjual().getId()));

        return "cektransaksi";
    }
}
