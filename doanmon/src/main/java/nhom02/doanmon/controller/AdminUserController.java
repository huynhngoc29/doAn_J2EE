package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import nhom02.doanmon.entity.User;
import nhom02.doanmon.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users/list";
    }

    @GetMapping("/edit/{id}")
    public String editUserRoleForm(@PathVariable Long id, Model model,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        String currentRole = user.getRoles().stream().findFirst().map(r -> r.getName()).orElse("USER");
        if (currentRole.equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Không thể chỉnh sửa hoặc thao tác trên tài khoản ADMIN");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("currentRole", currentRole);
        return "admin/users/form";
    }

    @PostMapping("/update-role")
    public String updateUserRole(@RequestParam("userId") Long userId, @RequestParam("role") String roleName,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(userId, roleName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
