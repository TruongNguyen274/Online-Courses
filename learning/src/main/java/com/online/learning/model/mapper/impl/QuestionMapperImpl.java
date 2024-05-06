package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.model.entity.Question;
import com.online.learning.model.mapper.ExamMapper;
import com.online.learning.model.mapper.QuestionMapper;
import com.online.learning.service.ExamService;
import com.online.learning.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Autowired
    ExamMapper examMapper;

    @Autowired
    ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Override
    public QuestionDTO toDTO(Question question) {
        if (question == null){
            return null;
        }
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setTitle(question.getTitle());
        questionDTO.setOption1(question.getOption1());
        questionDTO.setOption2(question.getOption2());
        questionDTO.setOption3(question.getOption3());
        questionDTO.setOption4(question.getOption4());
        questionDTO.setAnswer(question.getAnswer());

        //Exam
        questionDTO.setExamId(question.getExam().getId());
        ExamDTO examDTO = examMapper.toDTO(question.getExam());
        questionDTO.setExamDTO(examDTO);

        questionDTO.setExamTitle(examDTO.getTitle());
        questionDTO.setExamType(examDTO.getType());
        questionDTO.setStatus(question.isStatus());
        return questionDTO;
    }

    @Override
    public List<QuestionDTO> toListDTO(List<Question> questionList) {
        if (questionList == null) {
            return null;
        }
        List<QuestionDTO> list = new ArrayList<>(questionList.size());
        for (Question question : questionList) {
            QuestionDTO questionDTO = toDTO(question);
            if (questionDTO != null) {
                list.add(questionDTO);
            }
        }
        return list;
    }

    @Override
    public Question toEntity(QuestionDTO questionDTO) {
        if (questionDTO == null) {
            return null;
        }
        Question question = questionService.findById(questionDTO.getId());
        if (question == null) {
            question = new Question();
        }

        question.setTitle(questionDTO.getTitle());
        question.setOption1(questionDTO.getOption1());
        question.setOption2(questionDTO.getOption2());
        question.setOption3(questionDTO.getOption3());
        question.setOption4(questionDTO.getOption4());
        question.setAnswer(questionDTO.getAnswer());
        question.setExam(examService.findById(questionDTO.getExamId()));
        question.setStatus(questionDTO.isStatus());
        return question;
    }
}
