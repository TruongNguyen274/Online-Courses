package com.online.learning.controller.back;

import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.dto.OrderDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Order;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.model.mapper.OrderMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.CourseService;
import com.online.learning.service.OrderService;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/back/order")
public class OrderController {

    private String redirectUrl = "/back/order/";

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ValidatorUtil validatorUtil;

    @GetMapping(value = {"", "/"})
    public String findAll(Model model, Authentication authentication) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            List<Order> orderList;
            List<Course> courseList = courseService.findByOwner(account);
            if (account.getRole().equalsIgnoreCase("ADMIN")) {
                orderList = orderService.findAll();
            } else {
                orderList = new ArrayList<>();
                for (Course course : courseList) {
                    List<Order> tempt = orderService.findByCourse(course);
                    orderList.addAll(tempt);
                }
            }
            model.addAttribute("orderList", orderMapper.toListDTO(orderList));

            return "back/order_list";
        } catch (Exception ex) {
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/form/{id}"})
    public String edit(Model model, @PathVariable long id, @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Order order = orderService.findById(id);
            if (order == null) {
                return "redirect:" + redirectUrl;
            }
            model.addAttribute("orderDTO", orderMapper.toDTO(order));
            model.addAttribute("errorList", new HashMap<>());

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/order_form";
        } catch (Exception ex) {
            return "redirect:" + redirectUrl;
        }
    }

    @PostMapping(value = "/form")
    public String save(Model model, OrderDTO orderDTO, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                model.addAttribute("orderDTO", orderDTO);
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));

                return "back/order_form";
            } else {
                // save
                Order order = orderService.findById(orderDTO.getId());
                order.setStatus(orderDTO.isStatus());
                orderService.saveEntity(order);

                redirectUrl = "/back/order/form/" + order.getId() + "?action=save&status=success";
                return "redirect:" + redirectUrl;
            }
        } catch (Exception ex) {
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/delete/{id}"})
    public String delete(Model model, @PathVariable long id) {
        try {
            orderService.delete(id);
            redirectUrl = "/back/order" + "?action=save&status=success";

            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            return "redirect:" + redirectUrl;
        }
    }

}
