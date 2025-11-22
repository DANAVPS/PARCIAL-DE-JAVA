package com.proyecto_saber_pro.app.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "resultados_saber_pro")
public class ResultadoSaberPro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
    private Estudiante estudiante;
    
    @Column(name = "puntaje_global", precision = 6, scale = 2)
    private BigDecimal puntajeGlobal;
    
    @Column(name = "nivel_global", length = 50)
    private String nivelGlobal;
    
    @Column(name = "comunicacion_escrita", precision = 6, scale = 2)
    private BigDecimal comunicacionEscrita;
    
    @Column(name = "comunicacion_escrita_nivel", length = 50)
    private String comunicacionEscritaNivel;
    
    @Column(name = "razonamiento_cuantitativo", precision = 6, scale = 2)
    private BigDecimal razonamientoCuantitativo;
    
    @Column(name = "razonamiento_cuantitativo_nivel", length = 50)
    private String razonamientoCuantitativoNivel;
    
    @Column(name = "lectura_critica", precision = 6, scale = 2)
    private BigDecimal lecturaCritica;
    
    @Column(name = "lectura_critica_nivel", length = 50)
    private String lecturaCriticaNivel;
    
    @Column(name = "competencias_ciudadanas", precision = 6, scale = 2)
    private BigDecimal competenciasCiudadanas;
    
    @Column(name = "competencias_ciudadanas_nivel", length = 50)
    private String competenciasCiudadanasNivel;
    
    @Column(precision = 6, scale = 2)
    private BigDecimal ingles;
    
    @Column(name = "ingles_nivel", length = 50)
    private String inglesNivel;
    
    @Column(name = "nivel_ingles", length = 10)
    private String nivelIngles;
    
    @Column(name = "formulacion_proyectos", precision = 6, scale = 2)
    private BigDecimal formulacionProyectos;
    
    @Column(name = "formulacion_proyectos_nivel", length = 50)
    private String formulacionProyectosNivel;
    
    @Column(name = "pensamiento_cientifico", precision = 6, scale = 2)
    private BigDecimal pensamientoCientifico;
    
    @Column(name = "pensamiento_cientifico_nivel", length = 50)
    private String pensamientoCientificoNivel;
    
    @Column(name = "diseno_software", precision = 6, scale = 2)
    private BigDecimal disenoSoftware;
    
    @Column(name = "diseno_software_nivel", length = 50)
    private String disenoSoftwareNivel;
    
    @Column(name = "beneficio_obtenido", columnDefinition = "TEXT")
    private String beneficioObtenido;
    
    @Column(name = "exonerado_trabajo_grado")
    private Boolean exoneradoTrabajoGrado = false;
    
    @Column(name = "nota_trabajo_grado", precision = 3, scale = 1)
    private BigDecimal notaTrabajoGrado;
    
    @Column(name = "beca_derechos_grado")
    private Integer becaDerechosGrado = 0;
    
    @Column(name = "fecha_examen")
    private LocalDate fechaExamen;
    
    @Column(name = "vigencia_incentivo")
    private LocalDate vigenciaIncentivo;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    // Constructores
    public ResultadoSaberPro() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    public ResultadoSaberPro(Estudiante estudiante) {
        this();
        this.estudiante = estudiante;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    
    public BigDecimal getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(BigDecimal puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }
    
    public String getNivelGlobal() { return nivelGlobal; }
    public void setNivelGlobal(String nivelGlobal) { this.nivelGlobal = nivelGlobal; }
    
    public BigDecimal getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(BigDecimal comunicacionEscrita) { this.comunicacionEscrita = comunicacionEscrita; }
    
    public String getComunicacionEscritaNivel() { return comunicacionEscritaNivel; }
    public void setComunicacionEscritaNivel(String comunicacionEscritaNivel) { this.comunicacionEscritaNivel = comunicacionEscritaNivel; }
    
    public BigDecimal getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(BigDecimal razonamientoCuantitativo) { this.razonamientoCuantitativo = razonamientoCuantitativo; }
    
    public String getRazonamientoCuantitativoNivel() { return razonamientoCuantitativoNivel; }
    public void setRazonamientoCuantitativoNivel(String razonamientoCuantitativoNivel) { this.razonamientoCuantitativoNivel = razonamientoCuantitativoNivel; }
    
    public BigDecimal getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(BigDecimal lecturaCritica) { this.lecturaCritica = lecturaCritica; }
    
    public String getLecturaCriticaNivel() { return lecturaCriticaNivel; }
    public void setLecturaCriticaNivel(String lecturaCriticaNivel) { this.lecturaCriticaNivel = lecturaCriticaNivel; }
    
    public BigDecimal getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(BigDecimal competenciasCiudadanas) { this.competenciasCiudadanas = competenciasCiudadanas; }
    
    public String getCompetenciasCiudadanasNivel() { return competenciasCiudadanasNivel; }
    public void setCompetenciasCiudadanasNivel(String competenciasCiudadanasNivel) { this.competenciasCiudadanasNivel = competenciasCiudadanasNivel; }
    
    public BigDecimal getIngles() { return ingles; }
    public void setIngles(BigDecimal ingles) { this.ingles = ingles; }
    
    public String getInglesNivel() { return inglesNivel; }
    public void setInglesNivel(String inglesNivel) { this.inglesNivel = inglesNivel; }
    
    public String getNivelIngles() { return nivelIngles; }
    public void setNivelIngles(String nivelIngles) { this.nivelIngles = nivelIngles; }
    
    public BigDecimal getFormulacionProyectos() { return formulacionProyectos; }
    public void setFormulacionProyectos(BigDecimal formulacionProyectos) { this.formulacionProyectos = formulacionProyectos; }
    
    public String getFormulacionProyectosNivel() { return formulacionProyectosNivel; }
    public void setFormulacionProyectosNivel(String formulacionProyectosNivel) { this.formulacionProyectosNivel = formulacionProyectosNivel; }
    
    public BigDecimal getPensamientoCientifico() { return pensamientoCientifico; }
    public void setPensamientoCientifico(BigDecimal pensamientoCientifico) { this.pensamientoCientifico = pensamientoCientifico; }
    
    public String getPensamientoCientificoNivel() { return pensamientoCientificoNivel; }
    public void setPensamientoCientificoNivel(String pensamientoCientificoNivel) { this.pensamientoCientificoNivel = pensamientoCientificoNivel; }
    
    public BigDecimal getDisenoSoftware() { return disenoSoftware; }
    public void setDisenoSoftware(BigDecimal disenoSoftware) { this.disenoSoftware = disenoSoftware; }
    
    public String getDisenoSoftwareNivel() { return disenoSoftwareNivel; }
    public void setDisenoSoftwareNivel(String disenoSoftwareNivel) { this.disenoSoftwareNivel = disenoSoftwareNivel; }
    
    public String getBeneficioObtenido() { return beneficioObtenido; }
    public void setBeneficioObtenido(String beneficioObtenido) { this.beneficioObtenido = beneficioObtenido; }
    
    public Boolean getExoneradoTrabajoGrado() { return exoneradoTrabajoGrado; }
    public void setExoneradoTrabajoGrado(Boolean exoneradoTrabajoGrado) { this.exoneradoTrabajoGrado = exoneradoTrabajoGrado; }
    
    public BigDecimal getNotaTrabajoGrado() { return notaTrabajoGrado; }
    public void setNotaTrabajoGrado(BigDecimal notaTrabajoGrado) { this.notaTrabajoGrado = notaTrabajoGrado; }
    
    public Integer getBecaDerechosGrado() { return becaDerechosGrado; }
    public void setBecaDerechosGrado(Integer becaDerechosGrado) { this.becaDerechosGrado = becaDerechosGrado; }
    
    public LocalDate getFechaExamen() { return fechaExamen; }
    public void setFechaExamen(LocalDate fechaExamen) { this.fechaExamen = fechaExamen; }
    
    public LocalDate getVigenciaIncentivo() { return vigenciaIncentivo; }
    public void setVigenciaIncentivo(LocalDate vigenciaIncentivo) { this.vigenciaIncentivo = vigenciaIncentivo; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    // Método para calcular beneficios automáticamente
    @PrePersist
    @PreUpdate
    public void calcularBeneficios() {
        // 1. Calcular puntaje global (suma de los 5 módulos)
        BigDecimal pg = BigDecimal.ZERO;
        pg = pg.add(coalesce(comunicacionEscrita));
        pg = pg.add(coalesce(razonamientoCuantitativo));
        pg = pg.add(coalesce(lecturaCritica));
        pg = pg.add(coalesce(competenciasCiudadanas));
        pg = pg.add(coalesce(ingles));
        
        this.puntajeGlobal = pg;

        // 2. Vigencia +1 año
        if (fechaExamen != null) {
            this.vigenciaIncentivo = fechaExamen.plusYears(1);
        }

        // 3. Determinar si es Saber TyT o PRO
        boolean esTyT = (pg.doubleValue() <= 200); // TyT máximo 200, PRO máximo 300

        String beneficio = "Sin beneficio";
        boolean exonerado = false;
        BigDecimal nota = null;
        int beca = 0;

        if (esTyT) {
            // SABER TyT
            if (pg.doubleValue() >= 171) {
                beneficio = "Exonerado de trabajo de grado con nota 5.0 + 100% beca derechos de grado";
                exonerado = true; nota = BigDecimal.valueOf(5.0); beca = 100;
            } else if (pg.doubleValue() >= 151) {
                beneficio = "Exonerado de trabajo de grado con nota 4.7 + 50% beca derechos de grado";
                exonerado = true; nota = BigDecimal.valueOf(4.7); beca = 50;
            } else if (pg.doubleValue() >= 120) {
                beneficio = "Exonerado de trabajo de grado con nota 4.5";
                exonerado = true; nota = BigDecimal.valueOf(4.5);
            }
        } else {
            // SABER PRO
            if (pg.doubleValue() >= 241) {
                beneficio = "Exonerado de trabajo de grado con nota 5.0 + 100% beca derechos de grado";
                exonerado = true; nota = BigDecimal.valueOf(5.0); beca = 100;
            } else if (pg.doubleValue() >= 211) {
                beneficio = "Exonerado de trabajo de grado con nota 4.7 + 50% beca derechos de grado";
                exonerado = true; nota = BigDecimal.valueOf(4.7); beca = 50;
            } else if (pg.doubleValue() >= 180) {
                beneficio = "Exonerado de trabajo de grado con nota 4.5";
                exonerado = true; nota = BigDecimal.valueOf(4.5);
            }
        }

        this.beneficioObtenido = beneficio;
        this.exoneradoTrabajoGrado = exonerado;
        this.notaTrabajoGrado = nota;
        this.becaDerechosGrado = beca;
    }

    // Método auxiliar para evitar NullPointer
    private BigDecimal coalesce(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}