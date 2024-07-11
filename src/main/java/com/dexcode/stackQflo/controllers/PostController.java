package com.dexcode.stackQflo.controllers;

import com.dexcode.stackQflo.config.AppConstants;
import com.dexcode.stackQflo.dto.PostDTO;
import com.dexcode.stackQflo.entities.Post;
import com.dexcode.stackQflo.response.AnswerResponse;
import com.dexcode.stackQflo.services.FileService;
import com.dexcode.stackQflo.services.PostService;
import com.dexcode.stackQflo.validations.ValidationGroups;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

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
                                                    @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                    @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                    @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                    @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String direction
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

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<PostDTO>> searchPost(@PathVariable String keyword){
        List<PostDTO> searchedPosts = postService.searchPost(keyword);
        return ResponseEntity.ok(searchedPosts);
    }

    // upload image
    @PostMapping("/{postId}/image/upload")
    public ResponseEntity<PostDTO> uploadPostImage(@RequestParam MultipartFile image,
                                                        @PathVariable Long postId){
        try {
            PostDTO postDTO = postService.getPostById(postId);
            String fileName = fileService.uploadImage(path, image);
            postDTO.setImage(fileName);
            PostDTO updatedPost = postService.updatePost(postDTO, postId);

            return ResponseEntity.ok(updatedPost);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // serve image
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }



}
