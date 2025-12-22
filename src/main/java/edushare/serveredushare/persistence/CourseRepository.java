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
    
}
