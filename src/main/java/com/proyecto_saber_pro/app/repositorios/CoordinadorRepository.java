package com.proyecto_saber_pro.app.repositorios;

import com.proyecto_saber_pro.app.entidades.Coordinador;
import com.proyecto_saber_pro.app.entidades.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    
    // Buscar por número de documento
    Optional<Coordinador> findByNumeroDocumento(String numeroDocumento);
    
    // Buscar por correo electrónico
    Optional<Coordinador> findByCorreoElectronico(String correoElectronico);
    
    // Buscar por área asignada
    List<Coordinador> findByAreaAsignada(String areaAsignada);
    
    // Buscar coordinadores activos
    List<Coordinador> findByActivoTrue();
    
    // Buscar coordinadores creados por un administrador específico
    List<Coordinador> findByCreadoPorAdmin(Administrador administrador);
    
    // Buscar por área asignada (búsqueda parcial)
    @Query("SELECT c FROM Coordinador c WHERE LOWER(c.areaAsignada) LIKE LOWER(CONCAT('%', :area, '%')) AND c.activo = true")
    List<Coordinador> buscarPorAreaAsignada(@Param("area") String area);
    
    // Buscar coordinadores con información del administrador que los creó
    @Query("SELECT c FROM Coordinador c LEFT JOIN FETCH c.creadoPorAdmin WHERE c.activo = true")
    List<Coordinador> findAllWithAdmin();
    
    // Contar coordinadores por área
    @Query("SELECT c.areaAsignada, COUNT(c) FROM Coordinador c WHERE c.activo = true GROUP BY c.areaAsignada")
    List<Object[]> countByAreaAsignada();
    
    @Query("SELECT c FROM Coordinador c WHERE c.activo = true ORDER BY c.primerApellido, c.primerNombre")
    List<Coordinador> findAllActive();
    
    // Verificar existencia por número de documento
    boolean existsByNumeroDocumento(String numeroDocumento);

    
    @Query("SELECT DISTINCT c.areaAsignada FROM Coordinador c WHERE c.activo = true")
    List<String> findDistinctAreaAsignada();

    // Y este método para contar áreas diferentes:
    default long countDistinctAreas() {
        return findDistinctAreaAsignada().size();
    }
}