package com.dexcode.stackQflo.services;

import com.dexcode.stackQflo.dto.PostDTO;

import java.util.List;

public interface PostService {
    // Create
    PostDTO createPost(PostDTO postDTO);

    //Read
    List<PostDTO> getAllPosts();

    // Get Posts by Tags
    List<PostDTO> findPostsByTagsTagId(Long tagId);

    // Get Posts by Users
    List<PostDTO> findPostsByUserUserId(Long userId);

    // Get Post by Id
    PostDTO getPostById(Long postId);

    //Update
    PostDTO updatePost(PostDTO postDTO, Long postId);

    //Delete
    void deletePost(Long postId);


}
