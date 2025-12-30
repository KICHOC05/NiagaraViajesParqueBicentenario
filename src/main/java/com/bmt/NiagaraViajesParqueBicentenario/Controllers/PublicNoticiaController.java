package com.bmt.NiagaraViajesParqueBicentenario.Controllers;

import com.bmt.NiagaraViajesParqueBicentenario.Models.NoticiaDTO;
import com.bmt.NiagaraViajesParqueBicentenario.Services.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class PublicNoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    // Ver noticias públicas para usuarios (Página principal)
    @GetMapping("/noticias")
    public String verNoticiasPublicas(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            Model model) {
        
        try {
            List<NoticiaDTO> todasNoticias = noticiaService.obtenerTodasActivas();
            
            if (todasNoticias == null) {
                todasNoticias = Collections.emptyList();
            }
            
            // Ordenar por ID descendente (equivalente a ordenar por fecha de creación)
            todasNoticias.sort(Comparator.comparing(NoticiaDTO::getId).reversed());
            
            // Paginación simple
            int totalNoticias = todasNoticias.size();
            int totalPages = (int) Math.ceil((double) totalNoticias / size);
            
            // Asegurar que la página sea válida
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, totalNoticias);
            
            List<NoticiaDTO> noticiasPagina = start < end ? 
                todasNoticias.subList(start, end) : 
                Collections.emptyList();
            
            // Obtener noticias destacadas
            List<NoticiaDTO> destacadas = todasNoticias.stream()
                .filter(NoticiaDTO::isDestacada)
                .limit(3)
                .collect(Collectors.toList());
            
            model.addAttribute("noticias", noticiasPagina);
            model.addAttribute("destacadas", destacadas);
            model.addAttribute("totalNoticias", totalNoticias);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("destacadas", Collections.emptyList());
            model.addAttribute("totalNoticias", 0);
            model.addAttribute("error", "Error al cargar las noticias: " + e.getMessage());
        }
        return "noticias";
    }

    // Ver detalles de noticia pública
    @GetMapping("/noticias/{id}")
    public String verNoticiaPublica(@PathVariable("id") Long id, Model model) {
        try {
            Optional<NoticiaDTO> noticiaOpt = noticiaService.obtenerPorIdActiva(id);
            
            if (noticiaOpt.isEmpty()) {
                model.addAttribute("error", "Noticia no encontrada o no está disponible");
                return "redirect:/noticias";
            }
            
            NoticiaDTO noticia = noticiaOpt.get();
            model.addAttribute("noticia", noticia);
            
            // Obtener noticias relacionadas (últimas noticias activas excluyendo la actual)
            List<NoticiaDTO> todasActivas = noticiaService.obtenerTodasActivas();
            List<NoticiaDTO> relacionadas = todasActivas.stream()
                .filter(n -> !n.getId().equals(id))
                .sorted(Comparator.comparing(NoticiaDTO::getId).reversed())
                .limit(3)
                .collect(Collectors.toList());
            
            model.addAttribute("noticiasRelacionadas", relacionadas);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la noticia: " + e.getMessage());
            return "redirect:/noticias";
        }
        return "noticiasDetalle";
    }

    // Búsqueda de noticias públicas
    @GetMapping("/noticias/buscar")
    public String buscarNoticiasPublicas(@RequestParam String q, Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.buscarPorTitulo(q);
            
            if (noticias == null) {
                noticias = Collections.emptyList();
            }
            
            // Ordenar por ID descendente
            noticias.sort(Comparator.comparing(NoticiaDTO::getId).reversed());
            
            model.addAttribute("noticias", noticias);
            model.addAttribute("searchTerm", q);
            model.addAttribute("totalNoticias", noticias.size());
            
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
        }
        
        return "noticias";
    }

    // Noticias destacadas
    @GetMapping("/noticias/destacadas")
    public String verDestacadasPublicas(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            Model model) {
        
        try {
            List<NoticiaDTO> todasDestacadas = noticiaService.obtenerDestacadas();
            
            if (todasDestacadas == null) {
                todasDestacadas = Collections.emptyList();
            }
            
            // Ordenar por ID descendente
            todasDestacadas.sort(Comparator.comparing(NoticiaDTO::getId).reversed());
            
            // Paginación
            int totalNoticias = todasDestacadas.size();
            int totalPages = (int) Math.ceil((double) totalNoticias / size);
            
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, totalNoticias);
            
            List<NoticiaDTO> noticiasPagina = start < end ? 
                todasDestacadas.subList(start, end) : 
                Collections.emptyList();
            
            model.addAttribute("noticias", noticiasPagina);
            model.addAttribute("filterType", "destacadas");
            model.addAttribute("totalNoticias", totalNoticias);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar noticias destacadas: " + e.getMessage());
        }
        
        return "noticias";
    }

    // Noticias recientes (reemplazado por últimas noticias)
    @GetMapping("/noticias/ultimas")
    public String verUltimasNoticiasPublicas(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            Model model) {
        
        try {
            // Obtener todas las activas y ordenar por ID
            List<NoticiaDTO> todasNoticias = noticiaService.obtenerTodasActivas();
            
            if (todasNoticias == null) {
                todasNoticias = Collections.emptyList();
            }
            
            // Ordenar por ID descendente (las más recientes primero)
            todasNoticias.sort(Comparator.comparing(NoticiaDTO::getId).reversed());
            
            // Paginación
            int totalNoticias = todasNoticias.size();
            int totalPages = (int) Math.ceil((double) totalNoticias / size);
            
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, totalNoticias);
            
            List<NoticiaDTO> noticiasPagina = start < end ? 
                todasNoticias.subList(start, end) : 
                Collections.emptyList();
            
            model.addAttribute("noticias", noticiasPagina);
            model.addAttribute("filterType", "ultimas");
            model.addAttribute("totalNoticias", totalNoticias);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar las últimas noticias: " + e.getMessage());
        }
        
        return "noticias";
    }

    // Método de redirección para mantener compatibilidad con enlaces antiguos
    @GetMapping("/noticias/recientes")
    public String redirigirRecientes() {
        return "redirect:/noticias/ultimas";
    }

    // Ver noticias por categoría (ejemplo: noticias con enlace de Facebook)
    @GetMapping("/noticias/facebook")
    public String verNoticiasFacebook(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerConFacebook();
            
            if (noticias == null) {
                noticias = Collections.emptyList();
            }
            
            // Ordenar por ID descendente
            noticias.sort(Comparator.comparing(NoticiaDTO::getId).reversed());
            
            model.addAttribute("noticias", noticias);
            model.addAttribute("filterType", "facebook");
            model.addAttribute("totalNoticias", noticias.size());
            
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar noticias con Facebook: " + e.getMessage());
        }
        
        return "noticias";
    }

    // Ver noticias populares (basado en destacadas)
    @GetMapping("/noticias/populares")
    public String verNoticiasPopulares(Model model) {
        try {
            List<NoticiaDTO> noticias = noticiaService.obtenerDestacadas();
            
            if (noticias == null) {
                noticias = Collections.emptyList();
            }
            
            // Ordenar por ID descendente
            noticias.sort(Comparator.comparing(NoticiaDTO::getId).reversed());
            
            model.addAttribute("noticias", noticias);
            model.addAttribute("filterType", "populares");
            model.addAttribute("totalNoticias", noticias.size());
            
        } catch (Exception e) {
            model.addAttribute("noticias", Collections.emptyList());
            model.addAttribute("error", "Error al cargar noticias populares: " + e.getMessage());
        }
        
        return "noticias";
    }
}