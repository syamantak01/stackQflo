package com.dexcode.stackQflo.controllers;

import com.dexcode.stackQflo.dto.PostDTO;
import com.dexcode.stackQflo.entities.Post;
import com.dexcode.stackQflo.response.AnswerResponse;
import com.dexcode.stackQflo.services.PostService;
import com.dexcode.stackQflo.validations.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Validated({ValidationGroups.Create.class, Default.class}) @RequestBody PostDTO postDTO){
        PostDTO createdPost = postService.createPost(postDTO);
        URI postUri = URI.create(String.format("/api/posts/%s", createdPost.getPostId()));
        return ResponseEntity.created(postUri).body(createdPost);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<PostDTO>> getPostsByTag(@PathVariable Long tagId){
        return ResponseEntity.ok(postService.findPostsByTagsTagId(tagId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(postService.findPostsByUserUserId(userId));
    }

    @GetMapping("{postId}/answers")
    public ResponseEntity<AnswerResponse> getAnswers(@PathVariable Long postId,
                                                    @RequestParam(defaultValue = "1", required = false) Integer pageNumber,
                                                    @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                    @RequestParam(defaultValue = "timestamp", required = false) String sortBy,
                                                    @RequestParam(defaultValue = "ASC", required = false) String direction
                                                    ){
        return ResponseEntity.ok(postService.getAnswerPosts(postId, pageNumber, pageSize, sortBy, direction));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(@Validated({ValidationGroups.Update.class, Default.class}) @RequestBody PostDTO postDTO, @PathVariable Long postId){
        PostDTO updatedPost = postService.updatePost(postDTO, postId);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }



}
