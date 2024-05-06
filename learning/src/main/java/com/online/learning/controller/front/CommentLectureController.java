package com.online.learning.controller.front;

import com.online.learning.model.dto.CommentDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Comment;
import com.online.learning.model.mapper.CommentMapper;
import com.online.learning.service.CommentService;
import com.online.learning.service.custom.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentLectureController {

    private static final String REDIRECT_URL = "/my-courses";

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @PostMapping("/form")
    public String save(Model model, Authentication authentication, CommentDTO commentDTO, @RequestParam long courseId) {
        try {
            if (authentication == null) {
                return "redirect:/login";
            }

            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Comment comment = commentMapper.toEntity(commentDTO);
            comment.setCreatedDate(new Date());
            comment.setStatus(true);
            comment.setCourseId(courseId);
            comment.setOwner(account);
            comment.setCommentCourse(false);

            commentService.save(comment);

            String urlRedirect = "/course/learn?courseId=" + courseId + "&lectureId=" + comment.getLecture().getId();
            return "redirect:" + urlRedirect;
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form/course")
    public String saveCourse(Model model, Authentication authentication, CommentDTO commentDTO, @RequestParam long courseId) {
        try {
            if (authentication == null) {
                return "redirect:/login";
            }

            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Comment comment = commentMapper.toEntity(commentDTO);
            comment.setCreatedDate(new Date());
            comment.setStatus(true);
            comment.setCourseId(courseId);
            comment.setOwner(account);
            comment.setCommentCourse(true);

            commentService.save(comment);

            String urlRedirect = "/course?courseId=" + courseId;
            return "redirect:" + urlRedirect;
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
