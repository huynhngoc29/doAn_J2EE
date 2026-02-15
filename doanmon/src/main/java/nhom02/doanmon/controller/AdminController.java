package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.service.CakeService;

@Controller
@RequestMapping("/admin/cakes")
public class AdminController {

    @Autowired
    private CakeService cakeService;

    @GetMapping
    public String listCakes(Model model) {
        model.addAttribute("cakes", cakeService.findAll());
        return "admin/cakes/list";
    }

    @GetMapping("/add")
    public String addCakeForm(Model model) {
        model.addAttribute("cake", new Cake());
        return "admin/cakes/form";
    }

    @PostMapping("/save")
    public String saveCake(@ModelAttribute("cake") Cake cake) {
        cakeService.save(cake);
        return "redirect:/admin/cakes";
    }

    @GetMapping("/edit/{id}")
    public String editCakeForm(@PathVariable Long id, Model model) {
        model.addAttribute("cake", cakeService.findById(id).orElse(null));
        return "admin/cakes/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCake(@PathVariable Long id) {
        cakeService.deleteById(id);
        return "redirect:/admin/cakes";
    }
}
