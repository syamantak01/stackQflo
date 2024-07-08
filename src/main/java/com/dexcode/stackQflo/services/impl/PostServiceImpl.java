package com.dexcode.stackQflo.services.impl;

import com.dexcode.stackQflo.dto.PostDTO;
import com.dexcode.stackQflo.entities.Post;
import com.dexcode.stackQflo.entities.PostType;
import com.dexcode.stackQflo.entities.Tag;
import com.dexcode.stackQflo.exceptions.InvalidOperationException;
import com.dexcode.stackQflo.exceptions.ResourceNotFoundException;
import com.dexcode.stackQflo.repositories.PostRepository;
import com.dexcode.stackQflo.repositories.PostTypeRepository;
import com.dexcode.stackQflo.repositories.TagRepository;
import com.dexcode.stackQflo.repositories.UserRepository;
import com.dexcode.stackQflo.response.AnswerResponse;
import com.dexcode.stackQflo.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    PostType postType;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);
        Post savedPost = postRepository.save(post);

        return convertToDTO(savedPost);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> findPostsByTagsTagId(Long tagId){

        if(!tagRepository.existsById(tagId)){
            throw new ResourceNotFoundException("Tag", "tagId", tagId);
        }

        return postRepository.findPostsByTagsTagId(tagId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> findPostsByUserUserId(Long userId){
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User", "userId", userId);
        }

        return postRepository.findPostsByUserUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostById(Long postId) {
        return postRepository.findById(postId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post",
                        "postId",
                        postId)
                );
    }

    @Override
    public AnswerResponse getAnswerPosts(Long parentQuestionId, Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Post questionPost = postRepository.findById(parentQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", parentQuestionId));

        if(!questionPost.getPostType().getTypeName().equalsIgnoreCase("question")){
            throw new InvalidOperationException(String.format("Can't get answers of post with id: %d as the post is not of question type", parentQuestionId));
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        if(direction.equalsIgnoreCase("DESC")){
            p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }

        Page<Post> page = postRepository.findPostsByParentQuestionPostId(parentQuestionId, p);
        List<Post> answers = page.getContent();

        List<PostDTO> answerContent = answers
                .stream()
                .map(this::convertToDTO)
                .toList();

        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setAnswers(answerContent);
        answerResponse.setPageNumber(page.getNumber());
        answerResponse.setPageSize(page.getSize());
        answerResponse.setTotalAnswers((int) page.getTotalElements());
        answerResponse.setTotalPages(page.getTotalPages());
        answerResponse.setLastPage(page.isLast());

        return answerResponse;
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        postType = post.getPostType();

        return postRepository.findById(postId).map(existingPost -> {

            if(postDTO.getTitle() != null && !postDTO.getTitle().isBlank()){
                existingPost.setTitle(postDTO.getTitle());
            }
            if(postDTO.getDescription() != null && !postDTO.getDescription().isBlank()){
                existingPost.setDescription(postDTO.getDescription());
            }
            if(postDTO.getImage() != null){
                existingPost.setImage(postDTO.getImage());
            }
            if(postDTO.getTimestamp() != null){
                existingPost.setTimestamp(postDTO.getTimestamp());
            }

            if(postDTO.getAcceptedAnswerId() != null){

                // update the accepted answer of the post only if the post is of question type
                if(existingPost.getPostType().getTypeName().equalsIgnoreCase("question")){

                    Post acceptedAnswer = postRepository
                            .findById(postDTO.getAcceptedAnswerId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Post",
                                            "postId",
                                            postDTO.getAcceptedAnswerId()
                                    )
                            );

                    // check if the given accepted answer is of answer type or question type
                    // update only if its of answer type
                    if(acceptedAnswer.getPostType().getTypeName().equalsIgnoreCase("answer")){
                        existingPost.setAcceptedAnswer(acceptedAnswer);
                    }

                    else{
                        throw new InvalidOperationException("Cannot update the accepted answer as the accepted answer post is of question type");
                    }
                }

                else{
                    throw new InvalidOperationException("Cannot update the accepted answer of a post which is of answer type");
                }

            }

            if(postDTO.getTagIds() != null){
                Set<Tag> updatedTags = new HashSet<Tag>();
                postDTO.getTagIds().forEach(tagId -> {
                    if(tagRepository.findById(tagId).isPresent()){
                        updatedTags.add(tagRepository.findById(tagId).get());
                    }
                    else{
                        throw new ResourceNotFoundException("Tag", "tagId", tagId);
                    }
                });
                existingPost.setTags(updatedTags);
            }
            Post updatedPost = postRepository.save(existingPost);
            return convertToDTO(updatedPost);
        }).orElseThrow(() -> new ResourceNotFoundException(
                "Post", "postId", postId));
    }

    @Override
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        // post is of answer type
        if(post.getParentQuestion() != null){

            Post parentQuestion = post.getParentQuestion();

            // from parent question's answers list remove the post
            parentQuestion.getAnswers().remove(post);

            // set the parent question to null;
            post.setParentQuestion(null);
        }

        // post if of question type
        else{

            // remove answer posts
            for(Post answer: post.getAnswers()){
                this.deletePost(answer.getPostId());
            }

            Set<Tag> tags = post.getTags();

            //set the tags null
            post.setTags(null);

            // remove posts from tag dto to maintain consistency in bidirectional relation
            for(Tag tag : tags){
                tag.getPosts().remove(post);
            }
        }

        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> searchPost(String keyword) {
        return postRepository.findByTitleContaining(keyword)
                .stream()
                .map(this::convertToDTO)
                .toList();

    }

    private PostDTO convertToDTO(Post post){
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        Set<Tag> tags = post.getTags();

        if (tags != null){
            Set<Long> tagIds = post.getTags()
                    .stream()
                    .map(Tag::getTagId)
                    .collect(Collectors.toSet());

            postDTO.setTagIds(tagIds);
        }

        return postDTO;
    }

    private Post convertToEntity(PostDTO postDTO){
        Post post = modelMapper.map(postDTO, Post.class);

        userRepository.findById(postDTO.getUserId())
                .ifPresentOrElse(post::setUser, () -> {
                    throw new ResourceNotFoundException("User", "userID", postDTO.getUserId());
                });

        if(postDTO.getParentQuestionId() != null){
            postRepository.findById(postDTO.getParentQuestionId())
                    .ifPresentOrElse(post::setParentQuestion, () -> {
                        throw new ResourceNotFoundException("Post", "postId", postDTO.getParentQuestionId());
                    });
        }

        if(postDTO.getAcceptedAnswerId() != null){
            postRepository.findById(postDTO.getAcceptedAnswerId())
                    .ifPresentOrElse(post::setAcceptedAnswer, () -> {
                        throw new ResourceNotFoundException("Post", "postId", postDTO.getAcceptedAnswerId());
                    });
        }

        postTypeRepository.findById(postDTO.getPostTypeId())
                .ifPresentOrElse(post::setPostType, () -> {
                    throw new ResourceNotFoundException("Post Type", "postTypeId", postDTO.getPostTypeId());
                });

        Set<Long> tagIds = postDTO.getTagIds();

        if(tagIds != null){
            Set<Tag> tags = tagIds
                    .stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag", "tagId", tagId)))
                    .collect(Collectors.toSet());
            post.setTags(tags);
        }

        return post;
    }
}
