package com.online.learning.model.mapper;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Result;

import java.util.List;

public interface ResultMapper {
    ResultDTO toDTO(Result result);

    List<ResultDTO> toListDTO(List<Result> resultList);

    Result toEntity(ResultDTO resultDTO);
}
