package com.online.learning.controller.front;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.dto.OrderDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Order;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.model.mapper.OrderMapper;
import com.online.learning.service.CourseService;
import com.online.learning.service.OrderService;
import com.online.learning.service.custom.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/course")
    public String view(Model model, Authentication authentication, @RequestParam long courseId) {
        if (authentication == null) {
            return "redirect:/login";
        }

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Account account = customUserDetail.getAccount();

        Course course = courseService.findById(courseId);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAccountDTO(accountMapper.toDTO(account));
        orderDTO.setCourseDTO(courseMapper.toDTO(course));

        model.addAttribute("orderDTO", orderDTO);

        return "front/checkout";
    }

    @PostMapping("")
    public String saveCheckout(Model model, OrderDTO orderDTO) {
        CourseDTO courseDTO = courseMapper.toDTO(courseService.findById(orderDTO.getCourseDTO().getId()));
        Order order = orderService.checkout(orderDTO, courseDTO);

        model.addAttribute("orderDTO", orderMapper.toDTO(order));
        return "redirect:" + "/my-courses"+ "?action=save&status=success";
    }

}
