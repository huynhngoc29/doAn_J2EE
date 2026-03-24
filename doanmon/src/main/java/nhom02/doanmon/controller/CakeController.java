package nhom02.doanmon.controller;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.entity.Category;
import nhom02.doanmon.service.CakeService;
import nhom02.doanmon.service.CategoryService;

@Controller
@RequestMapping("/cakes")
public class CakeController {

    @Autowired
    private CakeService cakeService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getAllCakes(Model model) {
        List<Cake> allCakes = cakeService.findAll();
        List<Category> categories = categoryService.findAll();

        // Nhóm bánh theo category
        Map<Category, List<Cake>> cakesByCategory = new LinkedHashMap<>();
        for (Category category : categories) {
            List<Cake> cakesInCategory = allCakes.stream()
                    .filter(cake -> cake.getCategory() != null && cake.getCategory().getId().equals(category.getId()))
                    .collect(Collectors.toList());
            if (!cakesInCategory.isEmpty()) {
                cakesByCategory.put(category, cakesInCategory);
            }
        }

        // Bánh không có category
        List<Cake> uncategorizedCakes = allCakes.stream()
                .filter(cake -> cake.getCategory() == null)
                .collect(Collectors.toList());

        model.addAttribute("cakesByCategory", cakesByCategory);
        model.addAttribute("uncategorizedCakes", uncategorizedCakes);
        model.addAttribute("categories", categories);
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
