package com.bmt.NiagaraViajesParqueBicentenario.Repositories;

import com.bmt.NiagaraViajesParqueBicentenario.Models.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
    
    // Encontrar noticias activas ordenadas por ID (descendente)
    List<Noticia> findByActivaTrueOrderByIdDesc();
    
    // Encontrar todas las noticias ordenadas por ID (descendente)
    List<Noticia> findAllByOrderByIdDesc();
    
    // Encontrar noticias destacadas y activas
    List<Noticia> findByDestacadaTrueAndActivaTrueOrderByIdDesc();
    
    // Buscar noticias por título (activas)
    List<Noticia> findByTituloContainingIgnoreCaseAndActivaTrue(String titulo);
    
    // Buscar noticias por título (todas)
    List<Noticia> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar noticias inactivas
    List<Noticia> findByActivaFalseOrderByIdDesc();
    
    // Buscar noticias por estado
    List<Noticia> findByActivaOrderByIdDesc(boolean activa);
    
    // Encontrar noticias con enlace de Facebook
    List<Noticia> findByEnlaceFacebookIsNotNullAndActivaTrue();
    
    // Encontrar noticias sin enlace de Facebook
    List<Noticia> findByEnlaceFacebookIsNullAndActivaTrue();
    
    // Contar noticias activas
    long countByActivaTrue();
    
    // Contar noticias inactivas
    long countByActivaFalse();
    
    // Buscar las últimas N noticias activas
    @Query("SELECT n FROM Noticia n WHERE n.activa = true ORDER BY n.id DESC LIMIT :limit")
    List<Noticia> findUltimasActivas(int limit);
}