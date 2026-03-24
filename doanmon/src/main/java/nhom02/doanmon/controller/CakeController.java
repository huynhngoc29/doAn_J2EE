package nhom02.doanmon.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.service.CakeService;

@Controller
@RequestMapping("/cakes")
public class CakeController {

    @Autowired
    private CakeService cakeService;

    @GetMapping
    public String getAllCakes(Model model) {
        model.addAttribute("cakes", cakeService.findAll());
        return "cake/list";
    }

    @GetMapping("/{id}")
    public String getCakeDetail(@PathVariable Long id, Model model) {
        Optional<Cake> cake = cakeService.findById(id);
        if (cake.isPresent()) {
            model.addAttribute("cake", cake.get());
            return "cake/detail";
        }
        return "redirect:/cakes";
    }
}
