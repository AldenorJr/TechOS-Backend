package br.com.tech.os.ostech.repository;

import br.com.tech.os.ostech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String name);

    boolean existsByEmail(String email);

}
