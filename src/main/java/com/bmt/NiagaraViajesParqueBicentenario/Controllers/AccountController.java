package com.bmt.NiagaraViajesParqueBicentenario.Controllers;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bmt.NiagaraViajesParqueBicentenario.Models.AppUser;
import com.bmt.NiagaraViajesParqueBicentenario.Models.RegisterDto;
import com.bmt.NiagaraViajesParqueBicentenario.Repositories.AppUserRepository;

import jakarta.validation.Valid;

@Controller
public class AccountController {
	
	@Autowired
	private AppUserRepository repo;
	
	@GetMapping({"/login"})
	public String home () {
		return "login";
	}
	
	@GetMapping("/register")
	public String register (Model model) {
		RegisterDto registerDto = new RegisterDto();
		model.addAttribute(registerDto);
		model.addAttribute("success", false);
		return "register";
	}

	@PostMapping("/register")
	public String register(

			Model model,
			@Valid @ModelAttribute RegisterDto registerDto,
			BindingResult result
			) {
		
		if (!registerDto.getContraseña().equals(registerDto.getConfirmarContraseña())) {
			result.addError(
					new FieldError("registerDto", "confirmPassword"
							, "Las contraseñas no coinciden")
					);
		}
		
		AppUser appUser = repo.findByEmail(registerDto.getEmail());
		if (appUser != null) {
			result.addError(
					new FieldError("registerDto", "Email"
							, "El correo esta actualmente en uso")
					);
		}
		
		if (result.hasErrors()) {
			return "register";
		}
		try {
			//Crear nueva cuenta
			var bCryptEncoder = new BCryptPasswordEncoder();
			
			AppUser newUser = new AppUser();
			newUser.setNombre(registerDto.getNombre());
			newUser.setApellido(registerDto.getApellido());
			newUser.setEmail(registerDto.getEmail());
			newUser.setTelefono(registerDto.getTelefono());
			newUser.setDireccion(registerDto.getDireccion());
			newUser.setRol("CLIENT");
			newUser.setCreatedAt(new Date());
			newUser.setContraseña(bCryptEncoder.encode(registerDto.getContraseña()));
			
			repo.save(newUser);
			
			model.addAttribute("registerDto", new RegisterDto());
			model.addAttribute("success", true);
			
		}
		catch(Exception ex) {
			result.addError(
					new FieldError("registerDto", "Nombre"
							, ex.getMessage())
					);
	}
		return "register";
	}
	
	@GetMapping("/admin/ajustes")
	public String showProfile(Model model, Principal principal) {
	    String email = principal.getName();
	    AppUser appUser = repo.findByEmail(email);
	    model.addAttribute("appUser", appUser);
	    return "admin/ajustes";
	}

}
	
