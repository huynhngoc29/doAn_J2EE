package nhom02.doanmon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import nhom02.doanmon.service.CustomOAuth2UserService;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@EnableWebSecurity
@PropertySources({
                @PropertySource(value = "file:.env", ignoreResourceNotFound = true),
                @PropertySource(value = "file:../.env", ignoreResourceNotFound = true),
                @PropertySource(value = "file:./doanmon/.env", ignoreResourceNotFound = true),
                @PropertySource(value = "file:./doAn_J2EE/doanmon/.env", ignoreResourceNotFound = true)
})
public class SecurityConfig {

        @Autowired
        private CustomOAuth2UserService customOAuth2UserService;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/cakes", "/cakes/**", "/css/**", "/js/**",
                                                                "/images/**", "/register", "/login")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/cakes", true)
                                                .permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .defaultSuccessUrl("/cakes", true))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .csrf(csrf -> csrf.disable())
                                .headers(headers -> headers.disable());

                return http.build();
        }
}
