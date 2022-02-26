package com.example.springredditclone.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message="Password is required")
    private String password;

    @Email
    @NotEmpty(message="Email is required")
    private String email;

    private Instant createdAt;

    private boolean enabled;
}
