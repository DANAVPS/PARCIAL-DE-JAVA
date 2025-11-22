package com.proyecto_saber_pro.app.controladores;

import com.proyecto_saber_pro.app.entidades.Usuario;
import com.proyecto_saber_pro.app.entidades.Administrador;
import com.proyecto_saber_pro.app.entidades.Coordinador;
import com.proyecto_saber_pro.app.entidades.Estudiante;
import com.proyecto_saber_pro.app.entidades.ResultadoSaberPro;
import com.proyecto_saber_pro.app.repositorios.CoordinadorRepository;
import com.proyecto_saber_pro.app.repositorios.EstudianteRepository;
import com.proyecto_saber_pro.app.repositorios.ResultadoSaberProRepository;
import com.proyecto_saber_pro.app.repositorios.UsuarioRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    private CoordinadorRepository coordinadorRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ResultadoSaberProRepository resultadoSaberProRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping({"", "/"})
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !("ADMIN".equals(usuario.getRol()) || "ADMINISTRADOR".equals(usuario.getRol()))) {
            return "redirect:/auth/login";
        }

        // Estadísticas para el dashboard
        long totalCoordinadores = coordinadorRepository.count();
        long totalEstudiantes = estudianteRepository.count();
        long totalResultados = resultadoSaberProRepository.count();
        
        // CORRECCIÓN: Usar findByActivoTrue() en lugar de findAllActive()
        List<Coordinador> coordinadores = coordinadorRepository.findByActivoTrue();
        List<Estudiante> estudiantesRecientes = estudianteRepository.findByActivoTrue()
                .stream().limit(10).toList();

        model.addAttribute("usuario", usuario);
        model.addAttribute("totalCoordinadores", totalCoordinadores);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalResultados", totalResultados);
        model.addAttribute("coordinadores", coordinadores);
        model.addAttribute("estudiantesRecientes", estudiantesRecientes);
        model.addAttribute("coordinador", new Coordinador()); // Para el formulario

        return "admin/index";
    }

    @GetMapping("/coordinadores")
    public String listarCoordinadores(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        List<Coordinador> coordinadores = coordinadorRepository.findAllWithAdmin();
        model.addAttribute("usuario", usuario);
        model.addAttribute("coordinadores", coordinadores);

        return "admin/coordinadores";
    }

    @PostMapping("/coordinadores/activar/{id}")
    public String activarCoordinador(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        try {
            Coordinador coordinador = coordinadorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
            coordinador.setActivo(true);
            coordinadorRepository.save(coordinador);
            redirectAttributes.addFlashAttribute("success", "Coordinador activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar coordinador: " + e.getMessage());
        }

        return "redirect:/admin/coordinadores";
    }

    @PostMapping("/coordinadores/desactivar/{id}")
    public String desactivarCoordinador(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        try {
            Coordinador coordinador = coordinadorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
            coordinador.setActivo(false);
            coordinadorRepository.save(coordinador);
            redirectAttributes.addFlashAttribute("success", "Coordinador desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar coordinador: " + e.getMessage());
        }

        return "redirect:/admin/coordinadores";
    }

    @GetMapping("/estudiantes")
    public String listarEstudiantes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        List<Estudiante> estudiantes = estudianteRepository.findByActivoTrue();
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiantes", estudiantes);

        return "admin/estudiantes";
    }

    @GetMapping("/resultados")
    public String verResultados(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        List<ResultadoSaberPro> resultados = resultadoSaberProRepository.findAll();
        model.addAttribute("usuario", usuario);
        model.addAttribute("resultados", resultados);

        return "admin/resultados";
    }

    @GetMapping("/estadisticas")
    public String verEstadisticas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Obtener estadísticas
        Object[] statsGlobales = resultadoSaberProRepository.getEstadisticasPuntajesGlobales();
        List<Object[]> statsPorPrograma = resultadoSaberProRepository.getEstadisticasPorPrograma();
        List<Object[]> exoneradosPorPrograma = resultadoSaberProRepository.countExoneradosPorPrograma();

        model.addAttribute("usuario", usuario);
        model.addAttribute("statsGlobales", statsGlobales);
        model.addAttribute("statsPorPrograma", statsPorPrograma);
        model.addAttribute("exoneradosPorPrograma", exoneradosPorPrograma);

        return "admin/estadisticas";
    }
    
    
    @PostMapping("/coordinadores/crear")
    public String crearCoordinador(@ModelAttribute Coordinador coordinador,
                                   @RequestParam("password") String passwordPlana,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        System.out.println("PASO 1: Entró al método crearCoordinador");
        System.out.println("SessionId: " + session.getId());

        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
        
        if (usuarioActual == null) {
            System.out.println("ERROR: No hay usuario en sesión");
            System.out.println("Atributos en sesión: " + 
                java.util.Collections.list(session.getAttributeNames()));
            return "redirect:/auth/login";
        }

        // IMPORTANTE: Verificar que sea Administrador antes del cast
        if (!(usuarioActual instanceof Administrador)) {
            System.out.println("ERROR: Usuario no es Administrador. Tipo: " + 
                usuarioActual.getClass().getSimpleName());
            redirectAttributes.addFlashAttribute("error", 
                "Solo administradores pueden crear coordinadores");
            return "redirect:/admin";
        }

        Administrador admin = (Administrador) usuarioActual;
        System.out.println("PASO 2: Administrador autenticado: " + admin.getCorreoElectronico());

        try {
            // Validaciones
            if (usuarioRepository.existsByNumeroDocumento(coordinador.getNumeroDocumento())) {
                redirectAttributes.addFlashAttribute("error", "El documento ya existe");
                return "redirect:/admin";
            }
            if (usuarioRepository.existsByCorreoElectronico(coordinador.getCorreoElectronico())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya existe");
                return "redirect:/admin";
            }

            System.out.println("PASO 3: Validaciones OK");

            // Configurar coordinador
            coordinador.setPassword(passwordPlana);
            coordinador.setRol("COORDINADOR");
            coordinador.setActivo(true);
            coordinador.setFechaCreacion(LocalDateTime.now());
            coordinador.setCreadoPorAdmin(admin);
            coordinador.setFechaAsignacion(LocalDateTime.now());

            System.out.println("PASO 4: Datos configurados");
            System.out.println("   Documento: " + coordinador.getNumeroDocumento());
            System.out.println("   Correo: " + coordinador.getCorreoElectronico());
            System.out.println("   Área: " + coordinador.getAreaAsignada());
            System.out.println("   Creado por ID: " + admin.getId());

            System.out.println("PASO 5: Ejecutando save()...");
            Coordinador guardado = coordinadorRepository.save(coordinador);
            System.out.println("✅ GUARDADO EXITOSO! ID generado: " + guardado.getId());

            redirectAttributes.addFlashAttribute("success", 
                "Coordinador creado exitosamente");
            return "redirect:/admin";

        } catch (Exception e) {
            System.out.println("❌ ERROR EN SAVE! " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar: " + e.getMessage());
            return "redirect:/admin";
        }
    }
    
    @PostMapping("/coordinadores/editar/{id}")
    public String editarCoordinador(@PathVariable Long id,
                                  @ModelAttribute Coordinador coordinadorActualizado,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        try {
            Coordinador coordinador = coordinadorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

            // Actualizar campos editables
            coordinador.setPrimerNombre(coordinadorActualizado.getPrimerNombre());
            coordinador.setSegundoNombre(coordinadorActualizado.getSegundoNombre());
            coordinador.setPrimerApellido(coordinadorActualizado.getPrimerApellido());
            coordinador.setSegundoApellido(coordinadorActualizado.getSegundoApellido());
            coordinador.setCorreoElectronico(coordinadorActualizado.getCorreoElectronico());
            coordinador.setNumeroTelefonico(coordinadorActualizado.getNumeroTelefonico());
            coordinador.setAreaAsignada(coordinadorActualizado.getAreaAsignada());

            coordinadorRepository.save(coordinador);
            redirectAttributes.addFlashAttribute("success", "Coordinador actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar coordinador: " + e.getMessage());
        }

        return "redirect:/admin";
    }

    @PostMapping("/coordinadores/eliminar/{id}")
    public String eliminarCoordinador(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        try {
            Coordinador coordinador = coordinadorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
            
            // En lugar de eliminar físicamente, desactivamos
            coordinador.setActivo(false);
            coordinadorRepository.save(coordinador);
            
            redirectAttributes.addFlashAttribute("success", "Coordinador eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar coordinador: " + e.getMessage());
        }

        return "redirect:/admin";
    }
}