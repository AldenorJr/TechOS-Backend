package br.com.tech.os.ostech.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_services")
@Data
@NoArgsConstructor
public class OrderService {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "smartphoneId", referencedColumnName = "id")
    private Smartphone smartphoneId;

    @ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    private Client clientId;

    @OneToOne
    @JoinColumn(name = "budgetId", referencedColumnName = "id")
    private Budget budgetId;

    private Date departureDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

}
