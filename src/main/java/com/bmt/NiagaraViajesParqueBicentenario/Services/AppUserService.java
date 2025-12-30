package com.bmt.NiagaraViajesParqueBicentenario.Services;

import com.bmt.NiagaraViajesParqueBicentenario.Models.AppUser;
import com.bmt.NiagaraViajesParqueBicentenario.Repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = repo.findByEmail(email);

        if (appUser == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
        }

        // Asegúrate de que el rol no tenga el prefijo "ROLE_"
        String role = appUser.getRol().startsWith("ROLE_") 
            ? appUser.getRol().substring(5) // Elimina el prefijo "ROLE_"
            : appUser.getRol();

        return User.withUsername(appUser.getEmail())
                .password(appUser.getContraseña())
                .roles(role) // Usa el rol sin el prefijo "ROLE_"
                .build();
    }
}