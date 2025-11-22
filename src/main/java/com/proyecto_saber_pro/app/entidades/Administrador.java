package com.proyecto_saber_pro.app.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "id")
public class Administrador extends Usuario {
    
    @Column(name = "permisos_especiales", columnDefinition = "TEXT")
    private String permisosEspeciales;
    
    // Constructores
    public Administrador() {
        super();
        setRol("ADMINISTRADOR");
    }
    
    public Administrador(String tipoDocumento, String numeroDocumento, String primerApellido, 
                        String primerNombre, String correoElectronico, String password) {
        super(tipoDocumento, numeroDocumento, primerApellido, primerNombre, correoElectronico, password, "ADMINISTRADOR");
    }
    
    // Getters y Setters
    public String getPermisosEspeciales() { return permisosEspeciales; }
    public void setPermisosEspeciales(String permisosEspeciales) { this.permisosEspeciales = permisosEspeciales; }
}