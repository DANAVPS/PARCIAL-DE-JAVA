package com.proyecto_saber_pro.app.repositorios;

import com.proyecto_saber_pro.app.entidades.ResultadoSaberPro;
import com.proyecto_saber_pro.app.entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoSaberProRepository extends JpaRepository<ResultadoSaberPro, Long> {
    
    // Buscar resultados por estudiante
    List<ResultadoSaberPro> findByEstudiante(Estudiante estudiante);
    
    // Buscar resultados por estudiante ordenados por fecha más reciente
    List<ResultadoSaberPro> findByEstudianteOrderByFechaExamenDesc(Estudiante estudiante);
    
    // Buscar resultados por rango de puntaje global
    List<ResultadoSaberPro> findByPuntajeGlobalBetween(BigDecimal minPuntaje, BigDecimal maxPuntaje);
    
    // Buscar resultados por fecha de examen
    List<ResultadoSaberPro> findByFechaExamen(LocalDate fechaExamen);
    
    // Buscar resultados entre fechas
    List<ResultadoSaberPro> findByFechaExamenBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Buscar resultados con beneficios (exonerados)
    List<ResultadoSaberPro> findByExoneradoTrabajoGradoTrue();
    
    // Buscar resultados por nivel global
    List<ResultadoSaberPro> findByNivelGlobal(String nivelGlobal);
    
    // Buscar el resultado más reciente de un estudiante
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.estudiante = :estudiante ORDER BY r.fechaExamen DESC LIMIT 1")
    Optional<ResultadoSaberPro> findMostRecentByEstudiante(@Param("estudiante") Estudiante estudiante);
    
    // Estadísticas de puntajes globales
    @Query("SELECT AVG(r.puntajeGlobal), MIN(r.puntajeGlobal), MAX(r.puntajeGlobal) FROM ResultadoSaberPro r")
    Object[] getEstadisticasPuntajesGlobales();
    
    // Contar resultados por rango de puntaje
    @Query("SELECT COUNT(r) FROM ResultadoSaberPro r WHERE " +
           "r.puntajeGlobal BETWEEN :minPuntaje AND :maxPuntaje")
    Long countByRangoPuntaje(@Param("minPuntaje") BigDecimal minPuntaje, 
                            @Param("maxPuntaje") BigDecimal maxPuntaje);
    
    // Buscar estudiantes con mejores puntajes
    @Query("SELECT r FROM ResultadoSaberPro r ORDER BY r.puntajeGlobal DESC LIMIT :top")
    List<ResultadoSaberPro> findTopResultados(@Param("top") int top);
    
    // Buscar resultados por programa del estudiante
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.estudiante.programa = :programa")
    List<ResultadoSaberPro> findByProgramaEstudiante(@Param("programa") String programa);
    
    // Buscar resultados por tipo de programa del estudiante
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.estudiante.tipoPrograma = :tipoPrograma")
    List<ResultadoSaberPro> findByTipoProgramaEstudiante(@Param("tipoPrograma") String tipoPrograma);
    
    // Estadísticas por programa
    @Query("SELECT r.estudiante.programa, AVG(r.puntajeGlobal), COUNT(r) " +
           "FROM ResultadoSaberPro r GROUP BY r.estudiante.programa")
    List<Object[]> getEstadisticasPorPrograma();
    
    // Buscar resultados con beneficios específicos
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.becaDerechosGrado > 0")
    List<ResultadoSaberPro> findResultadosConBeca();
    
    // Contar estudiantes exonerados por programa
    @Query("SELECT r.estudiante.programa, COUNT(r) FROM ResultadoSaberPro r " +
           "WHERE r.exoneradoTrabajoGrado = true GROUP BY r.estudiante.programa")
    List<Object[]> countExoneradosPorPrograma();
    
    // Buscar resultados próximos a vencer (vigencia del incentivo)
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.vigenciaIncentivo BETWEEN :hoy AND :fechaLimite")
    List<ResultadoSaberPro> findResultadosPorVencer(@Param("hoy") LocalDate hoy, 
                                                   @Param("fechaLimite") LocalDate fechaLimite);
    
    // Verificar si un estudiante ya tiene resultado para una fecha específica
    boolean existsByEstudianteAndFechaExamen(Estudiante estudiante, LocalDate fechaExamen);
}