package com.bmt.NiagaraViajesParqueBicentenario.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    // Permisos para recursos estáticos
                    .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**"
                    ).permitAll()
                    
                    // Permisos para páginas públicas - AÑADIDO /noticias/**
                    .requestMatchers(
                        "/",
                        "/index",
                        "/galeria",
                        "/noticias",      // Página principal de noticias
                        "/noticias/**",   // ¡IMPORTANTE! Todas las rutas de noticias
                        "/contact",
                        "/register",
                        "/login",
                        "/logout"
                    ).permitAll()
                    
                    // Permisos para el admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    
                    // Permisos para usuarios autenticados (no admin)
                    .requestMatchers(
                        "/user/**",
                        "/reservations/**"
                    ).hasAnyRole("USER", "ADMIN")
                    
                    // Permisos para el resto de páginas principales (ajustar según necesites)
                    .requestMatchers(
                        "/#inicio",
                        "/#servicios",
                        "/#paquetes",
                        "/#proveedores",
                        "/#nosotros",
                        "/#contacto"
                    ).permitAll()
                    
                    // Todas las demás solicitudes requieren autenticación
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureUrl("/login?error=true")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                )
                .build();
    }
    
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, 
                                              HttpServletResponse response,
                                              Authentication authentication) 
                                              throws IOException {
                
                System.out.println("=== LOGIN EXITOSO ===");
                System.out.println("Usuario: " + authentication.getName());
                System.out.println("Roles: " + authentication.getAuthorities());
                
                // Verificar si tiene rol ADMIN
                boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> 
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
                
                // Verificar si tiene rol USER
                boolean isUser = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> 
                        grantedAuthority.getAuthority().equals("ROLE_USER"));
                
                System.out.println("isAdmin: " + isAdmin);
                System.out.println("isUser: " + isUser);
                
                // Redirección basada en el rol
                if (isAdmin) {
                    System.out.println("Redirigiendo ADMIN a /admin/dashboard");
                    response.sendRedirect("/admin/dashboard");
                } else if (isUser) {
                    System.out.println("Redirigiendo USER a /index");
                    response.sendRedirect("/index");
                } else {
                    // Por defecto, redirigir a /index
                    System.out.println("Redirigiendo por defecto a /index");
                    response.sendRedirect("/index");
                }
            }
        };
    }
    
    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}