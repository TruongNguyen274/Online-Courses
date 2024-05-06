package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.entity.Course;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.model.mapper.CategoryMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.CategoryService;
import com.online.learning.service.CourseService;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.DateUtil;
import com.online.learning.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapperImpl implements CourseMapper {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CourseDTO toDTO(Course course) {
        if (course == null)
            return null;

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setLastUpdate(DateUtil.convertDateToString(course.getUpdatedOn(), ConstantUtil.DATE_PATTERN));
        courseDTO.setTitle(course.getTitle());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setPrice(FormatUtils.formatNumber(course.getPrice()));
        courseDTO.setDiscount(FormatUtils.formatNumber(course.getDiscount()));
        courseDTO.setDuration(course.getDuration());
        courseDTO.setAvatar(course.getAvatar());
        courseDTO.setLecture(course.getLecture());
        courseDTO.setEnrolled(course.getEnrolled());
        courseDTO.setSummary(course.getSummary());
        courseDTO.setStatus(course.isStatus());

        // base
        courseDTO.setId(course.getId());
        courseDTO.setVersion(course.getVersion());
        courseDTO.setCreatedOn(DateUtil.convertDateToString(course.getCreatedOn(), "yyyy-MM-dd"));
        courseDTO.setCreatedOn(DateUtil.convertDateToString(course.getUpdatedOn(), "yyyy-MM-dd"));

        if (course.getOwner() != null) {
            courseDTO.setOwnerId(course.getOwner().getId());
            courseDTO.setOwnerDTO(accountMapper.toDTO(course.getOwner()));
        }

        courseDTO.setCategoryId(course.getCategory().getId());
        courseDTO.setCategoryDTO(categoryMapper.toDTO(course.getCategory()));

        int newPrice = course.getPrice() - ((course.getPrice() * course.getDiscount()) / 100) ;
        courseDTO.setNewPrice(FormatUtils.formatNumber(newPrice));

        return courseDTO;
    }

    @Override
    public List<CourseDTO> toListDTO(List<Course> courses) {
        if (courses == null)
            return null;

        List<CourseDTO> result = new ArrayList<>();
        courses.forEach(course -> result.add(toDTO(course)));

        return result;
    }

    @Override
    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null)
            return null;

        Course course = courseService.findById(courseDTO.getId());

        if (course == null)
            course = new Course();

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(FormatUtils.formatNumber(courseDTO.getPrice()));
        course.setDiscount(FormatUtils.formatNumber(courseDTO.getDiscount()));
        course.setSummary(courseDTO.getSummary());
        course.setDuration(courseDTO.getDuration());
        course.setLecture(courseDTO.getLecture());
        course.setStatus(courseDTO.isStatus());

        course.setOwner(accountService.findById(courseDTO.getOwnerId()));
        course.setCategory(categoryService.findById(courseDTO.getCategoryId()));

        return course;
    }
}
