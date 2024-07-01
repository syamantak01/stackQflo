package com.dexcode.stackQflo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import jakarta.persistence.*;

import java.util.Set;

@Data
@Entity
@Table(name="post_types")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "posts")
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTypeId;

    @Column(unique = true, nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "postType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Post> posts;

//    @Override
//    public String toString() {
//        return "PostType{" +
//                "postTypeId=" + postTypeId +
//                ", typeName='" + typeName + '\'' +
//                '}';
//    }
}

