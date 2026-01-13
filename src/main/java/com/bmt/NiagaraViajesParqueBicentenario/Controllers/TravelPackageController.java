package com.bmt.NiagaraViajesParqueBicentenario.Controllers;

import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackageDto;
import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackage;
import com.bmt.NiagaraViajesParqueBicentenario.Repositories.TravelPackageRepository;
import com.bmt.NiagaraViajesParqueBicentenario.Mappers.TravelPackageMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/packages")
public class TravelPackageController {

    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @Autowired
    private TravelPackageMapper travelPackageMapper;

    // ✅ RUTAS ESPECÍFICAS - SIN CONFLICTOS

    // READ - Listar todos los paquetes
    @GetMapping("/packageList")
    public String listarPaquetes(Model model) {
        List<TravelPackage> paquetes = travelPackageRepository.findAll();
        List<TravelPackageDto> paquetesDto = paquetes.stream()
                .map(travelPackageMapper::toDto)
                .collect(Collectors.toList());
        
        model.addAttribute("paquetes", paquetesDto);
        return "admin/packages/packageList";
    }

    // CREATE - Mostrar formulario de creación
    @GetMapping("/createPackage")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("paqueteDto", new TravelPackageDto());
        return "admin/packages/createPackage";
    }

    // CREATE - Procesar formulario de creación
    @PostMapping("/createPackage")
    public String crearPaquete(@Valid @ModelAttribute("paqueteDto") TravelPackageDto paqueteDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "admin/packages/createPackage";
        }

        try {
            TravelPackage paquete = travelPackageMapper.toEntity(paqueteDto);
            travelPackageRepository.save(paquete);
            
            redirectAttributes.addFlashAttribute("success", "Paquete creado exitosamente");
            return "redirect:/admin/packages/packageList";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el paquete: " + e.getMessage());
            return "redirect:/admin/packages/createPackage";
        }
    }

    // READ - Mostrar detalles de un paquete específico
    @GetMapping("/viewPackage/{id}")
    public String verPaquete(@PathVariable("id") Long id, Model model) {
        TravelPackage paquete = travelPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado con ID: " + id));
        
        TravelPackageDto paqueteDto = travelPackageMapper.toDto(paquete);
        model.addAttribute("paquete", paqueteDto);
        return "admin/packages/packageDetails";
    }

    // UPDATE - Mostrar formulario de edición
    @GetMapping("/editPackage/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        TravelPackage paquete = travelPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado con ID: " + id));
        
        TravelPackageDto paqueteDto = travelPackageMapper.toDto(paquete);
        model.addAttribute("paqueteDto", paqueteDto);
        return "admin/packages/updatePackage";
    }

    // UPDATE - Procesar formulario de edición
    @PostMapping("/updatePackage/{id}")
    public String actualizarPaquete(@PathVariable("id") Long id,
                                  @Valid @ModelAttribute("paqueteDto") TravelPackageDto paqueteDto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "admin/packages/updatePackage";
        }

        try {
            TravelPackage paqueteExistente = travelPackageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Paquete no encontrado con ID: " + id));
            
            // Actualizar los campos
            paqueteExistente.setTitulo(paqueteDto.getTitulo());
            paqueteExistente.setPrecio(paqueteDto.getPrecio());
            paqueteExistente.setDescripcion(paqueteDto.getDescripcion());
            paqueteExistente.setDias(paqueteDto.getDias());
            paqueteExistente.setNoches(paqueteDto.getNoches());
            paqueteExistente.setPersonas(paqueteDto.getPersonas());
            paqueteExistente.setImagenUrl(paqueteDto.getImagenUrl());
            
            travelPackageRepository.save(paqueteExistente);
            
            redirectAttributes.addFlashAttribute("success", "Paquete actualizado exitosamente");
            return "redirect:/admin/packages/packageList";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el paquete: " + e.getMessage());
            return "redirect:/admin/packages/updatePackage/" + id;
        }
    }

    // DELETE - Eliminar paquete
    @PostMapping("/deletePackage/{id}")
    public String eliminarPaquete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            TravelPackage paquete = travelPackageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Paquete no encontrado con ID: " + id));
            
            travelPackageRepository.delete(paquete);
            
            redirectAttributes.addFlashAttribute("success", "Paquete eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el paquete: " + e.getMessage());
        }
        
        return "redirect:/admin/packages/packageList";
    }

    // Búsqueda de paquetes por título
    @GetMapping("/searchPackage")
    public String buscarPaquetes(@RequestParam String titulo, Model model) {
        List<TravelPackage> paquetes = travelPackageRepository.findByTituloContainingIgnoreCase(titulo);
        List<TravelPackageDto> paquetesDto = paquetes.stream()
                .map(travelPackageMapper::toDto)
                .collect(Collectors.toList());
        
        model.addAttribute("paquetes", paquetesDto);
        model.addAttribute("searchTerm", titulo);
        return "admin/packages/packageList";
    }
}