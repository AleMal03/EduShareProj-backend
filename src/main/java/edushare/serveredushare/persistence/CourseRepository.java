package edushare.serveredushare.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Restituisce i corsi di uno user
    List<Course> findByOwner_Username(String username);

    // Restituisce i corsi seguiti da uno user 
    List<Course> findByStudentiIscritti_Username(String username);

	List<Course> findByMateriaIgnoreCase(String materia);
	List<Course> findByDifficolta(Course.Difficolta difficolta);
	List<Course> findByNomeIgnoreCase(String nomeCorso);

	// 2 filtri
	List<Course> findByNomeAndOwner_UsernameAllIgnoreCase(String nomeCorso, String username);
	List<Course> findByNomeIgnoreCaseAndDifficolta (String nomeCorso, Course.Difficolta difficolta);
	List<Course> findByNomeAndMateriaAllIgnoreCase(String nomeCorso, String materia);
	List<Course> findByOwner_UsernameIgnoreCaseAndDifficolta(String username, Course.Difficolta difficolta);
	List<Course> findByOwner_UsernameAndMateriaAllIgnoreCase(String username, String materia);
	List<Course> findByMateriaIgnoreCaseAndDifficolta(String materia, Course.Difficolta difficolta);

	// 3 filtri
	List<Course> findByNomeAndOwner_UsernameAndMateriaAllIgnoreCase(String nome, String username, String materia);
	List<Course> findByNomeAndOwner_UsernameAllIgnoreCaseAndDifficolta(String nome, String username, Course.Difficolta difficolta);
	List<Course> findByNomeAndMateriaAllIgnoreCaseAndDifficolta(String nome, String materia, Course.Difficolta difficolta);
	List<Course> findByOwner_UsernameAndMateriaAllIgnoreCaseAndDifficolta(String username, String materia, Course.Difficolta difficolta);

	// 4 filtri
	List<Course> findByNomeAndOwner_UsernameAndMateriaAllIgnoreCaseAndDifficolta(String nomeCorso, String username, String materia, Course.Difficolta difficolta);
}
