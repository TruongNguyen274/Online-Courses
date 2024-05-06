package com.online.learning.repository;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByChapter(Chapter chapter);

    List<Lecture> findByOwnerId(long ownerId);

    List<Lecture> findByChapterOrderBySortOrderAsc(Chapter chapter);

}
