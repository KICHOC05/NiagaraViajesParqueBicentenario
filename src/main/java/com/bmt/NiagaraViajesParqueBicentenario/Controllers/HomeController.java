package com.bmt.NiagaraViajesParqueBicentenario.Controllers;

import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackageDto;
import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackage;
import com.bmt.NiagaraViajesParqueBicentenario.Repositories.TravelPackageRepository;
import com.bmt.NiagaraViajesParqueBicentenario.Mappers.TravelPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    
    @Autowired
    private TravelPackageRepository travelPackageRepository;
    
    @Autowired
    private TravelPackageMapper travelPackageMapper;
    
    @GetMapping("/index")
    public String home(Model model) {
        try {
            List<TravelPackage> paquetes = travelPackageRepository.findAll();
            List<TravelPackageDto> paquetesDto = paquetes.stream()
                    .map(travelPackageMapper::toDto)
                    .collect(Collectors.toList());
            
            System.out.println("Número de paquetes encontrados: " + paquetes.size());
            model.addAttribute("paquetes", paquetesDto);
            
            // Para debug, también puedes agregar:
            model.addAttribute("debugCount", paquetes.size());
            
        } catch (Exception e) {
            System.err.println("Error al cargar paquetes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "index";
    }
    
    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }
    
    @GetMapping("/galeria")
    public String galeria() {
        return "gallery";
    }
}