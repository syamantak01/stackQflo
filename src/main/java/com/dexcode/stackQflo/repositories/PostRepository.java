package com.dexcode.stackQflo.repositories;

import com.dexcode.stackQflo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
