package com.dexcode.stackQflo.entities;

import lombok.*;

import jakarta.persistence.*;

import java.util.Set;

@Data
@Entity
@Table(name="tags")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long tagId;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String tagName;

    @EqualsAndHashCode.Include
    private String description;

    @ManyToMany(mappedBy = "tags", cascade = {CascadeType.PERSIST})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Post> posts;
}
