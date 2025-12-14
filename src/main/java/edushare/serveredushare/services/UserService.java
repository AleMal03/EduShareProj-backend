package edushare.serveredushare.services;

import edushare.serveredushare.persistence.User;
import edushare.serveredushare.persistence.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

/// Inizializzazione su DB degli User di comodo
@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostConstruct
	public void init(){
		userRepository.save(new User("Prof1", "prof123", "Prof", "Essore",
				"prof1@gmail.com", 45, "Italiana", Set.of("Italiano", "Tedesco", "Russo"), User.Role.TEACHER));
		userRepository.save(new User("SimoStr", "ss123", "Simone", "Stridi",
				"simostr@gmail.com", 25, "Italiana", Set.of("Italiano", "Inglese", "Francese"), User.Role.STUDENT));
		userRepository.save(new User("AleMa", "am123", "Alessandro", "Mallardi",
				"alema@gmail.com", 22, "Italiana", Set.of("Italiano", "Inglese", "Spagnolo"), User.Role.STUDENT));
	}

	public boolean checkCredentials(String username, String password){
		User user = userRepository.findByUsername(username);
		return user != null && user.getPassword().equals(password);
	}
}
