package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.dto.OrderDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Order;
import com.online.learning.model.mapper.CourseMapper;
import com.online.learning.model.mapper.OrderMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.CourseService;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.DateUtil;
import com.online.learning.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private DateUtil dateUtil;

    @Override
    public OrderDTO toDTO(Order order) {
        if (order == null){
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTimeOrder(DateUtil.convertDateToString(order.getTimeOrder(), ConstantUtil.DATE_PATTERN));
        orderDTO.setPriceView(FormatUtils.formatNumber(order.getPrice()));
        orderDTO.setTotalCost(order.getTotalCost());
        orderDTO.setInvoice(order.getInvoice());

        AccountDTO accountDTO= new AccountDTO();
        Account account = order.getAccount();
        if (account != null){
            accountDTO.setId(account.getId());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setFullName(account.getFullName());
            orderDTO.setAccountDTO(accountDTO);
            orderDTO.setAccountId(accountDTO.getId());
        }

        CourseDTO courseDTO;
        Course course = order.getCourse();
        if (course != null){
            courseDTO = courseMapper.toDTO(course);
            orderDTO.setCourseDTO(courseDTO);
            orderDTO.setCourseId(courseDTO.getId());
        }

        orderDTO.setStatus(order.isStatus());
        // base
        orderDTO.setId(order.getId());
        orderDTO.setVersion(order.getVersion());
        orderDTO.setCreatedOn(dateUtil.convertDateToString(order.getCreatedOn(), "yyyy-MM-dd"));
        orderDTO.setCreatedOn(dateUtil.convertDateToString(order.getUpdatedOn(), "yyyy-MM-dd"));

        return orderDTO;
    }

    @Override
    public List<OrderDTO> toListDTO(List<Order> orderList) {
        if (orderList == null) {
            return null;
        }
        List<OrderDTO> list = new ArrayList<>(orderList.size());
        for (Order order : orderList) {
            OrderDTO orderDTO = toDTO(order);
            if (orderDTO != null) {
                list.add(orderDTO);
            }
        }
        return list;
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        Order order = new Order();
        order.setTimeOrder(DateUtil.convertStringToDate(orderDTO.getTimeOrder(), ConstantUtil.DATE_PATTERN));
        order.setPrice(orderDTO.getPrice());
        order.setTotalCost(orderDTO.getTotalCost());
        order.setAccount(accountService.findById(orderDTO.getAccountId()));
        order.setCourse(courseService.findById(orderDTO.getCourseId()));
        order.setStatus(orderDTO.isStatus());
        return order;
    }
}
