package edushare.serveredushare.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    // Restituisce i corsi di uno user
    List<Course> findByOwner_Username(String username);

    @Query("SELECT c FROM Course c WHERE " +
            "(:nome IS NULL OR LOWER(c.nome) LIKE :nome) AND " +
            "(:owner IS NULL OR LOWER(c.owner.username) LIKE :owner) AND " +
            "(:materia IS NULL OR c.materia = :materia) AND " +
            "(:difficolta IS NULL OR c.difficolta = :difficolta) AND " + 
            "(:prezzo IS NULL OR c.prezzo <= :prezzo) AND " +
            "(:rating IS NULL OR c.mediaRecensioni >= :rating OR c.mediaRecensioni = 0) AND " +
            "(:studentUsername IS NULL OR :studentUsername IN (SELECT fc.user.username FROM c.iscritti fc))")
            
    List<Course> searchCourses(
            @Param("nome") String nome,
            @Param("owner") String owner,
            @Param("materia") String materia,
            @Param("difficolta") Course.Difficolta difficolta,
            @Param("prezzo") Double prezzo,
            @Param("rating") Short rating,
            @Param("studentUsername") String studentUsername
    );
}