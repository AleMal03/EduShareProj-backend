package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.CoursesListDTO;
import edushare.serveredushare.DTO.UserDTO; // Assicurati di importare il tuo UserDTO
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.services.CourseService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/miei_corsi")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") 
public class MieiCorsiController {

    private final CourseService courseService;

    MieiCorsiController(CourseService courseService) {
        this.courseService = courseService;
    }


    // Esempio: http://localhost:7777/miei_corsi?username=simo
    @GetMapping("")
    public ResponseEntity<?> getCoursesByUsername(@RequestParam String username, HttpSession session) {
        
        UserDTO sessionUser = (UserDTO) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessione scaduta o utente non loggato");
        }

        // Verifichiamo che l'utente stia chiedendo i PROPRI corsi e non quelli di un altro
        if (!sessionUser.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Errore: Non puoi visualizzare i corsi privati di un altro utente!");
        }

        List<Course> corsiGrezzi = courseService.getCoursesByUsername(username);
        
        return ResponseEntity.ok(CoursesListDTO.map(corsiGrezzi));
    }



    // POST: Aggiungi corso
    @PostMapping("/aggiungi")
    public ResponseEntity<?> aggiungiCorso(HttpSession session, @RequestBody CoursesListDTO.CourseDTO nuovoCorso) {

        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Devi essere loggato per aggiungere un corso");
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
            return ResponseEntity.ok(CoursesListDTO.map(listaAggiornata));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante la creazione del corso: " + e.getMessage());
        }
    }



    // POST: Rimuovi corso
    @PostMapping("/rimuovi")
    public ResponseEntity<?> rimuoviCorso(HttpSession session, @RequestBody Map<String, Object> payload) {

        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Devi essere loggato per rimuovere un corso");
        }

        try {
            Long idCorso = Long.valueOf(payload.get("id").toString());
            
            courseService.rimuoviCorso(idCorso, user.getUsername());
            
            List<Course> listaAggiornata = courseService.getCoursesByUsername(user.getUsername());
            return ResponseEntity.ok(CoursesListDTO.map(listaAggiornata));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore nella rimozione: " + e.getMessage());
        }
    }
}