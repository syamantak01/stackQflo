package com.dexcode.stackQflo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name="vote_types")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteTypeId;

    @Column(unique = true, nullable = false)
    private String voteType;

    @OneToMany(mappedBy = "voteType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Vote> votes;
}