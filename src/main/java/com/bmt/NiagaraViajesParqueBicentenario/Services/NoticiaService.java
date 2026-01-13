package com.bmt.NiagaraViajesParqueBicentenario.Services;

import com.bmt.NiagaraViajesParqueBicentenario.Models.NoticiaDTO;
import com.bmt.NiagaraViajesParqueBicentenario.Models.Noticia;
import com.bmt.NiagaraViajesParqueBicentenario.Repositories.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticiaService {
    
    @Autowired
    private NoticiaRepository noticiaRepository;
    
    // Obtener todas las noticias (incluyendo inactivas)
    @Transactional(readOnly = true)
    public List<NoticiaDTO> obtenerTodas() {
        return noticiaRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener todas las noticias activas
    @Transactional(readOnly = true)
    public List<NoticiaDTO> obtenerTodasActivas() {
        return noticiaRepository.findByActivaTrueOrderByIdDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener las últimas N noticias activas
    @Transactional(readOnly = true)
    public List<NoticiaDTO> obtenerUltimasActivas(int limit) {
        return noticiaRepository.findUltimasActivas(limit)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener noticia por ID (puede estar inactiva)
    @Transactional(readOnly = true)
    public Optional<NoticiaDTO> obtenerPorId(Long id) {
        return noticiaRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Obtener noticia por ID (solo activa)
    @Transactional(readOnly = true)
    public Optional<NoticiaDTO> obtenerPorIdActiva(Long id) {
        return noticiaRepository.findById(id)
                .filter(Noticia::isActiva)
                .map(this::convertToDTO);
    }
    
    // Obtener noticias destacadas
    @Transactional(readOnly = true)
    public List<NoticiaDTO> obtenerDestacadas() {
        return noticiaRepository.findByDestacadaTrueAndActivaTrueOrderByIdDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener noticias con enlace de Facebook
    @Transactional(readOnly = true)
    public List<NoticiaDTO> obtenerConFacebook() {
        return noticiaRepository.findByEnlaceFacebookIsNotNullAndActivaTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar noticias por título
    @Transactional(readOnly = true)
    public List<NoticiaDTO> buscarPorTitulo(String titulo) {
        return noticiaRepository.findByTituloContainingIgnoreCaseAndActivaTrue(titulo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar noticias por título (todas, incluyendo inactivas)
    @Transactional(readOnly = true)
    public List<NoticiaDTO> buscarPorTituloTodas(String titulo) {
        return noticiaRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Crear nueva noticia
    @Transactional
    public NoticiaDTO crearNoticia(NoticiaDTO noticiaDTO) {
        Noticia noticia = convertToEntity(noticiaDTO);
        noticia.setActiva(true);
        
        Noticia savedNoticia = noticiaRepository.save(noticia);
        return convertToDTO(savedNoticia);
    }
    
    // Actualizar noticia
    @Transactional
    public Optional<NoticiaDTO> actualizarNoticia(Long id, NoticiaDTO noticiaDTO) {
        return noticiaRepository.findById(id)
                .map(existingNoticia -> {
                    updateEntityFromDTO(existingNoticia, noticiaDTO);
                    
                    Noticia updatedNoticia = noticiaRepository.save(existingNoticia);
                    return convertToDTO(updatedNoticia);
                });
    }
    
    // Eliminar noticia (soft delete)
    @Transactional
    public boolean eliminarNoticia(Long id) {
        return noticiaRepository.findById(id)
                .map(noticia -> {
                    noticia.setActiva(false);
                    noticiaRepository.save(noticia);
                    return true;
                })
                .orElse(false);
    }
    
    // Reactivar noticia
    @Transactional
    public boolean reactivarNoticia(Long id) {
        return noticiaRepository.findById(id)
                .map(noticia -> {
                    noticia.setActiva(true);
                    noticiaRepository.save(noticia);
                    return true;
                })
                .orElse(false);
    }
    
    // Cambiar estado de destacada
    @Transactional
    public boolean toggleDestacada(Long id) {
        return noticiaRepository.findById(id)
                .map(noticia -> {
                    noticia.setDestacada(!noticia.isDestacada());
                    noticiaRepository.save(noticia);
                    return true;
                })
                .orElse(false);
    }
    
    // Contar noticias activas
    @Transactional(readOnly = true)
    public long contarActivas() {
        return noticiaRepository.countByActivaTrue();
    }
    
    // Contar noticias inactivas
    @Transactional(readOnly = true)
    public long contarInactivas() {
        return noticiaRepository.countByActivaFalse();
    }
    
    // Obtener noticias inactivas
    @Transactional(readOnly = true)
    public List<NoticiaDTO> obtenerInactivas() {
        return noticiaRepository.findByActivaFalseOrderByIdDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversión privados
    private NoticiaDTO convertToDTO(Noticia noticia) {
        return new NoticiaDTO(
            noticia.getId(),
            noticia.getTitulo(),
            noticia.getContenido(),
            noticia.getImagenUrl(),
            noticia.getEnlaceFacebook(),
            noticia.getTextoFacebook(),
            noticia.isDestacada(),
            noticia.isActiva()
        );
    }
    
    private Noticia convertToEntity(NoticiaDTO dto) {
        Noticia noticia = new Noticia();
        updateEntityFromDTO(noticia, dto);
        return noticia;
    }
    
    private void updateEntityFromDTO(Noticia noticia, NoticiaDTO dto) {
        noticia.setTitulo(dto.getTitulo());
        noticia.setContenido(dto.getContenido());
        noticia.setImagenUrl(dto.getImagenUrl());
        noticia.setEnlaceFacebook(dto.getEnlaceFacebook());
        noticia.setTextoFacebook(dto.getTextoFacebook());
        noticia.setDestacada(dto.isDestacada());
        noticia.setActiva(dto.isActiva());
    }
}