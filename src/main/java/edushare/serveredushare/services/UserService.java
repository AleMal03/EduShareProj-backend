package edushare.serveredushare.services;

import edushare.serveredushare.persistence.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/// Inizializzazione su DB degli User di comodo
@Service
public class UserService {
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;

	public UserService(UserRepository userRepository, CourseRepository courseRepository) {
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}

	@EventListener(ApplicationReadyEvent.class) // Al posto di @PostConstruction per aprire una sessione che permetta di effettuare transazioni
	@Transactional
	@Order(1)
	public void init(){
		User newUser;

		/* User 1 (insegnante) */
		newUser = new User("Prof1", "prof123", "Prof", "Essore",
				"prof1@gmail.com", 45, "Italiana", Set.of("Italiano", "Tedesco", "Russo"),
				Set.of(User.Role.TEACHER, User.Role.STUDENT), "prof1", 230.00);

		addTeacherProfileToUser(newUser, "Sono un professore",
				Set.of("Laurea triennale in professorologia", "Laurea magistrale in professorologia"));

		userRepository.save(newUser);

		/* User 2 (studente) */
		newUser =new User("SimoStr", "ss123", "Simone", "Stridi",
				"simostr@gmail.com", 25, "Italiana", Set.of("Italiano", "Inglese", "Francese"),
				Set.of(User.Role.STUDENT), "simostr", 24.75);

		userRepository.save(newUser);

		/* User 3 (studente) */
		newUser = new User("AleMa", "am123", "Alessandro", "Mallardi",
				"alema@gmail.com", 22, "Italiana", Set.of("Italiano", "Inglese", "Spagnolo"),
				Set.of(User.Role.STUDENT), "alema", 12.00);

		userRepository.save(newUser);
	}

	public boolean checkCredentials(String username, String password){
		User user = userRepository.findByUsername(username);
		return user != null && user.getPassword().equals(password);
	}

	public User getUserByUsername(String username){
		return userRepository.findByUsername(username);
	}

	@Transactional
	public void addTeacherProfileToUser(User user, String aboutMe, Set<String> titoli) throws RuntimeException{
		if(user != null){
			if(user.getTeacherProfile() == null){
				try{
					user.setTeacherProfile(new TeacherProfile(user, aboutMe, titoli));
				} catch (Exception e){throw new RuntimeException("Errore creazione TeacherProfile");}
			}
			else {
				throw new RuntimeException("Tentativo di aggiunta TeacherProfile a un utente che ha già un TeacherProfile");
			}
		}
	}

	@Transactional
	public void addCourseToFollowed(Long idCorso, String username){
		Optional<Course> opCorso = courseRepository.findById(idCorso);
		User user = userRepository.findByUsername(username);

		if(opCorso.isPresent() && user != null){
			user.addToCorsiSeguiti(opCorso.get());
		}
	}
}
