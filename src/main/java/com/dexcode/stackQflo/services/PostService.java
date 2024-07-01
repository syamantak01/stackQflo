package com.dexcode.stackQflo.services;

import com.dexcode.stackQflo.dto.PostDTO;

import java.util.List;

public interface PostService {
    // Create
    PostDTO createPost(PostDTO postDTO);

    //Read
    List<PostDTO> getAllPosts();

    PostDTO getPostById(Long postId);

    //Update
    PostDTO updatePost(PostDTO postDTO, Long postId);

    //Delete
    void deletePost(Long postId);
}
