package br.com.tech.os.ostech.repository;

import br.com.tech.os.ostech.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {

    int countByCreatedAtAfter(Date createdAt);

    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM Client c JOIN c.contact ct WHERE LOWER(ct.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Client> findByContactEmailContainingIgnoreCase(@Param("email") String email, Pageable pageable);

    @Query("SELECT c FROM Client c JOIN c.contact ct WHERE LOWER(ct.phone) LIKE LOWER(CONCAT('%', :phone, '%'))")
    Page<Client> findByContactPhoneContainingIgnoreCase(@Param("phone") String phone, Pageable pageable);
}
