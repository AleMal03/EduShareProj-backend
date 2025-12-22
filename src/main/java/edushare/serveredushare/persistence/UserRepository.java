package edushare.serveredushare.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/// Gestione repository Users
@Repository
public interface UserRepository extends JpaRepository<User, String> {
	public User findByUsername(String username);

}
