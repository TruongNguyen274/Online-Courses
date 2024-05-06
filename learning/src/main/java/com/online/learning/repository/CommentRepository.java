package com.online.learning.repository;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Comment;
import com.online.learning.model.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCourseIdOrderByCreatedDateDesc(long courseId);

    List<Comment> findByCourseIdAndIsCommentCourse(long courseId, boolean isCourse);

    List<Comment> findByOwner(Account owner);

    List<Comment> findByLectureAndStatusIsTrueOrderByCreatedDateDesc(Lecture lecture);

}
