package com.felfel.hogwarts_artifacts_online.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type User.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private Boolean enabled;
    private String roles;
    private String password;
}
