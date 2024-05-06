package com.online.learning.service;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Comment;
import com.online.learning.model.entity.Lecture;

import java.util.List;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(long id);

    List<Comment> findByCourseId(long courseId);

    List<Comment> findByCourseIdAndIsCommentCourse(long courseId);

    List<Comment> findByOwner(Account owner);

    List<Comment> findByLecture(Lecture lecture);

    Comment save(Comment comment);

}
