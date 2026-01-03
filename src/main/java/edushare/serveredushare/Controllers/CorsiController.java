package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.CorsiData;
import edushare.serveredushare.DTO.CourseDTO;
import edushare.serveredushare.DTO.UserDTO;
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.persistence.User;
import edushare.serveredushare.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
	// Esempio "http://localhost:5173/corsi?nomeCorso=TWeb"
	@GetMapping("")
	public ResponseEntity<CorsiData> allCourses(HttpSession session,
	                                            @RequestParam (required = false) String nomeCorso,
	                                            @RequestParam (required = false) String teacher,
	                                            @RequestParam (required = false) String materia,
	                                            @RequestParam (required = false) Course.Difficolta difficolta,
	                                            @RequestParam (required = false) Double prezzo,
	                                            @RequestParam (required = false) Short rating){
		List<Course> listaCourses = courseService.getFilteredCourses(nomeCorso, teacher, materia, difficolta, prezzo, rating);

		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user != null) {
            listaCourses.removeIf(corso ->
                    // 1. Elimino se l'utente è il proprietario (Insegnante)
                    corso.getOwner().getUsername().equals(user.getUsername()) 
                    
                    || // OR
                    
                    // 2. Elimino se l'utente lo sta già seguendo
                    // NOTA: Qui la logica cambia perché navighi dentro 'getIscritti()' -> 'getUser()'
                    corso.getIscritti().stream()
                            .anyMatch(iscrizione -> iscrizione.getUser().getUsername().equals(user.getUsername()))
            );
        }

        return ResponseEntity.ok(new CorsiData(listaCourses.stream()
                .map(CourseDTO::mapCourseToCourseDTO).toList(), "Corsi filtrati"));
	}

	/**
	 * Restituisce tutti i corsi seguiti dall'utente loggato
	 */
	@GetMapping("/seguiti")
	public ResponseEntity<CorsiData> getFollowedCoursesByUsername(HttpSession session,
	                                                              @RequestParam (required = false) String nomeCorso,
	                                                              @RequestParam (required = false) String teacher,
	                                                              @RequestParam (required = false) String materia,
	                                                              @RequestParam (required = false) Course.Difficolta difficolta) {
		
																	UserDTO user = (UserDTO) session.getAttribute("user");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null, "Sessione scaduta o utente non loggato"));
		}

		List<Course> corsiGrezzi = courseService.getFollowedCoursesByUsername(user.getUsername(), nomeCorso, teacher, materia, difficolta);

		return ResponseEntity.ok(new CorsiData(corsiGrezzi.stream()
				.map(CourseDTO::mapCourseToCourseDTO).toList(), "Corsi seguiti"));
	}

	/**
	 * Restituisce tutti i corsi creati dall'utente (insegnante) loggato
	 */
	@GetMapping("/miei")
	public ResponseEntity<CorsiData> getCoursesByUsername(HttpSession session,
	                                                      @RequestParam (required = false) String nomeCorso,
	                                                      @RequestParam (required = false) String materia,
	                                                      @RequestParam (required = false) Course.Difficolta difficolta) {
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null,"Sessione scaduta o utente non loggato"));
		}

		if(!user.getRuoli().contains(User.Role.TEACHER)){   // Se non se insegnante, non puoi creare corsi
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new CorsiData(null, "L'utente loggato non è un insegnante"));
		}

		List<Course> corsiGrezzi = courseService.getFilteredCoursesByUsername(nomeCorso, user.getUsername(), materia, difficolta);

		return ResponseEntity.ok(new CorsiData(corsiGrezzi.stream()
				.map(CourseDTO::mapCourseToCourseDTO).toList(), "Corsi creati"));
	}

	/**
	 * POST: Aggiungi corso (se sei insegnante)
	 * */
	@PostMapping("/miei/aggiungi")
	public ResponseEntity<CorsiData> aggiungiCorso(HttpSession session, @RequestBody CourseDTO nuovoCorso) {
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CorsiData(null, "Sessione scaduta o utente non loggato"));
		}

		if(!user.getRuoli().contains(User.Role.TEACHER)){   // Se non se insegnante, non puoi creare corsi
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new CorsiData(null, "L'utente loggato non è un insegnante"));
		}

		try {
			courseService.creaNuovoCorso(
					nuovoCorso.getNome().trim().isEmpty() ? null : nuovoCorso.getNome().trim(),
					nuovoCorso.getMateria().trim().isEmpty() ? null : nuovoCorso.getMateria().trim(),
					nuovoCorso.getPrezzo(),
					nuovoCorso.getDifficolta(),
					nuovoCorso.getIcona(),
					user.getUsername(), // Uso l'utente della sessione
					null
			);

			// Ritorno la lista aggiornata
			List<Course> listaAggiornata = courseService.getCoursesByUsername(user.getUsername());
			return ResponseEntity.ok(new CorsiData(listaAggiornata.stream()
					.map(CourseDTO::mapCourseToCourseDTO).toList(), "Corso aggiunto correttamente"));

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

		if(!user.getRuoli().contains(User.Role.TEACHER)){   // Se non se insegnante, non puoi creare corsi
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new CorsiData(null, "L'utente loggato non è un insegnante"));
		}

		try {
			Long idCorso = Long.valueOf(payload.get("id").toString());

			courseService.rimuoviCorso(idCorso, user.getUsername());

			List<Course> listaAggiornata = courseService.getCoursesByUsername(user.getUsername());
			return ResponseEntity.ok(new CorsiData(listaAggiornata.stream()
					.map(CourseDTO::mapCourseToCourseDTO).toList(), "Corso rimosso correttamente"));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new CorsiData(null ,"Errore nella rimozione: " + e.getMessage()));
		}
	}

	/**
	 * Ritorna tutte le materie presenti nel DB
	 */
	@GetMapping("/materie")
	public ResponseEntity<Set<String>> materie(){
		return ResponseEntity.ok(courseService.getMaterie());
	}

	/**
	 * Ritorna tutte le difficolta possibili
	 */
	@GetMapping("/difficolta")
	public ResponseEntity<Course.Difficolta[]> difficolta(){return ResponseEntity.ok(Course.Difficolta.values());}

	/**
	 * Ritorna il costo massimo dei corsi
	 */
	@GetMapping("/maxCosto")
	public ResponseEntity<Integer> maxCosto(){return ResponseEntity.ok((int) Math.ceil(courseService.getMaxCosto()));}



	/**
     * POST: Utente decide di seguire un corso
     */
    @PostMapping("/iscrizione")
    public ResponseEntity<CorsiData> seguiCorso(HttpSession session, @RequestBody Map<String, Long> payload) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CorsiData(null, "Utente non loggato"));
        }

        try {
            Long corsoId = payload.get("id");
            
            courseService.iscriviStudente(user.getUsername(), corsoId);

            List<Course> corsiSeguiti = courseService.getFollowedCoursesByUsername(user.getUsername(), null, null, null, null);
            
            return ResponseEntity.ok(new CorsiData(corsiSeguiti.stream()
                    .map(CourseDTO::mapCourseToCourseDTO).toList(), "Iscrizione avvenuta con successo"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new CorsiData(null, "Errore durante l'iscrizione: " + e.getMessage()));
        }
    }

    /**
     * POST: Utente smette di seguire un corso
     */
    @PostMapping("/seguiti/disiscrizione")
    public ResponseEntity<CorsiData> unfollowCorso(HttpSession session, @RequestBody Map<String, Long> payload) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CorsiData(null, "Utente non loggato"));
        }

        try {
            Long corsoId = payload.get("id");

            courseService.rimuoviIscrizione(user.getUsername(), corsoId);

            List<Course> corsiSeguiti = courseService.getFollowedCoursesByUsername(
                    user.getUsername(), null, null, null, null);

            return ResponseEntity.ok(new CorsiData(corsiSeguiti.stream()
                    .map(CourseDTO::mapCourseToCourseDTO).toList(), "Disiscrizione avvenuta"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CorsiData(null, "Errore: " + e.getMessage()));
        }
    }

}