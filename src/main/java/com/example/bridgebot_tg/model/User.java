package com.example.bridgebot_tg;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userRegisterToken;

    @NotNull
    private String userName;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

}
