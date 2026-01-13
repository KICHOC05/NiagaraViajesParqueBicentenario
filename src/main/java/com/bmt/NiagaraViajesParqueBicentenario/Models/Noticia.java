package com.bmt.NiagaraViajesParqueBicentenario.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "noticias")
public class Noticia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;
    
    @NotBlank(message = "El contenido es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @Size(max = 500, message = "La URL de la imagen no puede exceder los 500 caracteres")
    @Column(length = 500)
    private String imagenUrl;
    
    @Size(max = 500, message = "El enlace de Facebook no puede exceder los 500 caracteres")
    @Column(name = "enlace_facebook", length = 500)
    private String enlaceFacebook;
    
    @Column(name = "texto_facebook", length = 500)
    private String textoFacebook;
    
    @Column(name = "es_destacada", nullable = false)
    private boolean destacada = false;
    
    @Column(name = "es_activa", nullable = false)
    private boolean activa = true;
    
    // Constructor vacío
    public Noticia() {}
    
    // Constructor con parámetros principales
    public Noticia(String titulo, String contenido, String imagenUrl, String enlaceFacebook) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenUrl = imagenUrl;
        this.enlaceFacebook = enlaceFacebook;
    }
    
    // Constructor completo
    public Noticia(String titulo, String contenido, String imagenUrl, 
                   String enlaceFacebook, String textoFacebook, 
                   boolean destacada, boolean activa) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenUrl = imagenUrl;
        this.enlaceFacebook = enlaceFacebook;
        this.textoFacebook = textoFacebook;
        this.destacada = destacada;
        this.activa = activa;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getEnlaceFacebook() {
        return enlaceFacebook;
    }

    public void setEnlaceFacebook(String enlaceFacebook) {
        this.enlaceFacebook = enlaceFacebook;
    }

    public String getTextoFacebook() {
        return textoFacebook;
    }

    public void setTextoFacebook(String textoFacebook) {
        this.textoFacebook = textoFacebook;
    }

    public boolean isDestacada() {
        return destacada;
    }

    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}