package com.bmt.NiagaraViajesParqueBicentenario.Controllers;

import com.bmt.NiagaraViajesParqueBicentenario.Models.NoticiaDTO;
import com.bmt.NiagaraViajesParqueBicentenario.Services.NoticiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    // READ - Listar todas las noticias activas
    @GetMapping("/newsList")
    public String listarNoticias(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerTodasActivas();
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar las noticias: " + e.getMessage());
        }
        return "admin/noticias/newsList";
    }

    // CREATE - Mostrar formulario de creación
    @GetMapping("/createNews")
    public String mostrarFormularioCreacion(Model model) {
        if (!model.containsAttribute("noticiaDTO")) {
            model.addAttribute("noticiaDTO", new NoticiaDTO());
        }
        return "admin/noticias/createNews";
    }

    // CREATE - Procesar formulario de creación
    @PostMapping("/createNews")
    public String crearNoticia(@Valid @ModelAttribute("noticiaDTO") NoticiaDTO noticiaDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("noticiaDTO", noticiaDTO);
            return "admin/noticias/createNews";
        }

        try {
            // Asegurar que activa esté en true si no se envió (checkbox unchecked)
            if (!noticiaDTO.isActiva()) {
                noticiaDTO.setActiva(true);
            }
            
            NoticiaDTO savedNoticia = noticiaService.crearNoticia(noticiaDTO);
            
            redirectAttributes.addFlashAttribute("success", "¡Noticia creada exitosamente!");
            return "redirect:/admin/noticias/viewNews/" + savedNoticia.getId();
            
        } catch (Exception e) {
            model.addAttribute("noticiaDTO", noticiaDTO);
            model.addAttribute("error", "Error al crear la noticia: " + e.getMessage());
            return "admin/noticias/createNews";
        }
    }

    // READ - Mostrar detalles de una noticia específica
    @GetMapping("/viewNews/{id}")
    public String verNoticia(@PathVariable("id") Long id, Model model) {
        try {
            Optional<NoticiaDTO> noticiaOpt = noticiaService.obtenerPorId(id);
            
            if (noticiaOpt.isEmpty()) {
                model.addAttribute("error", "Noticia no encontrada con ID: " + id);
                return "redirect:/admin/noticias/newsList";
            }
            
            model.addAttribute("noticia", noticiaOpt.get());
            return "admin/noticias/newsDetails";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la noticia: " + e.getMessage());
            return "redirect:/admin/noticias/newsList";
        }
    }

    // UPDATE - Mostrar formulario de edición
    @GetMapping("/editNews/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        try {
            Optional<NoticiaDTO> noticiaOpt = noticiaService.obtenerPorId(id);
            
            if (noticiaOpt.isEmpty()) {
                model.addAttribute("error", "Noticia no encontrada con ID: " + id);
                return "redirect:/admin/noticias/newsList";
            }
            
            model.addAttribute("noticiaDTO", noticiaOpt.get());
            return "admin/noticias/updateNews";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la noticia: " + e.getMessage());
            return "redirect:/admin/noticias/newsList";
        }
    }

    // UPDATE - Procesar formulario de edición
    @PostMapping("/updateNews/{id}")
    public String actualizarNoticia(@PathVariable("id") Long id,
                                  @Valid @ModelAttribute("noticiaDTO") NoticiaDTO noticiaDTO,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("noticiaDTO", noticiaDTO);
            return "admin/noticias/updateNews";
        }

        try {
            Optional<NoticiaDTO> updatedNoticia = noticiaService.actualizarNoticia(id, noticiaDTO);
            
            if (updatedNoticia.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la noticia");
                return "redirect:/admin/noticias/editNews/" + id;
            }
            
            redirectAttributes.addFlashAttribute("success", "Noticia actualizada exitosamente");
            return "redirect:/admin/noticias/viewNews/" + id;
        } catch (Exception e) {
            model.addAttribute("noticiaDTO", noticiaDTO);
            model.addAttribute("error", "Error al actualizar la noticia: " + e.getMessage());
            return "admin/noticias/updateNews";
        }
    }

    // DELETE - Eliminar noticia (soft delete)
    @PostMapping("/deleteNews/{id}")
    public String eliminarNoticia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean eliminado = noticiaService.eliminarNoticia(id);
            
            if (eliminado) {
                redirectAttributes.addFlashAttribute("success", "Noticia eliminada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la noticia");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la noticia: " + e.getMessage());
        }
        
        return "redirect:/admin/noticias/newsList";
    }

    // ACTIVAR/DESACTIVAR - Cambiar estado de noticia
    @PostMapping("/toggleStatus/{id}")
    public String cambiarEstadoNoticia(@PathVariable("id") Long id,
                                     @RequestParam("action") String action,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<NoticiaDTO> noticiaOpt = noticiaService.obtenerPorId(id);
            
            if (noticiaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Noticia no encontrada");
                return "redirect:/admin/noticias/newsList";
            }
            
            NoticiaDTO noticiaDTO = noticiaOpt.get();
            noticiaDTO.setActiva(action.equals("activate"));
            
            noticiaService.actualizarNoticia(id, noticiaDTO);
            
            String mensaje = action.equals("activate") ? 
                "Noticia activada exitosamente" : 
                "Noticia desactivada exitosamente";
            
            redirectAttributes.addFlashAttribute("success", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        
        return "redirect:/admin/noticias/newsList";
    }

    // DESTACAR/QUITAR DESTACADO - Cambiar estado destacado
    @PostMapping("/toggleFeatured/{id}")
    public String cambiarDestacado(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            boolean cambiado = noticiaService.toggleDestacada(id);
            
            if (!cambiado) {
                redirectAttributes.addFlashAttribute("error", "Noticia no encontrada");
                return "redirect:/admin/noticias/newsList";
            }
            
            Optional<NoticiaDTO> noticiaOpt = noticiaService.obtenerPorId(id);
            String mensaje = noticiaOpt.map(NoticiaDTO::isDestacada)
                    .map(destacada -> destacada ? 
                        "Noticia marcada como destacada" : 
                        "Noticia quitada de destacados")
                    .orElse("Estado cambiado");
            
            redirectAttributes.addFlashAttribute("success", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar destacado: " + e.getMessage());
        }
        
        return "redirect:/admin/noticias/newsList";
    }

    // Búsqueda de noticias por título
    @GetMapping("/searchNews")
    public String buscarNoticias(@RequestParam String titulo, Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.buscarPorTitulo(titulo);
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("searchTerm", titulo);
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Ver noticias destacadas
    @GetMapping("/featuredNews")
    public String verDestacadas(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerDestacadas();
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("filterType", "destacadas");
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar noticias destacadas: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Ver noticias con enlace de Facebook
    @GetMapping("/facebookNews")
    public String verConFacebook(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerConFacebook();
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("filterType", "facebook");
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar noticias con Facebook: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Ver todas las noticias (incluyendo inactivas)
    @GetMapping("/allNews")
    public String listarTodasNoticias(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerTodas();
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("showInactive", true);
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar todas las noticias: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Ver últimas noticias
    @GetMapping("/latestNews")
    public String verUltimasNoticias(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerUltimasActivas(10);
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("filterType", "ultimas");
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar las últimas noticias: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Ver noticias inactivas
    @GetMapping("/inactiveNews")
    public String verInactivas(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerInactivas();
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("showInactive", true);
            model.addAttribute("filterType", "inactivas");
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar noticias inactivas: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Buscar noticias por título (incluyendo inactivas)
    @GetMapping("/searchAllNews")
    public String buscarTodasNoticias(@RequestParam String titulo, Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.buscarPorTituloTodas(titulo);
            model.addAttribute("noticias", noticias != null ? noticias : Collections.emptyList());
            model.addAttribute("searchTerm", titulo);
            model.addAttribute("showInactive", true);
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
        }
        
        return "admin/noticias/newsList";
    }

    // Reactivar noticia
    @PostMapping("/reactivate/{id}")
    public String reactivarNoticia(@PathVariable("id") Long id, 
                                 RedirectAttributes redirectAttributes) {
        try {
            boolean reactivada = noticiaService.reactivarNoticia(id);
            
            if (reactivada) {
                redirectAttributes.addFlashAttribute("success", "Noticia reactivada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo reactivar la noticia");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reactivar la noticia: " + e.getMessage());
        }
        
        return "redirect:/admin/noticias/inactiveNews";
    }

}