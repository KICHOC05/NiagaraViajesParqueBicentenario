package com.bmt.NiagaraViajesParqueBicentenario.Models;

import jakarta.validation.constraints.*;

public class TravelPackageDto {
    
    private Long id;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
    private String titulo;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "El número de días es obligatorio")
    @Min(value = 1, message = "El número de días debe ser al menos 1")
    private Integer dias;
    
    @NotNull(message = "El número de noches es obligatorio")
    @Min(value = 0, message = "El número de noches no puede ser negativo")
    private Integer noches;
    
    @NotNull(message = "El número de personas es obligatorio")
    @Min(value = 1, message = "El número de personas debe ser al menos 1")
    private Integer personas;
    
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 500, message = "La URL de la imagen no puede exceder los 500 caracteres")
    private String imagenUrl;

    // Constructores
    public TravelPackageDto() {}
    
    public TravelPackageDto(String titulo, Double precio, String descripcion, 
                           Integer dias, Integer noches, Integer personas, String imagenUrl) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.dias = dias;
        this.noches = noches;
        this.personas = personas;
        this.imagenUrl = imagenUrl;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getNoches() {
        return noches;
    }

    public void setNoches(Integer noches) {
        this.noches = noches;
    }

    public Integer getPersonas() {
        return personas;
    }

    public void setPersonas(Integer personas) {
        this.personas = personas;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}