package com.online.learning.controller.front;

import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/home", ""})
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        model.addAttribute("courseListDTO", courseMapper.toListDTO(courseService.getRandomCourses(3)));
        return "front/home";
    }

}
