package com.dexcode.stackQflo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name="posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"tags", "answers"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String image;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private Post parentQuestion;

    @OneToOne
    @JoinColumn(name = "accepted_answer_id")
    private Post acceptedAnswer;

    @ManyToOne
    @JoinColumn(name = "post_type_id", nullable = false)
    @JsonManagedReference
    private PostType postType;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_tag"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Post> answers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Vote> votes;

    @PreRemove
    private void preRemove() {

        if (this.postType != null && "question".equalsIgnoreCase(this.postType.getTypeName())) {
            for (Post answer : this.answers) {
                answer.setParentQuestion(null);
            }
        }

        if(this.postType != null && "answer".equalsIgnoreCase(this.postType.getTypeName())){
            if (this.parentQuestion != null) {
                this.parentQuestion.getAnswers().remove(this);
                this.setParentQuestion(null);
            }
        }
    }

    public void addAnswer(Post answerPost){
        answers.add(answerPost);
        answerPost.setParentQuestion(this);
    }

    public void removeAnswer(Post answerPost){
        answers.remove(answerPost);
        answerPost.setParentQuestion(null);
    }




}
