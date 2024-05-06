package com.online.learning.repository.custom;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultCustomRepository {

    List<ResultDTO> findByCourseId(long courseId);

    List<ResultDTO> findByChapterId(long chapterId);

    List<ResultDTO> findByLectureId(long lectureId);

}
