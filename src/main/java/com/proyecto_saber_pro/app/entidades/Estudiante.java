package com.proyecto_saber_pro.app.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "estudiantes")
@PrimaryKeyJoinColumn(name = "id")
public class Estudiante extends Usuario {
    
    @Column(name = "numero_registro", unique = true, length = 50)
    private String numeroRegistro;
    
    private Integer semestre;
    
    @Column(length = 100)
    private String programa;
    
    @Column(name = "tipo_programa", length = 20)
    private String tipoPrograma;
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ResultadoSaberPro> resultados = new ArrayList<>();
    
    // Constructores
    public Estudiante() {
        super();
        setRol("ESTUDIANTE");
    }
    
    public Estudiante(String tipoDocumento, String numeroDocumento, String primerApellido, 
                     String primerNombre, String correoElectronico, String password,
                     String numeroRegistro, Integer semestre, String programa, String tipoPrograma) {
        super(tipoDocumento, numeroDocumento, primerApellido, primerNombre, correoElectronico, password, "ESTUDIANTE");
        this.numeroRegistro = numeroRegistro;
        this.semestre = semestre;
        this.programa = programa;
        this.tipoPrograma = tipoPrograma;
    }
    
    // Getters y Setters
    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }
    
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    
    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
    
    public String getTipoPrograma() { return tipoPrograma; }
    public void setTipoPrograma(String tipoPrograma) { this.tipoPrograma = tipoPrograma; }
    
    public List<ResultadoSaberPro> getResultados() { return resultados; }
    public void setResultados(List<ResultadoSaberPro> resultados) { this.resultados = resultados; }
    
    // Método para obtener el resultado más reciente
    public ResultadoSaberPro getResultadoMasReciente() {
        if (resultados == null || resultados.isEmpty()) {
            return null;
        }
        return resultados.get(resultados.size() - 1); // Asumiendo que el último es el más reciente
    }
}