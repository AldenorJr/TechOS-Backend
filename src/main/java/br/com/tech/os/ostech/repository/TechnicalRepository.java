package br.com.tech.os.ostech.repository;

import br.com.tech.os.ostech.model.Technical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicalRepository extends JpaRepository<Technical, String> {

    Optional<Technical> findByName(String name);

    boolean existsByName(String name);

}
