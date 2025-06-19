package br.com.tech.os.ostech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String email;
    private String phone;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;


}
