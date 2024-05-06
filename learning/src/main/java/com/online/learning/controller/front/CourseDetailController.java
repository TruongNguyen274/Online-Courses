package com.online.learning.controller.front;

import com.online.learning.model.dto.ChapterDTO;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseDetailController {

    private static final int LIMIT_COURSE_RELATED = 3;

    @Autowired
    private CourseService courseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("")
    public String course_detail(Model model, Authentication authentication, @RequestParam long courseId) {
        Course course = courseService.findById(courseId);

        if (course == null) {
            return "front/403_error";
        }

        if (authentication != null) {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();

            if (account != null) {
                boolean isFound = orderService.findCourseInAccount(course, account);
                model.addAttribute("isFound", isFound);
            }
        }

        List<Chapter> chapterList = chapterService.findByCourse(course);
        List<Course> courseListRelated = courseService.findByRelatedAndStatusIsTrue(course, course.getCategory(), LIMIT_COURSE_RELATED);
        List<ChapterDTO> chapterListDTO = chapterMapper.toListDTO(chapterList);
        if (!chapterListDTO.isEmpty()) {
            for (int i = 0; i < chapterListDTO.size(); i++) {
                ChapterDTO chapterDTO = chapterListDTO.get(i);

                Chapter chapter = new Chapter();
                chapter.setId(chapterDTO.getId());

                List<Lecture> lectureList = lectureService.findByChapter(chapter);
                chapterDTO.setLectureList(lectureMapper.toListDTO(lectureList));

                chapterListDTO.set(i, chapterDTO);
            }
        }

        List<Order> orderList = orderService.findByCourse(course);
        List<Comment> commentList = commentService.findByCourseIdAndIsCommentCourse(courseId);

        model.addAttribute("courseDTO", courseMapper.toDTO(course));
        model.addAttribute("chapterListDTO", chapterListDTO);
        model.addAttribute("courseListRelatedDTO", courseMapper.toListDTO(courseListRelated));
        model.addAttribute("orderListDTO", orderMapper.toListDTO(orderList));
        model.addAttribute("commentDTOList", commentMapper.toListDTO(commentList));

        return "front/course_detail";
    }

}
