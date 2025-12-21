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
	private final TeacherProfileRepository teacherRepository;
	private final CourseRepository courseRepository;

	public UserService(UserRepository userRepository, TeacherProfileRepository teacherRepository, CourseRepository courseRepository) {
		this.userRepository = userRepository;
		this.teacherRepository = teacherRepository;
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
				Set.of(User.Role.TEACHER, User.Role.STUDENT), "prof1.png", 230.00);

		addTeacherProfileToUser(newUser, "Sono un professore",
				Set.of("Laurea triennale in professorologia", "Laurea magistrale in professorologia"));

		userRepository.save(newUser);

		/* User 2 (studente) */
		newUser =new User("SimoStr", "ss123", "Simone", "Stridi",
				"simostr@gmail.com", 25, "Italiana", Set.of("Italiano", "Inglese", "Francese"),
				Set.of(User.Role.STUDENT), "simostr.png", 24.75);

		userRepository.save(newUser);

		/* User 3 (studente) */
		newUser = new User("AleMa", "am123", "Alessandro", "Mallardi",
				"alema@gmail.com", 22, "Italiana", Set.of("Italiano", "Inglese", "Spagnolo"),
				Set.of(User.Role.STUDENT), "alema.png", 12.00);

		userRepository.save(newUser);
	}

	/// Verifica che le credenziali inserite siano valide (se esiste un utente registrato con quelle credenziali)
	@Transactional
	public boolean checkCredentials(String username, String password){
		User user = getUserByUsername(username);
		return user != null && user.getPassword().equals(password);
	}

	/// Restituisce l'oggetto User dato lo username
	public User getUserByUsername(String username){
		Optional<User> opUser = userRepository.findById(username);
		return opUser.orElse(null);
	}

	/// Restituisce l'oggetto TeacherProfile dato lo username
	public TeacherProfile getTeacherByUsername(String username){
		Optional<TeacherProfile> opTeacherProfile = teacherRepository.findById(username);
		return opTeacherProfile.orElse(null);
	}

	/// Aggiunge un TeacherProfile all'utente, se non ce l'ha già (fa diventare l'utente un insegnante)
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

	/// Aggiunge il corso dato il suo id alla lista dei corsi seguiti dell'utente dato il suo username
	@Transactional
	public boolean addCourseToFollowed(Long idCorso, String username){
		Optional<Course> opCorso = courseRepository.findById(idCorso);
		User user = getUserByUsername(username);

		if(opCorso.isPresent() && user != null){
			user.addToCorsiSeguiti(opCorso.get());
			return true;
		}

		return false;
	}

	/// Modifica il nome della foto profilo dell'utente dato lo username
	@Transactional
	public boolean changeUserImage(String username, String newFotoProfilo){
		if(username != null && newFotoProfilo != null){
			User user =  getUserByUsername(username);

			if(user != null){
				user.setImmagineProfilo(newFotoProfilo);
				userRepository.save(user);
				return true;
			}
		}
		return false;
	}

	/// Modifica l'email dell'utente dato lo username
	@Transactional
	public boolean changeUserEmail(String username, String newEmail) throws IllegalArgumentException, IllegalStateException{
		if(username != null && newEmail != null){
			// Email checking a titolo esemplificativo
			if (!newEmail.contains("@"))
				throw new IllegalArgumentException("Il formato dell'email non è valido.");
			if(userRepository.existsByEmail(newEmail))
				throw new IllegalStateException("Email già in uso.");

			User user =  getUserByUsername(username);

			if(user != null){
				user.setEmail(newEmail);
				userRepository.save(user);
				return true;
			}
		}
		return false;
	}

	/// Modifica la password dell'utente dato lo username e la vecchia password (per verificare l'utente)
	@Transactional
	public boolean changeUserPassword(String username, String oldPassword, String newPassword){
		if(username != null && oldPassword != null && newPassword != null){
			if(checkCredentials(username, oldPassword)){    // Prima di cambiare la password, verifico che l'utente abbia immesso la vecchia password correttamente
				User user = getUserByUsername(username);

				if(user != null){
					user.setPassword(newPassword);
					userRepository.save(user);
					return true;
				}
			}
		}
		return false;
	}

	/// Modifica le lingue parlate dell'utente dato lo username
	@Transactional
	public boolean changeUserLingueParlate(String username, Set<String> setLingue){
		if(username != null && setLingue != null){
			User user = getUserByUsername(username);

			// Ignora le stringhe vuote
			setLingue.removeIf(String::isBlank);

			if(user != null){
				user.setLingueParlate(setLingue);
				userRepository.save(user);
				return true;
			}
		}
		return false;
	}

	/// Modifica la descrizione (aboutMe) dell'insegnante, dato lo username
	@Transactional
	public boolean changeTeacherDescription(String username, String description){
		if(username != null && description != null){
			TeacherProfile teacher = getTeacherByUsername(username);

			if(teacher != null){    // Verifico sia un insegnante "in regola"
				teacher.setAboutMe(description);
				teacherRepository.save(teacher);
				return true;
			}
		}
		return false;
	}

	/// Modifica i titoli di studio dell'insegnante, dato lo username
	@Transactional
	public boolean changeTeacherTitoliStudio(String username, Set<String> setTitoli){
		if(username != null && setTitoli != null){
			TeacherProfile teacher = getTeacherByUsername(username);

			// Ignora le stringhe vuote
			setTitoli.removeIf(String::isBlank);

			if(teacher != null){
				teacher.setTitoliStudio(setTitoli);
				teacherRepository.save(teacher);
				return true;
			}
		}
		return false;
	}
}
