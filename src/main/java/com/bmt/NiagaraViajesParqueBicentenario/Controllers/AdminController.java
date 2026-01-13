package com.bmt.NiagaraViajesParqueBicentenario.Controllers;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.bmt.NiagaraViajesParqueBicentenario.Models.AppUser;
import com.bmt.NiagaraViajesParqueBicentenario.Models.RegisterDto;
import com.bmt.NiagaraViajesParqueBicentenario.Repositories.AppUserRepository;
@Controller
	@RequestMapping("/admin")
	public class AdminController {
	    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')") // Doble protección
    public String dashboard() {
        return "admin/dashboard";
    }
    
}
    

	


