package com.dexcode.stackQflo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String about;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonManagedReference
    private Role role;

//    @Override
//    public String toString() {
//        return "User{" +
//                "userId=" + userId +
//                ", username='" + username + '\'' +
//                '}';
//    }

}
