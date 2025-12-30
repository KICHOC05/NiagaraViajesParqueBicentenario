package com.bmt.NiagaraViajesParqueBicentenario.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NoticiaDTO {
    
    private Long id;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
    private String titulo;
    
    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;
    
    @Size(max = 500, message = "La URL de la imagen no puede exceder los 500 caracteres")
    private String imagenUrl;
    
    @Size(max = 500, message = "El enlace de Facebook no puede exceder los 500 caracteres")
    private String enlaceFacebook;
    
    @Size(max = 500, message = "El texto de Facebook no puede exceder los 500 caracteres")
    private String textoFacebook;
    
    private boolean destacada;
    
    private boolean activa;
    
    // Constructor vacío
    public NoticiaDTO() {}
    
    // Constructor con todos los parámetros
    public NoticiaDTO(Long id, String titulo, String contenido, String imagenUrl, 
                     String enlaceFacebook, String textoFacebook, boolean destacada, 
                     boolean activa) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenUrl = imagenUrl;
        this.enlaceFacebook = enlaceFacebook;
        this.textoFacebook = textoFacebook;
        this.destacada = destacada;
        this.activa = activa;
    }
    
    // Método para convertir de Entity a DTO
    public static NoticiaDTO fromEntity(Noticia noticia) {
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
    
    // Método para convertir de DTO a Entity
    public Noticia toEntity() {
        Noticia noticia = new Noticia();
        
        noticia.setId(this.id);
        noticia.setTitulo(this.titulo);
        noticia.setContenido(this.contenido);
        noticia.setImagenUrl(this.imagenUrl);
        noticia.setEnlaceFacebook(this.enlaceFacebook);
        noticia.setTextoFacebook(this.textoFacebook);
        noticia.setDestacada(this.destacada);
        noticia.setActiva(this.activa);
        
        return noticia;
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