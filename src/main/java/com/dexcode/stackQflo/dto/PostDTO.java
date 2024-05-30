package com.dexcode.stackQflo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long postId;
    private Long userId;
    private String title;
    private String description;
    private String image;
    private LocalDateTime timestamp;
    private Long parentQuestionId;
    private Long acceptedAnswerId;
    private Long postTypeId;
    private Set<Long> tagIds;

}
