package com.proyecto_saber_pro.app.controladores;

import com.proyecto_saber_pro.app.entidades.Usuario;
import com.proyecto_saber_pro.app.entidades.Estudiante;
import com.proyecto_saber_pro.app.entidades.ResultadoSaberPro;
import com.proyecto_saber_pro.app.repositorios.EstudianteRepository;
import com.proyecto_saber_pro.app.repositorios.ResultadoSaberProRepository;
import com.proyecto_saber_pro.app.repositorios.UsuarioRepository;
import jakarta.servlet.http.HttpSession;

// ❌ ELIMINA ESTA LÍNEA INCORRECTA:
// import org.apache.poi.sl.usermodel.Sheet;

// ✅ ASEGÚRATE DE TENER ESTAS IMPORTACIONES CORRECTAS:
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

	@Autowired
	private EstudianteRepository estudianteRepository;

	@Autowired
	private ResultadoSaberProRepository resultadoSaberProRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping({ "", "/" })
	public String dashboard(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		// Estadísticas para el dashboard del coordinador
		long totalEstudiantes = estudianteRepository.count();
		long estudiantesConResultados = estudianteRepository.findEstudiantesConResultados().size();
		long estudiantesSinResultados = estudianteRepository.findEstudiantesSinResultados().size();

		List<Estudiante> estudiantesRecientes = estudianteRepository.findByActivoTrue().stream().limit(5).toList();

		model.addAttribute("usuario", usuario);
		model.addAttribute("totalEstudiantes", totalEstudiantes);
		model.addAttribute("estudiantesConResultados", estudiantesConResultados);
		model.addAttribute("estudiantesSinResultados", estudiantesSinResultados);
		model.addAttribute("estudiantesRecientes", estudiantesRecientes);

		return "coordinador/index";
	}

	// CRUD ALUMNOS
	@GetMapping("/crud-alumnos")
	public String crudAlumnos(HttpSession session, Model model) {
	    Usuario usuario = (Usuario) session.getAttribute("usuario");
	    if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
	        return "redirect:/auth/login";
	    }

	    // ← PROTECCIÓN CONTRA NULL
	    List<Estudiante> estudiantes = estudianteRepository.findByActivoTrue()
	            .stream()
	            .filter(e -> e != null && e.getNumeroDocumento() != null)
	            .toList();

	    model.addAttribute("usuario", usuario);
	    model.addAttribute("estudiantes", estudiantes);
	    
	    // Para el formulario de crear
	    model.addAttribute("nuevoEstudiante", new Estudiante());

	    return "coordinador/crud-alumnos";
	}

	@PostMapping("/estudiantes/crear")
	public String crearEstudiante(@ModelAttribute Estudiante estudiante, HttpSession session,
	                              RedirectAttributes redirectAttributes) {
	    
	    Usuario usuario = (Usuario) session.getAttribute("usuario");
	    if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
	        return "redirect:/auth/login";
	    }

	    try {
	        // 1. Validar documento único
	        if (usuarioRepository.existsByNumeroDocumento(estudiante.getNumeroDocumento())) {
	            redirectAttributes.addFlashAttribute("error", "El número de documento ya existe");
	            return "redirect:/coordinador/crud-alumnos";
	        }

	        // 2. Validar número de registro único
	        if (estudiante.getNumeroRegistro() != null && 
	            !estudiante.getNumeroRegistro().trim().isEmpty() &&
	            estudianteRepository.existsByNumeroRegistro(estudiante.getNumeroRegistro().trim())) {
	            redirectAttributes.addFlashAttribute("error", "El número de registro ya existe");
	            return "redirect:/coordinador/crud-alumnos";
	        }

	        // 3. VALORES POR DEFECTO OBLIGATORIOS
	        if (estudiante.getTipoDocumento() == null || estudiante.getTipoDocumento().trim().isEmpty()) {
	            estudiante.setTipoDocumento("CC"); // ← Soluciona el error de validación
	        }

	        if (estudiante.getRol() == null || estudiante.getRol().trim().isEmpty()) {
	            estudiante.setRol("ESTUDIANTE");
	        }

	        // 4. CONTRASEÑA SEGURA: la cédula (o estudiante123 si prefieres)
	        estudiante.setPassword(estudiante.getNumeroDocumento()); // ← Mejor: cada uno entra con su cédula
	        // O si quieres estudiante123:
	        // estudiante.setPassword("estudiante123");

	        // 5. Estado inicial
	        estudiante.setActivo(true);
	        estudiante.setFechaCreacion(LocalDateTime.now());

	        // 6. Guardar
	        estudianteRepository.save(estudiante);
	        redirectAttributes.addFlashAttribute("success", "Estudiante creado correctamente. Contraseña: " + estudiante.getNumeroDocumento());

	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error al crear estudiante: " + e.getMessage());
	        e.printStackTrace(); // para que veas el error real en consola
	    }

	    return "redirect:/coordinador/crud-alumnos";
	}

	@PostMapping("/estudiantes/editar/{id}")
	public String editarEstudiante(@PathVariable Long id, @ModelAttribute Estudiante estudianteActualizado,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		try {
			Estudiante estudiante = estudianteRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

			// Actualizar campos
			estudiante.setPrimerNombre(estudianteActualizado.getPrimerNombre());
			estudiante.setSegundoNombre(estudianteActualizado.getSegundoNombre());
			estudiante.setPrimerApellido(estudianteActualizado.getPrimerApellido());
			estudiante.setSegundoApellido(estudianteActualizado.getSegundoApellido());
			estudiante.setCorreoElectronico(estudianteActualizado.getCorreoElectronico());
			estudiante.setNumeroTelefonico(estudianteActualizado.getNumeroTelefonico());
			estudiante.setNumeroRegistro(estudianteActualizado.getNumeroRegistro());
			estudiante.setSemestre(estudianteActualizado.getSemestre());
			estudiante.setPrograma(estudianteActualizado.getPrograma());
			estudiante.setTipoPrograma(estudianteActualizado.getTipoPrograma());

			estudianteRepository.save(estudiante);
			redirectAttributes.addFlashAttribute("success", "Estudiante actualizado exitosamente");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al actualizar estudiante: " + e.getMessage());
		}

		return "coordinador/editar-estudiante";
	}
	
	@GetMapping("/estudiantes/editar/{id}")
	public String mostrarEditarEstudiante(@PathVariable Long id, Model model) {
	    Estudiante estudiante = estudianteRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
	    model.addAttribute("estudiante", estudiante);
	    return "coordinador/editar-estudiante"; // ← la vista que ya te di
	}
	
	// Guardar calificación
	@PostMapping("/resultados/guardar")
	public String guardarCalificacion(@ModelAttribute ResultadoSaberPro resultado,
	                                  @RequestParam Long estudianteId, // ← Recibe el ID directamente
	                                  RedirectAttributes ra) {
	    
	    try {
	        // 1. Buscar el estudiante REAL de la base de datos
	        Estudiante estudiantePersistido = estudianteRepository.findById(estudianteId)
	                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

	        // 2. Asignar el estudiante REAL (persistido) al resultado
	        resultado.setEstudiante(estudiantePersistido);

	        // 3. Calcular beneficios
	        resultado.calcularBeneficios();

	        // 4. Guardar
	        resultadoSaberProRepository.save(resultado);

	        ra.addFlashAttribute("success", "Calificación guardada correctamente");
	    } catch (Exception e) {
	        ra.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return "redirect:/coordinador/crud-alumnos";
	}

	// Actualizar estudiante
	@PostMapping("/estudiantes/actualizar")
	public String actualizarEstudiante(@ModelAttribute Estudiante estudiante,
	                                   RedirectAttributes ra) {
	    estudianteRepository.save(estudiante);
	    ra.addFlashAttribute("success", "Estudiante actualizado correctamente");
	    return "redirect:/coordinador/crud-alumnos";
	}
	
	@GetMapping("/resultados/calificar/{id}")
	public String mostrarCalificar(@PathVariable Long id, Model model) {
	    Estudiante estudiante = estudianteRepository.findById(id).orElseThrow();
	    
	    ResultadoSaberPro resultado = resultadoSaberProRepository.findByEstudiante(estudiante)
	            .stream().findFirst().orElse(new ResultadoSaberPro());
	    
	    // Si ya existe, carga los datos. Si no, nuevo objeto vacío
	    model.addAttribute("estudiante", estudiante);
	    model.addAttribute("resultado", resultado);
	    return "coordinador/calificar-estudiante";
	}

	@PostMapping("/estudiantes/desactivar/{id}")
	public String desactivarEstudiante(@PathVariable Long id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		try {
			Estudiante estudiante = estudianteRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
			estudiante.setActivo(false);
			estudianteRepository.save(estudiante);
			redirectAttributes.addFlashAttribute("success", "Estudiante desactivado exitosamente");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al desactivar estudiante: " + e.getMessage());
		}

		return "redirect:/coordinador/crud-alumnos";
	}

	// INFORMES
	@GetMapping("/informe-alumnos")
	public String informeAlumnos(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		List<Estudiante> estudiantes = estudianteRepository.findAllWithResultados();
		model.addAttribute("usuario", usuario);
		model.addAttribute("estudiantes", estudiantes);

		return "coordinador/informe-alumnos";
	}

	@GetMapping("/informe-detallado")
	public String informeDetallado(HttpSession session, Model model, @RequestParam(required = false) String programa,
			@RequestParam(required = false) Integer semestre) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		List<ResultadoSaberPro> resultados;
		if (programa != null && !programa.isEmpty()) {
			resultados = resultadoSaberProRepository.findByProgramaEstudiante(programa);
		} else {
			resultados = resultadoSaberProRepository.findAll();
		}

		List<String> programas = estudianteRepository.findByActivoTrue().stream().map(Estudiante::getPrograma)
				.distinct().toList();

		model.addAttribute("usuario", usuario);
		model.addAttribute("resultados", resultados);
		model.addAttribute("programas", programas);
		model.addAttribute("programaSeleccionado", programa);

		return "coordinador/informe-detallado";
	}

	@GetMapping("/informe-beneficios")
	public String informeBeneficios(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		List<ResultadoSaberPro> resultadosConBeneficios = resultadoSaberProRepository.findByExoneradoTrabajoGradoTrue();
		List<ResultadoSaberPro> resultadosConBeca = resultadoSaberProRepository.findResultadosConBeca();

		// Estadísticas de beneficios
		List<Object[]> exoneradosPorPrograma = resultadoSaberProRepository.countExoneradosPorPrograma();

		model.addAttribute("usuario", usuario);
		model.addAttribute("resultadosConBeneficios", resultadosConBeneficios);
		model.addAttribute("resultadosConBeca", resultadosConBeca);
		model.addAttribute("exoneradosPorPrograma", exoneradosPorPrograma);

		return "coordinador/informe-beneficios";
	}

	// GESTIÓN DE RESULTADOS
	@PostMapping("/resultados/registrar")
	public String registrarResultado(@ModelAttribute ResultadoSaberPro resultado, @RequestParam Long estudianteId,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		try {
			Estudiante estudiante = estudianteRepository.findById(estudianteId)
					.orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

			// Verificar si ya existe resultado para esta fecha
			if (resultadoSaberProRepository.existsByEstudianteAndFechaExamen(estudiante, resultado.getFechaExamen())) {
				redirectAttributes.addFlashAttribute("error",
						"Ya existe un resultado para este estudiante en la fecha seleccionada");
				return "redirect:/coordinador/crud-alumnos";
			}

			resultado.setEstudiante(estudiante);
			resultado.calcularBeneficios(); // Calcular beneficios automáticamente
			resultadoSaberProRepository.save(resultado);

			redirectAttributes.addFlashAttribute("success", "Resultado registrado exitosamente");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al registrar resultado: " + e.getMessage());
		}

		return "redirect:/coordinador/crud-alumnos";

	}

	@PostMapping("/estudiantes/importar-excel")
	public String importarExcel(@RequestParam("file") MultipartFile file, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !"COORDINADOR".equals(usuario.getRol())) {
			return "redirect:/auth/login";
		}

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Por favor selecciona un archivo");
			return "redirect:/coordinador/crud-alumnos";
		}

		try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) { // ← Esta línea
																										// es la clave

			Sheet sheet = workbook.getSheetAt(0);
			int filasProcesadas = 0;
			int errores = 0;

			// Iterar manualmente porque Sheet no es Iterable en todas las versiones
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue; // fila vacía

				try {
					String tipoDoc = getCellValue(row.getCell(0));
					String numeroDoc = getCellValue(row.getCell(1));
					String primerApellido = getCellValue(row.getCell(2));
					String segundoApellido = getCellValue(row.getCell(3));
					String primerNombre = getCellValue(row.getCell(4));
					String segundoNombre = getCellValue(row.getCell(5));
					String correo = getCellValue(row.getCell(6));
					String telefono = getCellValue(row.getCell(7));
					String numeroRegistro = getCellValue(row.getCell(8));

					// Validar campos mínimos
					if (numeroDoc == null || numeroDoc.trim().isEmpty() || primerNombre == null
							|| primerNombre.trim().isEmpty()) {
						errores++;
						continue;
					}

					// Crear estudiante si no existe
					if (!usuarioRepository.existsByNumeroDocumento(numeroDoc.trim())) {
						Estudiante estudiante = new Estudiante();
						estudiante.setTipoDocumento(tipoDoc != null ? tipoDoc.trim() : "CC");
						estudiante.setNumeroDocumento(numeroDoc.trim());
						estudiante.setPrimerApellido(primerApellido != null ? primerApellido.trim() : "");
						estudiante.setSegundoApellido(segundoApellido != null ? segundoApellido.trim() : "");
						estudiante.setPrimerNombre(primerNombre.trim());
						estudiante.setSegundoNombre(segundoNombre != null ? segundoNombre.trim() : "");
						estudiante.setCorreoElectronico(correo != null ? correo.trim() : numeroDoc + "@mail.com");
						estudiante.setNumeroTelefonico(telefono);
						estudiante.setNumeroRegistro(numeroRegistro != null ? numeroRegistro.trim() : "");
						estudiante.setSemestre(10);
						estudiante.setPrograma("Ingeniería de Sistemas");
						estudiante.setTipoPrograma("Profesional");
						estudiante.setPassword(estudiante.getNumeroDocumento());
						estudiante.setActivo(true);
						estudiante.setFechaCreacion(LocalDateTime.now());

						estudianteRepository.save(estudiante);
						filasProcesadas++;
					}

					// Procesar resultado si existe puntaje global
					String puntajeStr = getCellValue(row.getCell(9));
					if (puntajeStr != null && !puntajeStr.trim().isEmpty() && Character.isDigit(puntajeStr.charAt(0))) {

						Estudiante estudiante = estudianteRepository.findByNumeroDocumento(numeroDoc.trim()).get();

						ResultadoSaberPro resultado = new ResultadoSaberPro();
						resultado.setEstudiante(estudiante);
						resultado.setPuntajeGlobal(new BigDecimal(puntajeStr.trim()));
						resultado.setNivelGlobal(getCellValue(row.getCell(10)));
						resultado.setComunicacionEscrita(new BigDecimal(getCellValue(row.getCell(11))));
						resultado.setComunicacionEscritaNivel(getCellValue(row.getCell(12)));
						resultado.setRazonamientoCuantitativo(new BigDecimal(getCellValue(row.getCell(13))));
						resultado.setRazonamientoCuantitativoNivel(getCellValue(row.getCell(14)));
						resultado.setLecturaCritica(new BigDecimal(getCellValue(row.getCell(15))));
						resultado.setLecturaCriticaNivel(getCellValue(row.getCell(16)));
						resultado.setCompetenciasCiudadanas(new BigDecimal(getCellValue(row.getCell(17))));
						resultado.setCompetenciasCiudadanasNivel(getCellValue(row.getCell(18)));
						resultado.setIngles(new BigDecimal(getCellValue(row.getCell(19))));
						resultado.setInglesNivel(getCellValue(row.getCell(20)));
						resultado.setNivelIngles(getCellValue(row.getCell(28)));
						resultado.setFechaExamen(LocalDate.now());
						resultado.calcularBeneficios();
						

						resultadoSaberProRepository.save(resultado);
					}

				} catch (Exception e) {
					errores++;
					System.out.println("Error en fila " + (i + 1) + ": " + e.getMessage());
				}
			}

			redirectAttributes.addFlashAttribute("success", "Importación completada: " + filasProcesadas
					+ " estudiantes creados. Errores en " + errores + " filas.");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al procesar el archivo: " + e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/coordinador/crud-alumnos";
	}

	// Método auxiliar para leer celdas de forma segura
	private String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		return switch (cell.getCellType()) {
		case STRING -> cell.getStringCellValue().trim();
		case NUMERIC -> {
			if (DateUtil.isCellDateFormatted(cell)) {
				yield cell.getDateCellValue().toString();
			} else {
				double num = cell.getNumericCellValue();
				if (num == (long) num) {
					yield String.valueOf((long) num);
				} else {
					yield String.valueOf(num);
				}
			}
		}
		case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
		case FORMULA -> cell.getCellFormula();
		default -> "";
		};
	}
	
	@GetMapping("/resultados/detalle/{estudianteId}")
	@ResponseBody
	public ResultadoSaberPro obtenerDetalle(@PathVariable Long estudianteId) {
	    Estudiante estudiante = estudianteRepository.findById(estudianteId).orElseThrow();
	    return resultadoSaberProRepository.findByEstudiante(estudiante)
	            .stream().findFirst().orElse(null);
	}
	
	@GetMapping("/resultados/ver/{estudianteId}")
	public String verDetalle(@PathVariable Long estudianteId, Model model) {
	    Estudiante estudiante = estudianteRepository.findById(estudianteId).orElseThrow();
	    
	    ResultadoSaberPro resultado = resultadoSaberProRepository.findByEstudiante(estudiante)
	            .stream().findFirst().orElse(new ResultadoSaberPro());

	    model.addAttribute("estudiante", estudiante);
	    model.addAttribute("resultado", resultado);
	    return "coordinador/modal-detalle :: modalContent"; // ← fragmento Thymeleaf
	}
	
}