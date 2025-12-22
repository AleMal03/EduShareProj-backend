package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.CoursesListDTO;
import edushare.serveredushare.DTO.UserDTO;
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.services.CourseService;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corsi_seguiti")
@CrossOrigin(origins = "*") // Permette al frontend di chiamare senza blocchi
public class CorsiSeguitiController {

    private final CourseService courseService;

    CorsiSeguitiController(CourseService courseService) {
		this.courseService = courseService;
	}



    // Esempio: http://localhost:7777/miei_corsi?username=simo
    @GetMapping("")
    public ResponseEntity<?> getFollowedCoursesByUsername(@RequestParam String username, HttpSession session) {
        
        UserDTO sessionUser = (UserDTO) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessione scaduta o utente non loggato");
        }

        // Verifichiamo che l'utente stia chiedendo i PROPRI corsi seguiti e non quelli di un altro
        if (!sessionUser.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Errore: Non puoi visualizzare i corsi seguiti di un altro utente!");
        }

        List<Course> corsiGrezzi = courseService.getFollowedCoursesByUsername(username);
        
        return ResponseEntity.ok(CoursesListDTO.map(corsiGrezzi));
    }

}