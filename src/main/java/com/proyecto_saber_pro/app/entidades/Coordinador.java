package com.proyecto_saber_pro.app.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coordinadores")
@PrimaryKeyJoinColumn(name = "id")
public class Coordinador extends Usuario {
    
    @Column(name = "area_asignada", length = 100)
    private String areaAsignada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_admin_id")
    private Administrador creadoPorAdmin;
    
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;
    
    // Constructores
    public Coordinador() {
        super();
        setRol("COORDINADOR");
        this.fechaAsignacion = LocalDateTime.now();
    }
    
    public Coordinador(String tipoDocumento, String numeroDocumento, String primerApellido, 
                      String primerNombre, String correoElectronico, String password, 
                      String areaAsignada, Administrador creadoPorAdmin) {
        super(tipoDocumento, numeroDocumento, primerApellido, primerNombre, correoElectronico, password, "COORDINADOR");
        this.areaAsignada = areaAsignada;
        this.creadoPorAdmin = creadoPorAdmin;
        this.fechaAsignacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public String getAreaAsignada() { return areaAsignada; }
    public void setAreaAsignada(String areaAsignada) { this.areaAsignada = areaAsignada; }
    
    public Administrador getCreadoPorAdmin() { return creadoPorAdmin; }
    public void setCreadoPorAdmin(Administrador creadoPorAdmin) { this.creadoPorAdmin = creadoPorAdmin; }
    
    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
}
