package com.online.learning.controller.back;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Comment;
import com.online.learning.model.entity.Course;
import com.online.learning.model.mapper.CommentMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.service.CommentService;
import com.online.learning.service.CourseService;
import com.online.learning.service.custom.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/back/comment")
public class CommentController {

    private static final String REDIRECT_URL = "/back/comment";

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            List<Course> courseList;
            List<Comment> commentList;
            if (account.getRole().equalsIgnoreCase("ADMIN")) {
                commentList = commentService.findAll();
            } else {
                courseList = courseService.findByOwner(account);
                commentList = new ArrayList<>();

                for (Course course : courseList) {
                    List<Comment> tempCommentList = commentService.findByCourseId(course.getId());
                    commentList.addAll(tempCommentList);
                }
            }

            model.addAttribute("commentDTOList", commentMapper.toListDTO(commentList));
            return "back/comment_list";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/hidden/{commentId}")
    public String hidden(Model model, @PathVariable long commentId) {
        try {
            Comment comment = commentService.findById(commentId);
            comment.setStatus(false);
            commentService.save(comment);
            return "redirect:/back/comment";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/show/{commentId}")
    public String show(Model model, @PathVariable long commentId) {
        try {
            Comment comment = commentService.findById(commentId);
            comment.setStatus(true);
            commentService.save(comment);
            return "redirect:/back/comment";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
