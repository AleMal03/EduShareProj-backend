package edushare.serveredushare.services;

import edushare.serveredushare.persistence.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

			creaNuovoCorso("Sistemi Operativi", "Informatica", 10, Course.Difficolta.MEDIO,
					"SO.png", "Prof1", null);

			creaNuovoCorso("DataBase", "Informatica", 0, Course.Difficolta.FACILE,
					"DB.png", "Prof1", null);
			
			creaNuovoCorso("Sistemi Operativi", "Informatica", 15, Course.Difficolta.DIFFICILE,
					"SO.png", "Chi123", null);

			creaNuovoCorso("Prog3", "Informatica", 0, Course.Difficolta.FACILE,
					"default.png", "Chi123", null);



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



	public List<Course> getCoursesByUsername(String username) {
		return courseRepository.findByOwner_Username(username);
	}

	public List<Course> getFollowedCoursesByUsername(String username) {
		return courseRepository.findByStudentiIscritti_Username(username);
	}

	public List<Course> getAllCourses(){return courseRepository.findAll();}

	public List<Course> getFilteredCourses(String nomeCorso, String owner, String materia, Course.Difficolta difficolta){
		// 0
		if(nomeCorso == null && owner == null && materia == null && difficolta == null)
			return getAllCourses();
		// 1
		else if(owner == null && materia == null && difficolta == null)
			return courseRepository.findByNomeIgnoreCase(nomeCorso);
		else if(nomeCorso == null && materia == null && difficolta == null)
			return courseRepository.findByOwner_Username(owner);
		else if(nomeCorso == null && owner == null && difficolta == null)
			return courseRepository.findByMateriaIgnoreCase(materia);
		else if(nomeCorso == null && owner == null && materia == null)
			return courseRepository.findByDifficolta(difficolta);
		// 2
		else if(nomeCorso == null && owner == null)
			return courseRepository.findByMateriaIgnoreCaseAndDifficolta(materia, difficolta);
		else if(nomeCorso == null && materia == null)
			return courseRepository.findByOwner_UsernameIgnoreCaseAndDifficolta(owner, difficolta);
		else if(nomeCorso == null && difficolta == null)
			return courseRepository.findByOwner_UsernameAndMateriaAllIgnoreCase(owner, materia);
		else if(owner == null && materia == null)
			return courseRepository.findByNomeIgnoreCaseAndDifficolta(nomeCorso, difficolta);
		else if(owner == null && difficolta == null)
			return courseRepository.findByNomeAndMateriaAllIgnoreCase(nomeCorso, materia);
		else if(materia == null && difficolta == null)
			return courseRepository.findByNomeAndOwner_UsernameAllIgnoreCase(nomeCorso, owner);
		// 3
		else if(nomeCorso == null)
			return courseRepository.findByOwner_UsernameAndMateriaAllIgnoreCaseAndDifficolta(owner, materia, difficolta);
		else if(owner == null)
			return courseRepository.findByNomeAndMateriaAllIgnoreCaseAndDifficolta(nomeCorso, materia, difficolta);
		else if(materia == null)
			return courseRepository.findByNomeAndOwner_UsernameAllIgnoreCaseAndDifficolta(nomeCorso, owner, difficolta);
		else if(difficolta == null)
			return courseRepository.findByNomeAndOwner_UsernameAndMateriaAllIgnoreCase(nomeCorso, owner, materia);

		//4
		else
			return courseRepository.findByNomeAndOwner_UsernameAndMateriaAllIgnoreCaseAndDifficolta(nomeCorso, owner, materia, difficolta);
	}

}
