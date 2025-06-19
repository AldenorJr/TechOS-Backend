package br.com.tech.os.ostech.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.tech.os.ostech.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.tech.os.ostech.model.OrderService;

import java.util.Date;
import java.util.List;

public interface OrderServiceRepository extends JpaRepository<OrderService, String> {
    @Query("SELECT o FROM OrderService o WHERE LOWER(o.clientId.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<OrderService> findByClientName(@Param("name") String name, Pageable pageable);

    @Query("SELECT o FROM OrderService o WHERE LOWER(o.smartphoneId.model) LIKE LOWER(CONCAT('%', :model, '%'))")
    Page<OrderService> findBySmartphoneModel(@Param("model") String model, Pageable pageable);

    Integer countByCreatedAtAfter(Date createdAtAfter);

    Integer countByDepartureDateIsNotNull();

    Integer countByDepartureDateAfter(Date departureDate);

    List<OrderService> findTop5ByOrderByUpdatedAtDesc();

    @Query("SELECT o FROM OrderService o WHERE FUNCTION('DATE', o.createdAt) = :date")
    List<OrderService> findByCreatedAtDate(@Param("date") Date date);

    @Query("SELECT o FROM OrderService o WHERE FUNCTION('DATE', o.departureDate) = :date")
    List<OrderService> findByDepartureDateDate(@Param("date") Date date);

    Page<OrderService> findByStatus(Status status, Pageable pageable);
}
