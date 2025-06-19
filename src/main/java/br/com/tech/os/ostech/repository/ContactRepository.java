package br.com.tech.os.ostech.repository;

import br.com.tech.os.ostech.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, String> {

}
