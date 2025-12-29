package edushare.serveredushare.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowedCourseRepository extends JpaRepository<FollowedCourse, Long> {
   
    // 1. TROVA TUTTI I CORSI SEGUITI DA UN UTENTE
    List<FollowedCourse> findByUser_Username(String username);

    // 2. TROVA TUTTI GLI UTENTI ISCRITTI A UN CORSO
    List<FollowedCourse> findByCorso_Id(Long corsoId);

    // 3. TROVA UNA SPECIFICA ISCRIZIONE
    Optional<FollowedCourse> findByUser_UsernameAndCorso_Id(String username, Long corsoId);

    // 4. CONTROLLA SE ESISTE GIÀ 
    boolean existsByUser_UsernameAndCorso_Id(String username, Long corsoId);

    // 5. CANCELLA UN'ISCRIZIONE
    void deleteByUser_UsernameAndCorso_Id(String username, Long corsoId);
    
}



