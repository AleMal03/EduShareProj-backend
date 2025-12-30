package edushare.serveredushare.services;

import edushare.serveredushare.persistence.*;
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
    private final FollowedCourseRepository followedCourseRepository;

    public UserService(UserRepository userRepository, TeacherProfileRepository teacherRepository, CourseRepository courseRepository, FollowedCourseRepository followedCourseRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.followedCourseRepository = followedCourseRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(1)
    public void init(){
        User newUser;

        /* User 1 (insegnante) */
        newUser = new User("Prof1", "prof123", "Prof", "Essore",
                "prof1@gmail.com", 45, "Italiana", Set.of("Italiano", "Tedesco", "Russo"),
                Set.of(User.Role.TEACHER, User.Role.STUDENT), "Prof1.png", 230.00);

        addTeacherProfileToUser(newUser, "Sono un professore",
                Set.of("Laurea triennale in professorologia", "Laurea magistrale in professorologia"));

        if(!userRepository.existsById(newUser.getUsername())) userRepository.save(newUser);

        /* User 2 (studente) */
        newUser =new User("SimoStr", "ss123", "Simone", "Stridi",
                "simostr@gmail.com", 25, "Italiana", Set.of("Italiano", "Inglese", "Francese"),
                Set.of(User.Role.STUDENT), "SimoStr.png", 24.75);

        if(!userRepository.existsById(newUser.getUsername())) userRepository.save(newUser);

        /* User 3 (studente) */
        newUser = new User("AleMa", "am123", "Alessandro", "Mallardi",
                "alema@gmail.com", 22, "Italiana", Set.of("Italiano", "Inglese", "Spagnolo"),
                Set.of(User.Role.STUDENT), "default.png", 12.00);

        if(!userRepository.existsById(newUser.getUsername())) userRepository.save(newUser);

        /* User 4 (insegnante) */
        newUser = new User("Chi123", "ch123", "Chiara", "Eli",
                "xhiaeli@gmail.com", 18, "Italiana", Set.of("Italiano", "Inglese", "Spagnolo"),
                Set.of(User.Role.STUDENT, User.Role.TEACHER), "default.png", 15.00);

        addTeacherProfileToUser(newUser, "Prof Chiara",
                Set.of("Laurea triennale in niente"));

        if(!userRepository.existsById(newUser.getUsername())) userRepository.save(newUser);
    }

    @Transactional
    public boolean checkCredentials(String username, String password){
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public User getUserByUsername(String username){
        Optional<User> opUser = userRepository.findById(username);
        return opUser.orElse(null);
    }

    public TeacherProfile getTeacherByUsername(String username){
        Optional<TeacherProfile> opTeacherProfile = teacherRepository.findById(username);
        return opTeacherProfile.orElse(null);
    }

    @Transactional
    public void addTeacherProfileToUser(User user, String aboutMe, Set<String> titoli) throws RuntimeException{
        if(user != null){
            if(user.getTeacherProfile() == null){
                try{
                    user.setTeacherProfile(new TeacherProfile(user, aboutMe, titoli));
                    // Nota: qui non serve salvare esplicitamente teacherRepository perché c'è CascadeType.ALL su User
                } catch (Exception e){throw new RuntimeException("Errore creazione TeacherProfile");}
            }
            else {
                throw new RuntimeException("Tentativo di aggiunta TeacherProfile a un utente che ha già un TeacherProfile");
            }
        }
    }


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

    @Transactional
    public boolean changeUserEmail(String username, String newEmail) throws IllegalArgumentException, IllegalStateException{
        if(username != null && newEmail != null){
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

    @Transactional
    public boolean changeUserPassword(String username, String oldPassword, String newPassword) throws IllegalArgumentException{
        if(username != null && oldPassword != null && newPassword != null){
            User user = getUserByUsername(username);

            if(user != null){
                if(user.getPassword().equals(oldPassword)) {
                    user.setPassword(newPassword);
                    userRepository.save(user);
                    return true;
                }
                else{throw new IllegalArgumentException("La vecchia password è errata");}
            }
        }
        return false;
    }

    @Transactional
    public boolean changeUserLingueParlate(String username, Set<String> setLingue){
        if(username != null && setLingue != null){
            User user = getUserByUsername(username);
            setLingue.removeIf(String::isBlank);

            if(user != null && !setLingue.isEmpty()){
                user.setLingueParlate(setLingue);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean changeTeacherDescription(String username, String description){
        if(username != null && description != null){
            TeacherProfile teacher = getTeacherByUsername(username);

            if(teacher != null){
                teacher.setAboutMe(description);
                teacherRepository.save(teacher);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean changeTeacherTitoliStudio(String username, Set<String> setTitoli){
        if(username != null && setTitoli != null){
            TeacherProfile teacher = getTeacherByUsername(username);
            setTitoli.removeIf(String::isBlank);

            if(teacher != null && !setTitoli.isEmpty()){
                teacher.setTitoliStudio(setTitoli);
                teacherRepository.save(teacher);
                return true;
            }
        }
        return false;
    }
}