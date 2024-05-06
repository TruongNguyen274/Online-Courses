package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Result;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.model.mapper.ExamMapper;
import com.online.learning.model.mapper.ResultMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.ExamService;
import com.online.learning.service.ResultService;
import com.online.learning.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResultMapperImpl implements ResultMapper {

    @Autowired
    ResultService resultService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ExamMapper examMapper;

    @Autowired
    ExamService examService;

    @Override
    public ResultDTO toDTO(Result result) {
        if (result == null){
            return null;
        }
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setId(result.getId());
        resultDTO.setStartTime(DateUtil.convertDateToString(result.getStartTime(), "yyyy-mm-dd hh:mm:ss"));
        resultDTO.setEndTime(DateUtil.convertDateToString(result.getEndTime(), "yyyy-mm-dd hh:mm:ss"));
        resultDTO.setScore(result.getScore());
        resultDTO.setResult(result.getResult());

        //account
        resultDTO.setAccountId(result.getAccount().getId());
        resultDTO.setAccountDTO(accountMapper.toDTO(result.getAccount()));

        //Exam
        resultDTO.setExamId(result.getExam().getId());
        resultDTO.setExamDTO(examMapper.toDTO(result.getExam()));

        resultDTO.setStatus(result.isStatus());
        return resultDTO;
    }

    @Override
    public List<ResultDTO> toListDTO(List<Result> resultList) {
        if (resultList == null) {
            return null;
        }
        List<ResultDTO> list = new ArrayList<>(resultList.size());
        for (Result result : resultList) {
            ResultDTO resultDTO = toDTO(result);
            if (resultDTO != null) {
                list.add(resultDTO);
            }
        }
        return list;
    }

    @Override
    public Result toEntity(ResultDTO resultDTO) {
        if (resultDTO == null) {
            return null;
        }
        Result result = resultService.findById(resultDTO.getId()) ;

        if (result == null) {
            result = new Result();
        }

        result.setResult(resultDTO.getResult());
        result.setScore(resultDTO.getScore());
        result.setStartTime(DateUtil.convertStringToDate(resultDTO.getStartTime(), "yyyy-mm-dd hh:mm:ss"));
        result.setEndTime(DateUtil.convertStringToDate(resultDTO.getEndTime(), "yyyy-mm-dd hh:mm:ss"));
        result.setAccount(accountService.findById(resultDTO.getAccountId()));
        result.setExam(examService.findById(resultDTO.getExamId()));
        result.setStatus(resultDTO.isStatus());
        return result;
    }
}
