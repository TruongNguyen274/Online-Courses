package com.online.learning.repository.custom;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResultCustomRepositoryImpl implements ResultCustomRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<ResultDTO> findByCourseId(long courseId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT R.* FROM result AS R ");
        stringBuilder.append("LEFT JOIN exam E on R.exam_id = E.id ");
        stringBuilder.append("WHERE E.course_id = :courseId ");
        stringBuilder.append("ORDER BY R.created_on DESC ");

        mapSqlParameterSource.addValue("courseId", courseId);

        return namedParameterJdbcTemplate.query(stringBuilder.toString(), mapSqlParameterSource,
                (rs, rowNum) -> ResultDTO.builder()
                        .id(rs.getLong("id"))
                        .startTime(rs.getString("start_time"))
                        .endTime(rs.getString("end_time"))
                        .score(rs.getDouble("score"))
                        .result(rs.getString("result"))
                        .examId(rs.getLong("exam_id"))
                        .accountId(rs.getLong("account_id"))
                        .status(rs.getBoolean("status"))
                        .build());
    }

    @Override
    public List<ResultDTO> findByChapterId(long chapterId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT R.* FROM result AS R ");
        stringBuilder.append("LEFT JOIN exam E on R.exam_id = E.id ");
        stringBuilder.append("WHERE E.chapter_id = :chapterId ");
        stringBuilder.append("ORDER BY R.created_on DESC ");

        mapSqlParameterSource.addValue("chapterId", chapterId);

        return namedParameterJdbcTemplate.query(stringBuilder.toString(), mapSqlParameterSource,
                (rs, rowNum) -> ResultDTO.builder()
                        .id(rs.getLong("id"))
                        .startTime(rs.getString("start_time"))
                        .endTime(rs.getString("end_time"))
                        .score(rs.getDouble("score"))
                        .result(rs.getString("result"))
                        .examId(rs.getLong("exam_id"))
                        .accountId(rs.getLong("account_id"))
                        .status(rs.getBoolean("status"))
                        .build());
    }

    @Override
    public List<ResultDTO> findByLectureId(long lectureId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT R.* FROM result AS R ");
        stringBuilder.append("LEFT JOIN exam E on R.exam_id = E.id ");
        stringBuilder.append("WHERE E.lecture_id = :lectureId ");
        stringBuilder.append("ORDER BY R.created_on DESC ");

        mapSqlParameterSource.addValue("lectureId", lectureId);

        return namedParameterJdbcTemplate.query(stringBuilder.toString(), mapSqlParameterSource,
                (rs, rowNum) -> ResultDTO.builder()
                        .id(rs.getLong("id"))
                        .startTime(rs.getString("start_time"))
                        .endTime(rs.getString("end_time"))
                        .score(rs.getDouble("score"))
                        .result(rs.getString("result"))
                        .examId(rs.getLong("exam_id"))
                        .accountId(rs.getLong("account_id"))
                        .status(rs.getBoolean("status"))
                        .build());
    }
}
