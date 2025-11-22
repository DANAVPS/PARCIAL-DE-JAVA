package com.proyecto_saber_pro.app.controladores;

import com.proyecto_saber_pro.app.entidades.Usuario;
import com.proyecto_saber_pro.app.entidades.Estudiante;
import com.proyecto_saber_pro.app.entidades.ResultadoSaberPro;
import com.proyecto_saber_pro.app.repositorios.EstudianteRepository;
import com.proyecto_saber_pro.app.repositorios.ResultadoSaberProRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ResultadoSaberProRepository resultadoSaberProRepository;

    @GetMapping("/")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ESTUDIANTE".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Obtener el estudiante con sus resultados
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByIdWithResultados(usuario.getId());
        if (estudianteOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        Estudiante estudiante = estudianteOpt.get();
        ResultadoSaberPro resultadoReciente = estudiante.getResultadoMasReciente();

        model.addAttribute("usuario", estudiante);
        model.addAttribute("resultadoReciente", resultadoReciente);
        model.addAttribute("totalResultados", estudiante.getResultados().size());

        return "estudiante/index";
    }

    @GetMapping("/identificacion")
    public String identificacion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ESTUDIANTE".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Obtener información completa del estudiante
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(usuario.getId());
        if (estudianteOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("usuario", estudianteOpt.get());
        return "estudiante/identificacion";
    }

    @GetMapping("/resultado-unico")
    public String resultadoUnico(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ESTUDIANTE".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Obtener el resultado más reciente
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(usuario.getId());
        if (estudianteOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        Estudiante estudiante = estudianteOpt.get();
        ResultadoSaberPro resultadoReciente = estudiante.getResultadoMasReciente();

        if (resultadoReciente == null) {
            model.addAttribute("error", "No se encontraron resultados para mostrar");
        }

        model.addAttribute("usuario", estudiante);
        model.addAttribute("resultado", resultadoReciente);

        return "estudiante/resultado-unico";
    }

    @GetMapping("/resultados-total")
    public String resultadosTotal(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ESTUDIANTE".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Obtener todos los resultados del estudiante ordenados por fecha
        List<ResultadoSaberPro> resultados = resultadoSaberProRepository
                .findByEstudianteOrderByFechaExamenDesc(
                    estudianteRepository.findById(usuario.getId()).orElseThrow()
                );

        model.addAttribute("usuario", usuario);
        model.addAttribute("resultados", resultados);

        return "estudiante/resultados-total";
    }

    @GetMapping("/resultado-beneficio")
    public String resultadoBeneficio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ESTUDIANTE".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Obtener el resultado más reciente con beneficios
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(usuario.getId());
        if (estudianteOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        Estudiante estudiante = estudianteOpt.get();
        ResultadoSaberPro resultadoReciente = estudiante.getResultadoMasReciente();

        model.addAttribute("usuario", estudiante);
        model.addAttribute("resultado", resultadoReciente);

        if (resultadoReciente != null && resultadoReciente.getExoneradoTrabajoGrado()) {
            model.addAttribute("tieneBeneficios", true);
            model.addAttribute("beneficio", resultadoReciente.getBeneficioObtenido());
            model.addAttribute("nota", resultadoReciente.getNotaTrabajoGrado());
            model.addAttribute("beca", resultadoReciente.getBecaDerechosGrado());
            model.addAttribute("vigencia", resultadoReciente.getVigenciaIncentivo());
        } else {
            model.addAttribute("tieneBeneficios", false);
            model.addAttribute("mensaje", "No se han obtenido beneficios con los resultados actuales");
        }

        return "estudiante/resultado-beneficio";
    }

    @GetMapping("/detalle-resultado/{id}")
    public String detalleResultado(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ESTUDIANTE".equals(usuario.getRol())) {
            return "redirect:/auth/login";
        }

        // Verificar que el resultado pertenezca al estudiante
        Optional<ResultadoSaberPro> resultadoOpt = resultadoSaberProRepository.findById(id);
        if (resultadoOpt.isEmpty() || !resultadoOpt.get().getEstudiante().getId().equals(usuario.getId())) {
            return "redirect:/estudiante/resultados-total";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("resultado", resultadoOpt.get());

        return "estudiante/detalle-resultado";
    }
}