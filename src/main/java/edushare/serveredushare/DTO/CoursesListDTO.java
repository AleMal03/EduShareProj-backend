package edushare.serveredushare.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.persistence.Course.Difficolta;


public class CoursesListDTO implements Serializable {

    private ArrayList<CourseDTO> courses;

    public CoursesListDTO(){
        courses = new ArrayList<>();
    }

    public List<CourseDTO> getCourses(){
        return courses;
    }

    public boolean addCourse(CourseDTO course){
        return courses.add(course);
    }





    // --- MAPPER STATICO ---
    public static CoursesListDTO map(List<Course> courses){
        if(courses == null)
            return null;

        CoursesListDTO coursesListDTO = new CoursesListDTO();

        for(Course c : courses){
            CourseDTO temp = new CourseDTO(c.getId(), c.getNome(), c.getMateria(), c.getPrezzo(), c.getDifficolta(), c.getIcona(), c.getOwner().getUsername());
            coursesListDTO.addCourse(temp);
        }

        return coursesListDTO;
    }



    // --- CLASSE INNESTATA - singolo corso (STATIC) ---
    public static class CourseDTO implements Serializable {

        private Long id;
        private String nome;
        private String materia;
        private double prezzo;
        private Difficolta difficolta;
        private String icona;
        private String owner;

        public CourseDTO(){}

        public CourseDTO(Long id, String nome, String materia, double prezzo, Difficolta difficolta, String icona, String owner){
            this.id = id;
            this.nome = nome;
            this.materia = materia;
            this.prezzo = prezzo;
            this.difficolta = difficolta;
            this.icona = icona;
            this.owner = owner;
        }

        // --- GETTER (FONDAMENTALI PER IL JSON) ---
        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getMateria() { return materia; }
        public double getPrezzo() { return prezzo; }
        public Difficolta getDifficolta() { return difficolta; }
        public String getIcona() { return icona; }
        public String getOwner() {return owner;}
    }
}