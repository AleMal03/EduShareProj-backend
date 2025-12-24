package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.CorsiData;
import edushare.serveredushare.DTO.CoursesListDTO;
import edushare.serveredushare.DTO.UserDTO;
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/corsi")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CorsiController {
	private CourseService courseService;

	public CorsiController(CourseService courseService) {
		this.courseService = courseService;
	}

	/**
	 * Restituisce tutti i corsi presenti nel DB
	 */
	@GetMapping("")
	public ResponseEntity<CorsiData> allCourses(@RequestParam (required = false) String nomeCorso,
	                                            @RequestParam (required = false) String owner,
	                                            @RequestParam (required = false) String materia,
	                                            @RequestParam (required = false) Course.Difficolta difficolta){
		List<Course> listaCourses = courseService.getFilteredCourses(nomeCorso, owner, materia, difficolta);

		return ResponseEntity.ok(new CorsiData(CoursesListDTO.map(listaCourses), "Corsi filtrati"));
	}

	/**
	 * Restituisce tutti i corsi seguiti dall'utente loggato
	 */
	@GetMapping("seguiti")
	public ResponseEntity<CorsiData> getFollowedCoursesByUsername(@RequestParam String username, HttpSession session) {
		UserDTO sessionUser = (UserDTO) session.getAttribute("user");

		if (sessionUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null, "Sessione scaduta o utente non loggato"));
		}

		List<Course> corsiGrezzi = courseService.getFollowedCoursesByUsername(username);

		return ResponseEntity.ok(new CorsiData(CoursesListDTO.map(corsiGrezzi), "Corsi seguiti"));
	}

	/**
	 * Restituisce tutti i corsi creati dall'utente (insegnante) loggato
	 */
	@GetMapping("miei")
	public ResponseEntity<CorsiData> getCoursesByUsername(HttpSession session) {
		UserDTO sessionUser = (UserDTO) session.getAttribute("user");

		if (sessionUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null,"Sessione scaduta o utente non loggato"));
		}

		List<Course> corsiGrezzi = courseService.getCoursesByUsername(sessionUser.getUsername());

		return ResponseEntity.ok(new CorsiData(CoursesListDTO.map(corsiGrezzi), "Corsi seguiti"));
	}

	// POST: Aggiungi corso
	@PostMapping("/miei/aggiungi")
	public ResponseEntity<CorsiData> aggiungiCorso(HttpSession session, @RequestBody CoursesListDTO.CourseDTO nuovoCorso) {
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null, "Sessione scaduta o utente non loggato"));
		}

		try {
			courseService.creaNuovoCorso(
					nuovoCorso.getNome(),
					nuovoCorso.getMateria(),
					nuovoCorso.getPrezzo(),
					nuovoCorso.getDifficolta(),
					nuovoCorso.getIcona(),
					user.getUsername(), // Uso l'utente della sessione
					null
			);

			// Ritorno la lista aggiornata
			List<Course> listaAggiornata = courseService.getCoursesByUsername(user.getUsername());
			return ResponseEntity.ok(new CorsiData(CoursesListDTO.map(listaAggiornata), "Corso aggiunto correttamente"));

		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new CorsiData(null ,"Errore durante la creazione del corso: " + e.getMessage()));
		}
	}

	// POST: Rimuovi corso
	@PostMapping("/miei/rimuovi")
	public ResponseEntity<CorsiData> rimuoviCorso(HttpSession session, @RequestBody Map<String, Object> payload) {
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null, "Sessione scaduta o utente non loggato"));
		}

		try {
			Long idCorso = Long.valueOf(payload.get("id").toString());

			courseService.rimuoviCorso(idCorso, user.getUsername());

			List<Course> listaAggiornata = courseService.getCoursesByUsername(user.getUsername());
			return ResponseEntity.ok(new CorsiData(CoursesListDTO.map(listaAggiornata), "Corso rimosso correttamente"));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new CorsiData(null ,"Errore nella rimozione: " + e.getMessage()));
		}
	}
}
