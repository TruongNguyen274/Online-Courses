package com.online.learning.repository;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByCourse(Course course);

    List<Chapter> findByOwnerId(long ownerId);

    List<Chapter> findByCourseOrderBySortOrderAsc(Course course);

    Chapter findByCourseId(long id);
}
