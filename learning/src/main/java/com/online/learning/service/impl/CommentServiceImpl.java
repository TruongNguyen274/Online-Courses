package com.online.learning.service.impl;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Comment;
import com.online.learning.model.entity.Lecture;
import com.online.learning.repository.CommentRepository;
import com.online.learning.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> findByCourseId(long courseId) {
        return commentRepository.findByCourseIdOrderByCreatedDateDesc(courseId);
    }

    @Override
    public List<Comment> findByCourseIdAndIsCommentCourse(long courseId) {
        return commentRepository.findByCourseIdAndIsCommentCourse(courseId, true);
    }

    @Override
    public List<Comment> findByOwner(Account owner) {
        return commentRepository.findByOwner(owner);
    }

    @Override
    public List<Comment> findByLecture(Lecture lecture) {
        return commentRepository.findByLectureAndStatusIsTrueOrderByCreatedDateDesc(lecture);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
