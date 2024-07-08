package com.dexcode.stackQflo.repositories;

import com.dexcode.stackQflo.dto.PostDTO;
import com.dexcode.stackQflo.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsByTagsTagId(Long tagId);

    List<Post> findPostsByUserUserId(Long userId);

    Page<Post> findPostsByParentQuestionPostId(Long questionId, Pageable p);
}
