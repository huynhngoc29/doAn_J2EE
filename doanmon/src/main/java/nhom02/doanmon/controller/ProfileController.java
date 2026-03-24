package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import nhom02.doanmon.entity.User;
import nhom02.doanmon.repository.UserRepository;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

            @Autowired
    private nhom02.doanmon.repository.PaymentRepository paymentRepository;

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() 
            && !authentication.getName().equals("anonymousUser")) {
            
            String username;
            String email = null;
            String fullName = null;
            String provider = "LOCAL";
            String roles = "";
            java.util.List<nhom02.doanmon.entity.Payment> orders = new java.util.ArrayList<>();

            User user = null;

            // Check if logged in via OAuth2 (Google)
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                email = oauth2User.getAttribute("email");
                fullName = oauth2User.getAttribute("name");
                username = email;
                provider = "GOOGLE";
                roles = String.join(", ", authentication.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority())
                    .toList());
                user = userRepository.findByUsername(username).orElse(null);
            } else {
                // Regular form login
                username = authentication.getName();
                user = userRepository.findByUsername(username).orElse(null);
                
                if (user != null) {
                    email = user.getEmail();
                    fullName = user.getFullName();
                    provider = user.getProvider() != null ? user.getProvider() : "LOCAL";
                    roles = String.join(", ", user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .toList());
                }
            }

            if (user != null && user.getId() != null) {
                orders = paymentRepository.findByUserId(user.getId());
                // sort orders by newest
                orders.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

                boolean changed = false;
                for (nhom02.doanmon.entity.Payment p : orders) {
                    if (!p.isUserRead()) {
                        p.setUserRead(true);
                        changed = true;
                    }
                }
                if (changed) {
                    paymentRepository.saveAll(orders);
                }
            }

            model.addAttribute("orders", orders);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("fullName", fullName);
            model.addAttribute("provider", provider);
            model.addAttribute("roles", roles);
            model.addAttribute("unreadUpdateCount", 0);
            model.addAttribute("authenticated", true);
        } else {
            model.addAttribute("authenticated", false);
        }

        return "profile/view";
    }
}
