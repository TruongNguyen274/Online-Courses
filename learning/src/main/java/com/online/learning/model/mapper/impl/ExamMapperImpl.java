package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.mapper.ChapterMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.model.mapper.ExamMapper;
import com.online.learning.model.mapper.LectureMapper;
import com.online.learning.service.ChapterService;
import com.online.learning.service.CourseService;
import com.online.learning.service.ExamService;
import com.online.learning.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamMapperImpl implements ExamMapper {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ExamService examService;

    @Override
    public ExamDTO toDTO(Exam exam) {
        if (exam == null) {
            return null;
        }
        ExamDTO examDTO = new ExamDTO();
        examDTO.setId(exam.getId());
        examDTO.setTitle(exam.getTitle());
        examDTO.setDescription(exam.getDescription());
        examDTO.setType(exam.getType());
        examDTO.setKindExam(exam.getKindExam());
        examDTO.setTotalTime(exam.getTotalTime());
        examDTO.setNumberOfDay(exam.getNumberOfDay());

        //course
        if (exam.getCourse() != null) {
            examDTO.setCourseId(exam.getCourse().getId());
            examDTO.setCourseDTO(courseMapper.toDTO(exam.getCourse()));
        }

        // Chapter
        if (exam.getChapter() != null) {
            examDTO.setChapterId(exam.getChapter().getId());
            examDTO.setChapterDTO(chapterMapper.toDTO(exam.getChapter()));
        }

        //Lecture
        if (exam.getLecture() != null) {
            examDTO.setLectureId(exam.getLecture().getId());
            examDTO.setLectureDTO(lectureMapper.toDTO(exam.getLecture()));
        }

        examDTO.setStatus(exam.isStatus());
        return examDTO;
    }

    @Override
    public List<ExamDTO> toListDTO(List<Exam> examList) {
        if (examList == null) {
            return null;
        }
        List<ExamDTO> list = new ArrayList<>(examList.size());
        for (Exam exam : examList) {
            ExamDTO examDTO = toDTO(exam);
            if (examDTO != null) {
                list.add(examDTO);
            }
        }
        return list;
    }

    @Override
    public Exam toEntity(ExamDTO examDTO) {
        if (examDTO == null) {
            return null;
        }

        Exam exam = examService.findById(examDTO.getId());
        if (exam == null) {
            exam = new Exam();
        }
        exam.setTitle(examDTO.getTitle());
        exam.setDescription(examDTO.getDescription());
        exam.setType(examDTO.getType());
        exam.setKindExam(examDTO.getKindExam());
        exam.setTotalTime(examDTO.getTotalTime());
        exam.setNumberOfDay(examDTO.getNumberOfDay());
        exam.setCourse(courseService.findById(examDTO.getCourseId()));
        exam.setChapter(chapterService.findById(examDTO.getChapterId()));
        exam.setLecture(lectureService.findById(examDTO.getLectureId()));
        exam.setStatus(examDTO.isStatus());
        return exam;
    }
}
