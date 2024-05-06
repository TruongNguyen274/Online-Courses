package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.LectureDTO;
import com.online.learning.model.entity.Lecture;
import com.online.learning.model.mapper.ChapterMapper;
import com.online.learning.model.mapper.LectureMapper;
import com.online.learning.service.ChapterService;
import com.online.learning.service.LectureService;
import com.online.learning.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LectureMapperImpl implements LectureMapper {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public LectureDTO toDTO(Lecture lecture) {
        if (lecture == null)
            return null;

        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(lecture.getId());
        lectureDTO.setTitle(lecture.getTitle());
        lectureDTO.setLinkVideo(lecture.getLinkVideo());
        lectureDTO.setLinkResource(lecture.getLinkResource());
        lectureDTO.setContent(lecture.getContent());
        lectureDTO.setTimeLesson(lecture.getTimeLesson());
        lectureDTO.setOwnerId(lectureDTO.getOwnerId());
        lectureDTO.setSortOrder(FormatUtils.formatNumber(lecture.getSortOrder()));
        lectureDTO.setStatus(lecture.isStatus());

        lectureDTO.setChapterId(lecture.getChapter().getId());
        lectureDTO.setChapterDTO(chapterMapper.toDTO(lecture.getChapter()));

        return lectureDTO;
    }

    @Override
    public List<LectureDTO> toListDTO(List<Lecture> lectures) {
        if (lectures == null)
            return null;

        List<LectureDTO> result = new ArrayList<>();
        lectures.forEach(lecture -> result.add(toDTO(lecture)));

        return result;
    }

    @Override
    public Lecture toEntity(LectureDTO lectureDTO) {
        if (lectureDTO == null)
            return null;

        Lecture lecture = lectureService.findById(lectureDTO.getId());
        if (lecture == null)
            lecture = new Lecture();

        lecture.setTitle(lectureDTO.getTitle());
//        lecture.setLinkVideo(lectureDTO.getLinkVideo());
//        lecture.setLinkResource(lectureDTO.getLinkResource());
        lecture.setContent(lectureDTO.getContent());
        lecture.setTimeLesson(lectureDTO.getTimeLesson());
        lecture.setStatus(lectureDTO.isStatus());
        lecture.setOwnerId(lectureDTO.getOwnerId());
        lecture.setChapter(chapterService.findById(lectureDTO.getChapterId()));

        return lecture;
    }
}
