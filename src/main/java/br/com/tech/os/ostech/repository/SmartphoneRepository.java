package br.com.tech.os.ostech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.tech.os.ostech.model.Smartphone;

@Repository
public interface SmartphoneRepository extends JpaRepository<Smartphone, String> {
}
