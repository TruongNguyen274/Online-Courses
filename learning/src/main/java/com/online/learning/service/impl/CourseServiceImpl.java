package com.online.learning.service.impl;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Category;
import com.online.learning.model.entity.Course;
import com.online.learning.repository.CategoryRepository;
import com.online.learning.repository.CourseRepository;
import com.online.learning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findAllByActive() {
        return courseRepository.findByStatusIsTrue();
    }

    @Override
    public List<Course> findByOwner(Account owner) {
        return courseRepository.findByOwner(owner);
    }

    @Override
    public Page<Course> findByCategoryAndStatusIsTrue(Category category, Pageable pageable) {
        return category == null
                ? courseRepository.findByStatusTrue(pageable)
                : courseRepository.findByCategoryAndStatusTrue(category, pageable);
    }

    @Override
    public List<Course> findByRelatedAndStatusIsTrue(Course course, Category category, int limit) {
        return courseRepository.findByRelated(course.getId(), category.getId(), limit);
    }

    @Override
    public Page<Course> searchByTitleAndStatusIsTrue(String title, Pageable pageable) {
        return courseRepository.searchByTitle(title, pageable);
    }

    @Override
    public Course findByTitle(String title) {
        return courseRepository.findByTitleAndStatusTrue(title);
    }

    @Override
    public Course findById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

//    @Override
//    public Course save(CourseDTO courseDTO) {
//        Course course = courseRepository.findById(courseDTO.getId()).orElse(null);
//        if (course == null){
//            course = new Course();
//            course.setStatus(true);
//        }
//        if (!StringUtils.isEmpty(courseDTO.getTitle())) {
//            course.setTitle(courseDTO.getTitle());
//        }
//        if (!StringUtils.isEmpty(courseDTO.getDescription())) {
//            course.setDescription(courseDTO.getDescription());
//        }
//        if (!StringUtils.isEmpty(courseDTO.isStatus())) {
//            course.setStatus(courseDTO.isStatus());
//        }
//        if (!StringUtils.isEmpty(courseDTO.getPrice())) {
//            course.setPrice(FormatUtils.formatNumber(courseDTO.getPrice()));
//        }
//        if (!StringUtils.isEmpty(courseDTO.getDiscount())) {
//            course.setDiscount(FormatUtils.formatNumber(courseDTO.getDiscount()));
//        }
//        course.setCategory(categoryRepository.findById(courseDTO.getCategoryId()).orElse(null));
//
//        return courseRepository.save(course);
//    }


    @Override
    public List<Course> getRandomCourses(int limit) {
        List<Course> allCourses = findAllByActive();
        List<Course> randomCourses = new ArrayList<>();

        if (allCourses.size() <= limit) {
            return allCourses;
        }

        Random random = new Random();
        while (randomCourses.size() < limit) {
            Course randomCourse = allCourses.get(random.nextInt(allCourses.size()));
            if (!randomCourses.contains(randomCourse)) {
                randomCourses.add(randomCourse);
            }
        }

        return randomCourses;
    }

}
