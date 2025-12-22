package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.CoursesListDTO;
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corsi_seguiti")
@CrossOrigin(origins = "*") // Permette al frontend di chiamare senza blocchi
public class CorsiSeguitiController {
    
    @Autowired
    private CourseService courseService;

    // Esempio: GET http://localhost:7777/corsi_seguiti?username=simo
    @GetMapping("")
    public CoursesListDTO getFollowedCoursesByUsername(@RequestParam String username) {
        List<Course> corsiGrezzi = courseService.getFollowedCoursesByUsername(username);
        
        return CoursesListDTO.map(corsiGrezzi);
    }


}