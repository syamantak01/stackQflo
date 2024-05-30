package com.dexcode.stackQflo.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name="posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String description;
    private String image;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private Post parentQuestion;

    @ManyToOne
    @JoinColumn(name = "accepted_answer_id")
    private Post acceptedAnswer;

    @ManyToOne
    @JoinColumn(name = "post_type_id", nullable = false)
    private PostType postType;

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> answers;

    @OneToMany(mappedBy = "acceptedAnswer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> referencedAnswers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Vote> votes;
}
