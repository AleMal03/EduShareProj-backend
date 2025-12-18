package edushare.serveredushare.services;

import edushare.serveredushare.persistence.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@DependsOn("userService")   // Indica che questo servizio necessita che userService sia già stato inizializzato
public class CourseService {
	private final CourseRepository courseRepository;
	private final TeacherProfileRepository tpRepository;

	public CourseService(CourseRepository courseRepository, TeacherProfileRepository tpRepository) {
		this.courseRepository = courseRepository;
		this.tpRepository = tpRepository;
	}

	@EventListener(ApplicationReadyEvent.class) // Al posto di @PostConstruction per aprire una sessione che permetta di recuperare il professore lazy dal repository
	@Transactional
	@Order(2)
	public void init() {
		try {
			creaNuovoCorso("Tecnologie Web", "Informatica", 0, Course.Difficolta.MEDIO,
					"tweb.png", "Prof1", null);
			creaNuovoCorso("Programmazione 1", "Informatica", 0, Course.Difficolta.MEDIO,
					"prog1.png", "Prof1", null);
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

		if(opOwner.isPresent()){
			owner = opOwner.get();
		}
		else{
			throw new IllegalArgumentException("Teacher " + ownerId + " not found in creaNuovoCorso");
		}

		Course newCourse = new Course(nome, materia, prezzo, difficolta, icona, owner, risorse);

		courseRepository.save(newCourse);
		owner.addCourse(newCourse);
	}
}
