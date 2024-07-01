package com.dexcode.stackQflo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name="post_types")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTypeId;

    @Column(unique = true, nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "postType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> posts;
}

