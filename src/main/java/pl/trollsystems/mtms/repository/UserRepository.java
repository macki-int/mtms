package pl.trollsystems.mtms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.trollsystems.mtms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginName(String loginName);
}
