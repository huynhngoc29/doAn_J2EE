package nhom02.doanmon.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.entity.Role;
import nhom02.doanmon.entity.User;
import nhom02.doanmon.repository.CakeRepository;
import nhom02.doanmon.repository.RoleRepository;
import nhom02.doanmon.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

        @Bean
        CommandLineRunner initDatabase(CakeRepository cakeRepository, RoleRepository roleRepository,
                        UserRepository userRepository, nhom02.doanmon.repository.CategoryRepository categoryRepository) {
                return args -> {
                        // Seed Categories
                        if (categoryRepository.count() == 0) {
                            categoryRepository.saveAll(List.of(
                                new nhom02.doanmon.entity.Category(null, "Chocolate", "Chocolate cakes"),
                                new nhom02.doanmon.entity.Category(null, "Fruit", "Fruit cakes"),
                                new nhom02.doanmon.entity.Category(null, "Classic", "Classic cakes"),
                                new nhom02.doanmon.entity.Category(null, "Cheese", "Cheese cakes")
                            ));
                        }

                        // Seed Cakes
                        if (cakeRepository.count() == 0) {
                                nhom02.doanmon.entity.Category chocCategory = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Chocolate")).findFirst().orElse(null);
                                nhom02.doanmon.entity.Category fruitCategory = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Fruit")).findFirst().orElse(null);
                                nhom02.doanmon.entity.Category classicCategory = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Classic")).findFirst().orElse(null);
                                nhom02.doanmon.entity.Category cheeseCategory = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Cheese")).findFirst().orElse(null);

                                List<Cake> cakes = List.of(
                                                new Cake(null, "Chocolate Fudge Cake", 15.99,
                                                                "Rich and moist chocolate cake with fudge frosting.",
                                                                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                                                                chocCategory, null),
                                                new Cake(null, "Strawberry Shortcake", 12.50,
                                                                "Fresh strawberries and whipped cream on a sponge cake.",
                                                                "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                                                                fruitCategory, null),
                                                new Cake(null, "Red Velvet", 18.00,
                                                                "Classic red velvet cake with cream cheese frosting.",
                                                                "https://images.unsplash.com/photo-1586788680434-30d32443d858?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                                                                classicCategory, null),
                                                new Cake(null, "Lemon Drizzle", 10.99,
                                                                "Zesty lemon cake with a sugary glaze.",
                                                                "https://images.unsplash.com/photo-1519340333755-56e9c1d04579?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                                                                fruitCategory, null),
                                                new Cake(null, "Cheesecake", 20.00, "Creamy New York style cheesecake.",
                                                                "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                                                                cheeseCategory, null));
                                cakeRepository.saveAll(cakes);
                        }

                        // Seed Roles
                        if (roleRepository.count() == 0) {
                                roleRepository.save(new Role(null, "ADMIN"));
                                roleRepository.save(new Role(null, "USER"));
                        }

                        // Seed Admin User
                        if (userRepository.findByUsername("admin").isEmpty()) {
                                User admin = new User();
                                admin.setUsername("admin");
                                admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
                                admin.setEmail("admin@example.com");
                                admin.setFullName("Admin User");
                                admin.setProvider("LOCAL");

                                Role adminRole = roleRepository.findByName("ADMIN").get();
                                admin.setRoles(Collections.singleton(adminRole));

                                userRepository.save(admin);
                                System.out.println("Admin user created: admin / 123456");
                        }
                };
        }
}
