package com.dexcode.stackQflo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDTO {
    private Long tagId;
    private String tagName;
    private String description;
    private Set<Long> postIds;
}
