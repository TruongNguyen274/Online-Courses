package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.AnswerDTO;
import com.online.learning.model.entity.Answer;
import com.online.learning.model.mapper.*;
import com.online.learning.service.QuestionService;
import com.online.learning.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Autowired
    ResultService resultService;

    @Autowired
    ResultMapper resultMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;

    @Override
    public AnswerDTO toDTO(Answer answer) {
        if (answer == null){
            return null;
        }
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setOption(answer.getChoose());
        answerDTO.setCorrect(answer.isCorrect());

        //result
        answerDTO.setResultId(answer.getResult().getId());
        answerDTO.setResultDTO(resultMapper.toDTO(answer.getResult()));

        //Question
        answerDTO.setQuestionId(answer.getQuestion().getId());
        answerDTO.setQuestionDTO(questionMapper.toDTO(answer.getQuestion()));

        answerDTO.setStatus(answer.isStatus());
        return answerDTO;
    }

    @Override
    public List<AnswerDTO> toListDTO(List<Answer> answerList) {
        if (answerList == null) {
            return null;
        }
        List<AnswerDTO> list = new ArrayList<>(answerList.size());
        for (Answer answer : answerList) {
            AnswerDTO answerDTO = toDTO(answer);
            if (answerDTO != null) {
                list.add(answerDTO);
            }
        }
        return list;
    }

    @Override
    public Answer toEntity(AnswerDTO answerDTO) {
        if (answerDTO == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setChoose(answerDTO.getOption());
        answer.setCorrect(answerDTO.isCorrect());
        answer.setResult(resultService.findById(answerDTO.getResultId()));
        answer.setQuestion(questionService.findById(answerDTO.getQuestionId()));
        answer.setStatus(answerDTO.isStatus());
        return answer;
    }
}
