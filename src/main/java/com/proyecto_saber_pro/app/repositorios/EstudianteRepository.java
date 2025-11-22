package com.proyecto_saber_pro.app.repositorios;

import com.proyecto_saber_pro.app.entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Buscar por número de documento
    Optional<Estudiante> findByNumeroDocumento(String numeroDocumento);
    
    // Buscar por correo electrónico
    Optional<Estudiante> findByCorreoElectronico(String correoElectronico);
    
    // Buscar por número de registro
    Optional<Estudiante> findByNumeroRegistro(String numeroRegistro);
    
    // Buscar estudiantes activos
    List<Estudiante> findByActivoTrue();
    
    // Buscar por programa académico
    List<Estudiante> findByPrograma(String programa);
    
    // Buscar por tipo de programa
    List<Estudiante> findByTipoPrograma(String tipoPrograma);
    
    // Buscar por semestre
    List<Estudiante> findBySemestre(Integer semestre);
    
    // Buscar por programa y semestre
    List<Estudiante> findByProgramaAndSemestre(String programa, Integer semestre);
    
    // Buscar estudiantes con resultados
    @Query("SELECT e FROM Estudiante e LEFT JOIN FETCH e.resultados WHERE e.activo = true")
    List<Estudiante> findAllWithResultados();
    
    // Buscar estudiante por ID con sus resultados
    @Query("SELECT e FROM Estudiante e LEFT JOIN FETCH e.resultados WHERE e.id = :id")
    Optional<Estudiante> findByIdWithResultados(@Param("id") Long id);
    
    // Buscar por nombre o apellido (búsqueda parcial)
    @Query("SELECT e FROM Estudiante e WHERE " +
           "(LOWER(e.primerNombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(e.segundoNombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(e.primerApellido) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(e.segundoApellido) LIKE LOWER(CONCAT('%', :termino, '%'))) " +
           "AND e.activo = true")
    List<Estudiante> buscarPorNombreOApellido(@Param("termino") String termino);
    
    // Buscar por número de documento o número de registro
    @Query("SELECT e FROM Estudiante e WHERE e.numeroDocumento = :identificador OR e.numeroRegistro = :identificador")
    Optional<Estudiante> findByNumeroDocumentoOrNumeroRegistro(@Param("identificador") String identificador);
    
    // Contar estudiantes por programa
    @Query("SELECT e.programa, COUNT(e) FROM Estudiante e WHERE e.activo = true GROUP BY e.programa")
    List<Object[]> countByPrograma();
    
    // Contar estudiantes por semestre
    @Query("SELECT e.semestre, COUNT(e) FROM Estudiante e WHERE e.activo = true GROUP BY e.semestre")
    List<Object[]> countBySemestre();
    
    // Verificar existencia por número de documento
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    // Verificar existencia por número de registro
    boolean existsByNumeroRegistro(String numeroRegistro);
    
    // Buscar estudiantes sin resultados
    @Query("SELECT e FROM Estudiante e WHERE e.activo = true AND e.resultados IS EMPTY")
    List<Estudiante> findEstudiantesSinResultados();
    
    // Buscar estudiantes con resultados recientes
    @Query("SELECT e FROM Estudiante e WHERE e.activo = true AND SIZE(e.resultados) > 0")
    List<Estudiante> findEstudiantesConResultados();
}