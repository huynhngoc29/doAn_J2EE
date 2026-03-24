package nhom02.doanmon.controller;

import nhom02.doanmon.entity.Category;
import nhom02.doanmon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public List<String> listCategories() {
        return categoryService.findAll().stream()
                .map(c -> c.getName() + " - " + c.getDescription())
                .collect(Collectors.toList());
    }
}

