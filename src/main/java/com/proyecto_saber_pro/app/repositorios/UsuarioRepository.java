package com.proyecto_saber_pro.app.repositorios;

import com.proyecto_saber_pro.app.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por número de documento
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);
    
    // Buscar por correo electrónico
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    
    // Buscar por rol
    List<Usuario> findByRol(String rol);
    
    // Buscar usuarios activos por rol
    List<Usuario> findByRolAndActivoTrue(String rol);
    
    // Buscar por número de documento o correo electrónico
    @Query("SELECT u FROM Usuario u WHERE u.numeroDocumento = :identificador OR u.correoElectronico = :identificador")
    Optional<Usuario> findByNumeroDocumentoOrCorreoElectronico(@Param("identificador") String identificador);
    
    // Verificar existencia por número de documento
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    // Verificar existencia por correo electrónico
    boolean existsByCorreoElectronico(String correoElectronico);
    
    // Buscar por nombre o apellido (búsqueda parcial)
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.primerNombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.segundoNombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.primerApellido) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.segundoApellido) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Usuario> buscarPorNombreOApellido(@Param("termino") String termino);
    
    // Contar usuarios por rol
    long countByRol(String rol);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Buscar usuarios inactivos
    List<Usuario> findByActivoFalse();
}