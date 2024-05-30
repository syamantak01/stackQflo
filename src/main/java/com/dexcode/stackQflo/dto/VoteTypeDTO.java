package com.dexcode.stackQflo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteTypeDTO {
    private Long voteTypeId;
    private String voteType;
}
