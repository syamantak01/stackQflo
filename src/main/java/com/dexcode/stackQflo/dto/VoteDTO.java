package com.dexcode.stackQflo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDTO {
    private Long voteId;
    private Long voteTypeId;
    private Long userId;
    private Long postId;
    private LocalDateTime timestamp;
}
