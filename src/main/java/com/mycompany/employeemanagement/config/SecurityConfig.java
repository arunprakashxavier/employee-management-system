package com.mycompany.employeemanagement.config; // Use your actual package name

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher; // Import for Ant Matchers

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // PasswordEncoder bean remains the same
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DatabaseUserDetailsService is used implicitly by Spring Security

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access
                        .requestMatchers(
                                "/", // Allow access to root path if needed
                                "/css/**", // Allow access to static resources like CSS
                                "/js/**", // Allow access to static resources like JS
                                "/images/**", // Allow access to static resources like images
                                "/webjars/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        // Allow access to the login page and processing URL
                        .requestMatchers("/login", "/login?error", "/login?logout").permitAll() // Added logout URL permit

                        // API Authorization Rules
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/employees")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/api/v1/employees/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/v1/employees/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/employees/**")).hasRole("ADMIN")

                        // Web UI Authorization Rules (assuming pages under /web/)
                        .requestMatchers(antMatcher("/web/**")).hasRole("ADMIN") // Secure web pages

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // Keep for API access if needed
                .formLogin(formLogin -> formLogin // Configure Form Login
                                .loginPage("/login") // Specify custom login page URL
                                .permitAll() // Allow everyone to access the login page URL itself
                                .defaultSuccessUrl("/web/employees", true) // Redirect to employee list on success
                        // .failureUrl("/login?error=true") // URL on login failure (default mechanism works)
                )
                .logout(logout -> logout // Configure Logout
                        .logoutSuccessUrl("/login?logout") // Redirect after logout (triggers message on login page)
                        .permitAll() // Allow anyone to trigger logout
                        .invalidateHttpSession(true) // Invalidate session
                        .deleteCookies("JSESSIONID") // Delete session cookie (if using sessions)
                )
                .csrf(csrf -> csrf.disable()); // Adjust CSRF if needed (enable for production with session auth)
        return http.build();
    }
}