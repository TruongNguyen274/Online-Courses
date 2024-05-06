package com.online.learning.controller.back;

import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.model.mapper.OrderMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.CourseService;
import com.online.learning.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/back/dashboard")
public class DashboardController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping(value = {"", "/"})
    public String view(Model model) {
        model.addAttribute("totalAccount", accountService.findByStatusIsTrue().size());
        model.addAttribute("totalCourse", courseService.findAllByActive().size());
        model.addAttribute("totalStudent", accountService.findByRole("STUDENT").size());
        model.addAttribute("totalOrder", orderService.findByStatusIsTrue().size());

        model.addAttribute("courseList", courseMapper.toListDTO(courseService.findAllByActive()));
        model.addAttribute("orderList",orderMapper.toListDTO(orderService.findByStatusIsTrue()));
        return "back/dashboard";
    }

}
