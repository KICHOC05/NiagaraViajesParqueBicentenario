package com.bmt.NiagaraViajesParqueBicentenario.Models;

import jakarta.validation.constraints.*;

public class RegisterDto {

    @NotEmpty(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y espacios")
    private String Nombre;
    
    @NotEmpty(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y espacios")
    private String Apellido;
    
    @NotEmpty(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.0-9]*$", 
             message = "Debe ser un número de teléfono válido")
    private String telefono;
    
    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres")
    private String direccion;
    
    @NotEmpty(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
    private String contraseña;
    
    @NotEmpty(message = "Debe confirmar la contraseña")
    private String confirmarContraseña;

    // Validación personalizada para coincidencia de contraseñas
    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordMatching() {
        if (contraseña == null || confirmarContraseña == null) {
            return false;
        }
        return contraseña.equals(confirmarContraseña);
    }

    // Getters y Setters
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }

    public void setConfirmarContraseña(String confirmarContraseña) {
        this.confirmarContraseña = confirmarContraseña;
    }
}