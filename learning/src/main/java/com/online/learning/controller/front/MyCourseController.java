package com.online.learning.controller.front;

import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Order;
import com.online.learning.model.mapper.OrderMapper;
import com.online.learning.service.OrderService;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/my-courses")
public class MyCourseController {

    private static final String DEFAULT_PAGE = "1";

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping(value = {"", "/"})
    public String view(Model model, Authentication authentication,
                       @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        if (authentication == null) {
            return "redirect:/login";
        }

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Account account = customUserDetail.getAccount();

        Pageable pageable = PageRequest.of(pageNumber - 1, ConstantUtil.PAGE_SIZE, Sort.by("id").descending());
        Page<Order> orderList = orderService.findByAccount(account, pageable);
        List<Order> orderListByPage = new ArrayList<>(orderList.getContent());

        if (action == null) {
            model.addAttribute("messageDTO", null);
        } else {
            model.addAttribute("messageDTO", new MessageDTO(action, status,
                    "Đăng Ký Khóa Học Thành Công! Vui Lòng Đợi Quản Lý Duyệt!"));
        }

        model.addAttribute("orderListDTO", orderMapper.toListDTO(orderListByPage));
        model.addAttribute("totalPage", orderList.getTotalPages());
        model.addAttribute("currentPage", pageNumber);

        return "front/my_courses";
    }

}
