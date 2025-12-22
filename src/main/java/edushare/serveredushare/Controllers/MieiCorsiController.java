package edushare.serveredushare.Controllers;

import edushare.serveredushare.DTO.CoursesListDTO;
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.services.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/miei_corsi")
@CrossOrigin(origins = "*") // Permette al frontend di chiamare senza blocchi
public class MieiCorsiController {

    @Autowired
    private CourseService courseService;

    // Esempio: GET http://localhost:7777/miei-corsi?username=simo
    @GetMapping("")
    public CoursesListDTO getCoursesByUsername(@RequestParam String username) {
        List<Course> corsiGrezzi = courseService.getCoursesByUsername(username);
        
        return CoursesListDTO.map(corsiGrezzi);
    }


    // Esempio: POST http://localhost:8081/miei_corsi/aggiungi
    @PostMapping("/aggiungi")
    public CoursesListDTO aggiungiCorso(@RequestBody CoursesListDTO.CourseDTO nuovoCorso) {
        
        courseService.creaNuovoCorso(
            nuovoCorso.getNome(),
            nuovoCorso.getMateria(),
            nuovoCorso.getPrezzo(),
            nuovoCorso.getDifficolta(),
            nuovoCorso.getIcona(),
            nuovoCorso.getOwner(), 
            null                      
        );

        List<Course> listaAggiornata = courseService.getCoursesByUsername(nuovoCorso.getOwner());
        
        return CoursesListDTO.map(listaAggiornata);
    }


    // Esempio: POST http://localhost:8081/miei_corsi/rimuovi
    @PostMapping("/rimuovi")
    // Mappa il body sottoforma di chiave-valore, chiave=string e valore=object
    public CoursesListDTO rimuoviCorso(@RequestBody Map<String, Object> payload) {
        
        Long idCorso = Long.valueOf(payload.get("id").toString());
        String owner = (String) payload.get("ownerId");

        courseService.rimuoviCorso(idCorso, owner);
        
        return CoursesListDTO.map(courseService.getCoursesByUsername(owner));
    }



    @GetMapping("/prova")
    public String prova() {
        return "Stronzo";
    }
    


}