package com.proyecto_saber_pro.app.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;
    
    @NotBlank(message = "El número de documento es obligatorio")
    @Column(name = "numero_documento", unique = true, nullable = false, length = 20)
    private String numeroDocumento;
    
    @NotBlank(message = "El primer apellido es obligatorio")
    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;
    
    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;
    
    @NotBlank(message = "El primer nombre es obligatorio")
    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;
    
    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;
    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Column(name = "correo_electronico", unique = true, nullable = false, length = 100)
    private String correoElectronico;
    
    @Column(name = "numero_telefonico", length = 20)
    private String numeroTelefonico;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "El rol es obligatorio")
    @Column(nullable = false, length = 20)
    private String rol;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Usuario(String tipoDocumento, String numeroDocumento, String primerApellido, 
                  String primerNombre, String correoElectronico, String password, String rol) {
        this();
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.primerApellido = primerApellido;
        this.primerNombre = primerNombre;
        this.correoElectronico = correoElectronico;
        this.password = password;
        this.rol = rol;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    
    public String getNumeroTelefonico() { return numeroTelefonico; }
    public void setNumeroTelefonico(String numeroTelefonico) { this.numeroTelefonico = numeroTelefonico; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    // Método para obtener nombre completo
    public String getNombreCompleto() {
        return primerNombre + 
               (segundoNombre != null ? " " + segundoNombre : "") + 
               " " + primerApellido + 
               (segundoApellido != null ? " " + segundoApellido : "");
    }
}