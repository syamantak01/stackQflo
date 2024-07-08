package com.dexcode.stackQflo.response;

import com.dexcode.stackQflo.dto.PostDTO;
import lombok.Data;

import java.util.List;

@Data
public class AnswerResponse {

    private List<PostDTO> answers;
    private int pageNumber;
    private int pageSize;
    private int totalAnswers;
    private int totalPages;
    private boolean lastPage;
}
