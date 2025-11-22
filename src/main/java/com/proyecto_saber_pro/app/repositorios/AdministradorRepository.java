package com.proyecto_saber_pro.app.repositorios;

import com.proyecto_saber_pro.app.entidades.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    
    // Buscar por número de documento (heredado de Usuario)
    Optional<Administrador> findByNumeroDocumento(String numeroDocumento);
    
    // Buscar por correo electrónico (heredado de Usuario)
    Optional<Administrador> findByCorreoElectronico(String correoElectronico);
    
    // Buscar administradores activos
    List<Administrador> findByActivoTrue();
    
    // Buscar todos los administradores con información básica
    @Query("SELECT a FROM Administrador a WHERE a.activo = true ORDER BY a.primerApellido, a.primerNombre")
    List<Administrador> findAllActive();
    
    // Verificar existencia por número de documento
    boolean existsByNumeroDocumento(String numeroDocumento);
}