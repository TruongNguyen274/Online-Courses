package com.online.learning.controller.front;

import com.online.learning.model.entity.Category;
import com.online.learning.model.entity.Course;
import com.online.learning.model.mapper.CategoryMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.service.CategoryService;
import com.online.learning.service.CourseService;
import com.online.learning.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseListController {

    private static final String DEFAULT_PAGE = "1";

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping(value = {"", "/"})
    public String showCourseAll(Model model, @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber) {
        return listByPage(model, pageNumber, 0, null);
    }

    @GetMapping(value = {"/search"})
    public String search(Model model, @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber,
                         @RequestParam(value = "search", required = false) String search) {
        return listByPage(model, pageNumber, 0, search);
    }

    @GetMapping("/{categoryId}")
    public String showCourseByCategory(Model model, @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber,
                                   @PathVariable long categoryId, @RequestParam(value = "search", required = false) String search) {
        return listByPage(model, pageNumber, categoryId, search);
    }

    private String listByPage(Model model, int pageNumber, long categoryId, String search) {
        List<Category> categoryList = categoryService.findAllByActive();

        // pagination
        Pageable pageable = PageRequest.of(pageNumber - 1, ConstantUtil.PAGE_SIZE, Sort.by("title").ascending());
        Page<Course> courseList;

        if (search != null && !search.equalsIgnoreCase("")) {
            courseList = courseService.searchByTitleAndStatusIsTrue(search, pageable);
        } else {
            if (categoryId == 0) {
                courseList = courseService.findByCategoryAndStatusIsTrue(null, pageable);
            } else {
                Category category = categoryService.findById(categoryId);
                courseList = courseService.findByCategoryAndStatusIsTrue(category, pageable);
            }
        }

        List<Course> courseListByPage = new ArrayList<>(courseList.getContent());

        model.addAttribute("courseListDTO", courseMapper.toListDTO(courseListByPage));
        model.addAttribute("categoryListDTO", categoryMapper.toListDTO(categoryList));
        model.addAttribute("totalPage", courseList.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("categoryId", categoryId);

        return "front/course_list";
    }

}
