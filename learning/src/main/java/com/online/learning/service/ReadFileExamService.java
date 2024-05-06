package com.online.learning.service;

import com.online.learning.model.dto.ExamDTO;

import java.io.IOException;

public interface ReadFileExamService {

    ExamDTO readQuiz(ExamDTO examDTO) throws IOException;

}
