package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.ChapterDTO;
import com.online.learning.model.entity.Chapter;
import com.online.learning.model.mapper.ChapterMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.model.mapper.ExamMapper;
import com.online.learning.service.ChapterService;
import com.online.learning.service.CourseService;
import com.online.learning.service.ExamService;
import com.online.learning.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChapterMapperImpl implements ChapterMapper {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public ChapterDTO toDTO(Chapter chapter) {
        if (chapter == null)
            return null;

        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setId(chapter.getId());
        chapterDTO.setTitle(chapter.getTitle());
        chapterDTO.setDescription(chapter.getDescription());
        chapterDTO.setOwnerId(chapter.getOwnerId());
        chapterDTO.setTotalLesson(FormatUtils.formatNumber(chapter.getTotalLesson()));
        chapterDTO.setSortOrder(FormatUtils.formatNumber(chapter.getSortOrder()));
        chapterDTO.setStatus(chapter.isStatus());

        chapterDTO.setCourseId(chapter.getCourse().getId());
        chapterDTO.setCourseDTO(courseMapper.toDTO(chapter.getCourse()));

        return chapterDTO;
    }

    @Override
    public List<ChapterDTO> toListDTO(List<Chapter> chapters) {
        if (chapters == null)
            return null;

        List<ChapterDTO> result = new ArrayList<>();
        chapters.forEach(chapter -> result.add(toDTO(chapter)));

        return result;
    }

    @Override
    public Chapter toEntity(ChapterDTO chapterDTO) {
        if (chapterDTO == null)
            return null;

        Chapter chapter = chapterService.findById(chapterDTO.getId());
        if (chapter == null)
            chapter = new Chapter();

        chapter.setTitle(chapterDTO.getTitle());
        chapter.setDescription(chapterDTO.getDescription());
        chapter.setStatus(chapterDTO.isStatus());
        chapter.setOwnerId(chapterDTO.getOwnerId());
        chapter.setTotalLesson(FormatUtils.formatNumber(chapterDTO.getTotalLesson()));
        chapter.setCourse(courseService.findById(chapterDTO.getCourseId()));

        return chapter;
    }

}
