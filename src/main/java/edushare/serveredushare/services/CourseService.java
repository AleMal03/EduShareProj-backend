package edushare.serveredushare.services;

import com.sun.jdi.DoubleValue;
import edushare.serveredushare.persistence.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@DependsOn("userService")   // Indica che questo servizio necessita che userService sia già stato inizializzato
public class CourseService {

    private final UserRepository userRepository;
	private final CourseRepository courseRepository;
	private final TeacherProfileRepository tpRepository;

	public CourseService(CourseRepository courseRepository, TeacherProfileRepository tpRepository, UserRepository userRepository) {
		this.courseRepository = courseRepository;
		this.tpRepository = tpRepository;
		this.userRepository = userRepository;
	}

	@EventListener(ApplicationReadyEvent.class) // Al posto di @PostConstruction per aprire una sessione che permetta di recuperare il professore lazy dal repository
	@Transactional
	@Order(2)
	public void init() {
		try {
			creaNuovoCorso("Tecnologie Web", "Informatica", 0, Course.Difficolta.FACILE,
					"TW.png", "Prof1", null);

			creaNuovoCorso("Sistemi Operativi", "Informatica", 10, Course.Difficolta.MEDIA,
					"SO.png", "Prof1", null);

			creaNuovoCorso("DataBase", "Informatica", 0, Course.Difficolta.FACILE,
					"DB.png", "Prof1", null);
			
			creaNuovoCorso("Sistemi Operativi", "Informatica", 15, Course.Difficolta.DIFFICILE,
					"SO.png", "Chi123", null);

			creaNuovoCorso("Prog3", "Informatica", 0, Course.Difficolta.FACILE,
					"default.png", "Chi123", null);

			creaNuovoCorso("Analisi I", "Matematica", 35, Course.Difficolta.MEDIA,
					"default.png", "Chi123", null);

			creaNuovoCorso("Matematica discreta", "Matematica", 20, Course.Difficolta.FACILE,
					"default.png", "Prof1", null);



			// ---------------------------------------------------
			// ISCRIZIONE DELLO STUDENTE
			// ---------------------------------------------------

			Course course;
			User studente;
			
			/* 1° Corso Seguito */
			studente = userRepository.findByUsername("SimoStr");
			course = courseRepository.findById(1L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);


			/* 2° Corso Seguito */
			studente = userRepository.findByUsername("SimoStr");
			course = courseRepository.findById(4L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);


			/* 3° Corso Seguito */
			studente = userRepository.findByUsername("SimoStr");
			course = courseRepository.findById(5L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);


			/* 4° Corso Seguito */
			studente = userRepository.findByUsername("Chi123");
			course = courseRepository.findById(1L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);


			/* 5° Corso Seguito */
			studente = userRepository.findByUsername("Chi123");
			course = courseRepository.findById(2L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);


			/* 6° Corso Seguito */
			studente = userRepository.findByUsername("Chi123");
			course = courseRepository.findById(3L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);

			/* 7° Corso Seguito */
			studente = userRepository.findByUsername("AleMa");
			course = courseRepository.findById(1L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);

			/* 7° Corso Seguito */
			studente = userRepository.findByUsername("AleMa");
			course = courseRepository.findById(3L).orElseThrow(() -> new RuntimeException("Course not found"));
			// Aggiungiamo il corso alla lista dello studente
			studente.addToCorsiSeguiti(course);
			userRepository.save(studente);


		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Crea un nuovo corso e lo aggiunge al repository dei corsi e alla lista dei corsi dell'insegnante proprietario
	 */
	@Transactional
	public void creaNuovoCorso(String nome, String materia, double prezzo, Course.Difficolta difficolta, String icona,
	                           String ownerId, List<File> risorse) throws IllegalArgumentException{
		Optional<TeacherProfile> opOwner = tpRepository.findById(ownerId);
		TeacherProfile owner;

		if(opOwner.isPresent())
			owner = opOwner.get();
		else
			throw new IllegalArgumentException("Teacher " + ownerId + " not found in creaNuovoCorso");
		
		Course newCourse = new Course(nome, materia, prezzo, difficolta, icona, owner, risorse);

		courseRepository.save(newCourse);
		owner.addCourse(newCourse);
	}

	/**
	 * Rimuove il corso con l'id passato
	 */
    @Transactional
    public void rimuoviCorso(Long idCorso, String ownerId) {
        Course corso = courseRepository.findById(idCorso)
                .orElseThrow(() -> new IllegalArgumentException("Corso non trovato con ID: " + idCorso));

        // 1. CONTROLLO SICUREZZA
        String proprietarioReale = corso.getOwner().getUsername();
        if (!proprietarioReale.equals(ownerId)) {
            throw new IllegalStateException("OPERAZIONE NEGATA");
        }

        // 2. Disiscrivo tutti gli user che seguono il corso
        List<User> studentiIscritti = new ArrayList<>(corso.getStudentiIscritti());

        for (User studente : studentiIscritti) {
            // Rimuovi il corso dalla lista dello studente (lato attivo)
            studente.getCorsiSeguiti().remove(corso);
            
            userRepository.save(studente); 
        }
        
        // Puliamo anche la lista lato corso per coerenza (anche se è il lato passivo)
        corso.getStudentiIscritti().clear();

        // 3. Scollego il corso dal professore
        TeacherProfile teacher = corso.getOwner();
        if(teacher != null){
            teacher.getMieiCorsi().remove(corso);
        }

        // 4. Cancellazione reale
        courseRepository.delete(corso);
    }

	public List<Course> getFilteredCoursesByUsername(String nomeCorso, String owner,String materia, Course.Difficolta difficolta) {
		nomeCorso = cleanParamString(nomeCorso);

		return courseRepository.searchCourses(nomeCorso, owner.toLowerCase(), materia, difficolta, null, null, null);
	}

	public List<Course> getCoursesByUsername(String username) {
		return courseRepository.findByOwner_Username(username);
	}

	public List<Course> getFollowedCoursesByUsername(String studentUsername, String nomeCorso, String owner,String materia,
	                                                 Course.Difficolta difficolta) {
		nomeCorso = cleanParamString(nomeCorso);
		owner = cleanParamString(owner);

		return courseRepository.searchCourses(nomeCorso, owner, materia, difficolta, null, null, studentUsername);

	}

	public List<Course> getAllCourses(){return courseRepository.findAll();}

	public List<Course> getFilteredCourses(String nomeCorso, String owner, String materia, Course.Difficolta difficolta,
	                                       Double prezzo, Short rating){

		nomeCorso = cleanParamString(nomeCorso);
		owner = cleanParamString(owner);

		return courseRepository.searchCourses(nomeCorso, owner, materia, difficolta, prezzo, rating, null);
	}

	/**
	 * Pulisce la stringa passata come parametro dalla GET/POST per la query
	 */
	private String cleanParamString(String s){
		// Se la stringa è presente, la rendiamo minuscola e aggiungiamo % per il pattern matching nella query
		if (s != null && !s.isBlank()) {
			s = "%" + s.toLowerCase() + "%";
		} else {
			s = null;   // Assicura che stringhe vuote diventino null
		}

		return s;
	}

	public Set<String> getMaterie(){
		List<Course> corsi = getAllCourses();
		Set<String> materie = new HashSet<>();

		for(Course c : corsi){
			materie.add(c.getMateria());
		}

		return materie;
	}

	public Double getMaxCosto(){
		List<Course> corsi = courseRepository.findAll();

		double maxCosto = 0;
		for(Course c : corsi){
			double costo = c.getPrezzo();
			if(costo > maxCosto){
				maxCosto = costo;
			}
		}

		return maxCosto;
	}
}
